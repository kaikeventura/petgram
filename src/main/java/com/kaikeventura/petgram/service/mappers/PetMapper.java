package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.dto.PetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Mapper(componentModel = "spring")
public abstract class PetMapper {

    @Autowired
    private S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "avatarUrl", target = "avatarUrl", qualifiedByName = "generatePresignedUrl")
    public abstract PetResponse toPetResponse(Pet pet);

    @Named("generatePresignedUrl")
    public String generatePresignedUrl(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return null;
        }

        try {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .getObjectRequest(req -> req.bucket(bucketName).key(objectKey))
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        } catch (Exception e) {
            // In a real application, you should log this error.
            return null;
        }
    }
}
