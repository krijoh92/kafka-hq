# Kafka + AKHQ Kubernetes Deployment

This repository contains Helm configurations to deploy Apache Kafka with AKHQ GUI on Kubernetes.

## Public Services

- **AKHQ (Kafka UI)**: https://kafka-hq.wittyshizard.dev
- **ACE Backend API**: https://ace-backend.wittyshizard.dev
- **Webapp Frontend**: https://ace-web.wittyshizard.dev/

## Prerequisites

- Kubernetes cluster
- Helm 3.x installed
- kubectl configured to access your cluster
- NGINX Ingress Controller installed
- cert-manager installed with existing `letsencrypt` ClusterIssuer
- DNS record: `kafka-hq.wittyshizard.dev` pointing to your ingress

## Quick Start

1. **Deploy everything:**
   ```bash
   ./deploy.sh
   ```

2. **Access AKHQ:**
   - Web UI: https://kafka-hq.wittyshizard.dev
   - Local access: `kubectl port-forward service/akhq 8080:8080 -n kafka-hq`

## Manual Deployment

### 1. Add AKHQ Helm Repository
```bash
helm repo add akhq https://akhq.io/
helm repo update
```

### 2. Create Namespace
```bash
kubectl create namespace kafka-hq
```

### 3. Deploy Kafka
```bash
helm upgrade --install kafka oci://registry-1.docker.io/bitnamicharts/kafka \
  --namespace kafka-hq \
  --values kafka-values.yaml
```

### 4. Deploy AKHQ
```bash
helm upgrade --install akhq akhq/akhq \
  --namespace kafka-hq \
  --values akhq-values.yaml
```

## Configuration

### Kafka Configuration (`kafka-values.yaml`)
- KRaft mode enabled (no Zookeeper)
- 3 controller replicas
- 3 broker replicas
- Plaintext authentication
- JMX metrics enabled

### AKHQ Configuration (`akhq-values.yaml`)
- Connects to Kafka cluster automatically
- Access logging enabled
- Schema registry support configured
- Connect support configured

## Useful Commands

### Port Forward Services
```bash
# AKHQ Web UI
kubectl port-forward service/akhq 8080:8080 -n kafka-hq

# Kafka broker
kubectl port-forward service/kafka 9092:9092 -n kafka-hq
```

### Check Pod Status
```bash
kubectl get pods -n kafka-hq
```

### Check SSL Certificate
```bash
kubectl get certificate kafka-hq-tls -n kafka-hq
kubectl describe certificate kafka-hq-tls -n kafka-hq
```

### View Logs
```bash
# Kafka logs
kubectl logs -f deployment/kafka -n kafka-hq

# AKHQ logs
kubectl logs -f deployment/akhq -n kafka-hq
```

### Cleanup
```bash
./reset.sh
```

## Customization

Edit the values files to customize your deployment:
- `kafka-values.yaml` - Kafka cluster configuration
- `akhq-values.yaml` - AKHQ GUI configuration

For production use, consider:
- Enabling authentication and authorization
- Configuring ingress for external access
- Setting up persistent volumes
- Configuring resource limits and requests
- Enabling TLS/SSL