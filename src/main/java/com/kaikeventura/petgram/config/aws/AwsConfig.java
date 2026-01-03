package com.kaikeventura.petgram.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.endpoint-url:#{null}}")
    private Optional<String> s3EndpointUrl;

    @Value("${aws.access-key-id:#{null}}")
    private Optional<String> awsAccessKeyId;

    @Value("${aws.secret-access-key:#{null}}")
    private Optional<String> awsSecretAccessKey;

    @Bean
    public S3Client s3Client() {
        var clientBuilder = S3Client.builder().region(Region.of(awsRegion));

        // For local development with MinIO
        s3EndpointUrl.ifPresent(endpoint -> {
            clientBuilder.endpointOverride(URI.create(endpoint));
            clientBuilder.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());
        });

        // Use static credentials if provided (for MinIO)
        if (awsAccessKeyId.isPresent() && awsSecretAccessKey.isPresent()) {
            clientBuilder.credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(awsAccessKeyId.get(), awsSecretAccessKey.get())
                    )
            );
        }

        return clientBuilder.build();
    }
}
