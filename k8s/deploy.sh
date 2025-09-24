#!/bin/bash

set -e

NAMESPACE="kafka-hq"
KAFKA_RELEASE="kafka"
AKHQ_RELEASE="akhq"

echo "🚀 Deploying Kafka with AKHQ on Kubernetes"

# Create namespace
echo "📁 Creating namespace: $NAMESPACE"
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

# Apply registry credentials
echo "🔐 Creating registry credentials..."
kubectl apply -f regcred.yaml

# Add AKHQ Helm repository
echo "📦 Adding AKHQ Helm repository..."
helm repo add akhq https://akhq.io/
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

# Deploy AKHQ
echo "🎛️ Deploying AKHQ..."
helm upgrade --install $AKHQ_RELEASE akhq/akhq \
  --namespace $NAMESPACE \
  --values akhq-values.yaml \
  --wait

# Deploy additional services
echo "🏗️ Deploying additional services..."
kubectl apply -f ace-calc-k8s.yaml
kubectl apply -f ace-backend.yaml
kubectl apply -f webapp.yaml

echo "✅ Deployment completed!"
echo ""
echo "🌐 Services available at:"
echo "  - AKHQ: https://kafka-hq.wittyshizard.dev"
echo "  - ACE Backend: https://ace-backend.wittyshizard.dev"
echo "  - Web App: https://ace-web.wittyshizard.dev"
echo "⏳ SSL certificates may take a few minutes to provision"
echo ""
echo "🔍 Check certificate status:"
echo "  kubectl get certificate -n $NAMESPACE"
echo ""
echo "📋 Local access (if needed):"
echo "  kubectl port-forward service/akhq 8080:8080 -n $NAMESPACE"
echo "  kubectl port-forward service/ace-backend 8081:8080 -n $NAMESPACE"
echo "  kubectl port-forward service/webapp 3000:3000 -n $NAMESPACE"
echo ""
echo "📋 Kafka connection details:"
echo "  Bootstrap servers: kafka:9092 (internal)"
echo "  External access: kubectl port-forward service/kafka 9092:9092 -n $NAMESPACE"