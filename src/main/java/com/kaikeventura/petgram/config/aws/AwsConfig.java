package com.kaikeventura.petgram.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.util.Optional;

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

    private StaticCredentialsProvider getCredentialsProvider() {
        if (awsAccessKeyId.isPresent() && awsSecretAccessKey.isPresent()) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(awsAccessKeyId.get(), awsSecretAccessKey.get())
            );
        }
        return null; // Or default provider
    }

    @Bean
    public S3Client s3Client() {
        var clientBuilder = S3Client.builder().region(Region.of(awsRegion));
        var credentialsProvider = getCredentialsProvider();
        if (credentialsProvider != null) {
            clientBuilder.credentialsProvider(credentialsProvider);
        }

        s3EndpointUrl.ifPresent(endpoint -> {
            clientBuilder.endpointOverride(URI.create(endpoint));
            clientBuilder.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());
        });

        return clientBuilder.build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        var presignerBuilder = S3Presigner.builder().region(Region.of(awsRegion));
        var credentialsProvider = getCredentialsProvider();
        if (credentialsProvider != null) {
            presignerBuilder.credentialsProvider(credentialsProvider);
        }

        s3EndpointUrl.ifPresent(endpoint -> {
            presignerBuilder.endpointOverride(URI.create(endpoint));
            // This was the missing piece for the S3Presigner
            presignerBuilder.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());
        });

        return presignerBuilder.build();
    }
}
