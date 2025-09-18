#!/bin/bash

set -e

NAMESPACE="kafka-hq"
KAFKA_RELEASE="kafka"
KAFKA_UI_RELEASE="kafka-ui"

echo "🚀 Deploying Kafka with Kafka-UI (ARM64 compatible)"

# Create namespace
echo "📁 Creating namespace: $NAMESPACE"
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

# Add Kafka-UI Helm repository
echo "📦 Adding Kafka-UI Helm repository..."
helm repo add kafka-ui https://provectus.github.io/kafka-ui-charts
helm repo update

# Deploy Kafka using OCI registry
echo "☕ Deploying Kafka cluster..."
helm upgrade --install $KAFKA_RELEASE oci://registry-1.docker.io/bitnamicharts/kafka \
  --namespace $NAMESPACE \
  --values kafka-values.yaml \
  --wait

# Wait for Kafka to be ready
echo "⏳ Waiting for Kafka to be ready..."
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=kafka --timeout=300s -n $NAMESPACE

# Deploy Kafka-UI
echo "🎛️ Deploying Kafka-UI..."
helm upgrade --install $KAFKA_UI_RELEASE kafka-ui/kafka-ui \
  --namespace $NAMESPACE \
  --values kafka-ui-values.yaml \
  --wait

echo "✅ Deployment completed!"
echo ""
echo "🌐 Kafka-UI is available at: https://kafka-hq.wittyshizard.dev"
echo "⏳ SSL certificate may take a few minutes to provision"
echo ""
echo "🔍 Check certificate status:"
echo "  kubectl get certificate kafka-hq-tls -n $NAMESPACE"
echo ""
echo "📋 Local access (if needed):"
echo "  kubectl port-forward service/kafka-ui 8080:8080 -n $NAMESPACE"
echo ""
echo "📋 Kafka connection details:"
echo "  Bootstrap servers: kafka:9092 (internal)"
echo "  External access: kubectl port-forward service/kafka 9092:9092 -n $NAMESPACE"