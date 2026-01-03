#!/bin/bash
echo "----------- Creating S3 bucket for LocalStack -----------"
awslocal s3api create-bucket \
    --bucket petgram-bucket \
    --region us-east-1

echo "----------- Applying CORS policy to the bucket -----------"
awslocal s3api put-bucket-cors \
    --bucket petgram-bucket \
    --cors-configuration file:///etc/localstack/init/ready.d/cors.json

echo "----------- S3 bucket created and configured -----------"
