#!/bin/bash

set -e

NAMESPACE="kafka-hq"
KAFKA_RELEASE="kafka"
AKHQ_RELEASE="akhq"

echo "🧹 Cleaning up Kafka + AKHQ deployment"

# Delete Helm releases
echo "🗑️ Removing Helm releases..."
helm uninstall $AKHQ_RELEASE -n $NAMESPACE --ignore-not-found
helm uninstall $KAFKA_RELEASE -n $NAMESPACE --ignore-not-found

# Wait a bit for resources to be cleaned up
echo "⏳ Waiting for resources to be cleaned up..."
sleep 10

# Force delete any remaining pods
echo "🔄 Force deleting any remaining pods..."
kubectl delete pods --all -n $NAMESPACE --force --grace-period=0 --ignore-not-found

# Delete PVCs (persistent volume claims)
echo "💾 Deleting persistent volume claims..."
kubectl delete pvc --all -n $NAMESPACE --ignore-not-found

# Delete secrets and configmaps
echo "🔐 Deleting secrets and configmaps..."
kubectl delete secrets --all -n $NAMESPACE --ignore-not-found
kubectl delete configmaps --all -n $NAMESPACE --ignore-not-found

# Delete services
echo "🌐 Deleting services..."
kubectl delete services --all -n $NAMESPACE --ignore-not-found

# Delete namespace
echo "📁 Deleting namespace: $NAMESPACE"
kubectl delete namespace $NAMESPACE --ignore-not-found

# Note: Not removing existing 'letsencrypt' ClusterIssuer as it may be used by other apps

echo "✅ Cleanup completed!"
echo ""
echo "💡 You can now run ./deploy.sh to start fresh"