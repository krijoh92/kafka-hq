package com.example;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"topic-a"}, partitions = 1, controlledShutdown = true)
@TestPropertySource(properties = {
        "app.kafka.enabled=true",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
class IntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    TestRestTemplate rest;

    @Test
    void endToEnd_fromKafkaToApi() {
        // Producer-konfig
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
        senderProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        senderProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaTemplate<String, String> template =
                new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(senderProps));
        template.setDefaultTopic("topic-a");

        String payload = """
                [
                  {"Area":"NO1","Quantity":300,"TimeStamp":"2025-01-01T00:00:00Z"},
                  {"Area":"NO1","Quantity":150,"TimeStamp":"2025-01-01T01:00:00Z"}
                ]
                """;
        template.sendDefault(payload);

        String url = String.format("http://localhost:%d/api/timeseries?area=NO1&start=2025-01-01T00:00:00Z&end=2025-01-01T02:00:00Z", port);

        await().atMost(20, SECONDS).pollInterval(1, SECONDS).untilAsserted(() -> {
            var entity = rest.getForEntity(url, String.class);
            assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
            String body = entity.getBody();
            assertThat(body).contains("\"area\":\"NO1\"");
            assertThat(body).contains("\"timestamp\":\"2025-01-01T00:00:00Z\"");
            assertThat(body).contains("\"value\":300");
            assertThat(body).contains("\"timestamp\":\"2025-01-01T01:00:00Z\"");
            assertThat(body).contains("\"value\":150");
        });
    }
}
