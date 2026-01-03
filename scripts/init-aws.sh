#!/bin/bash
echo "----------- Creating S3 bucket for LocalStack -----------"
awslocal s3api create-bucket \
    --bucket petgram-bucket \
    --region us-east-1

echo "----------- S3 bucket created -----------"
