package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Post;
import com.kaikeventura.petgram.dto.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public abstract class PostMapper {

    @Autowired
    private S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Mapping(target = "likeCount", expression = "java(post.getLikes() != null ? post.getLikes().size() : 0)")
    @Mapping(target = "commentCount", expression = "java(post.getComments() != null ? post.getComments().size() : 0)")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "photoUrl", target = "photoUrl", qualifiedByName = "generatePresignedUrl")
    public abstract PostResponse toPostResponse(Post post);

    @Named("generatePresignedUrl")
    protected String generatePresignedUrl(String objectKey) {
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
            // For now, returning null or a placeholder URL is a safe fallback.
            return null;
        }
    }
}
