package com.matriz.backend.modules.images;

import com.matriz.backend.modules.config.TestContainersConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestContainersConfig.class)
@Slf4j
public class S3IntegrationTest {
    @Autowired
    private ImageProcessingService imageProcessingService;

    @Autowired
    private S3Client s3Client;

    @Value("${cloud.r2.bucket-name}")
    private String bucketName;

    @BeforeEach
    void setUp() {
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    }

    @Test
    @DisplayName("Should process, upload, rename and delete image in LocalStack S3")
    void s3Operations_ShouldSucceed() throws IOException {
        String TINY_IMAGE_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=";
        // Decode mock image
        byte[] imageBytes = Base64.getDecoder().decode(TINY_IMAGE_BASE64);
        MultipartFile file = new MockMultipartFile("photo", "test.png", "image/png", imageBytes);
        log.info("Created image {} to S3",  file.getOriginalFilename());

        // Get image size
        long fileSize = file.getSize();
        log.info("Image size: {} bytes", fileSize);

        // Get image dimensions
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage != null) {
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                log.info("Image dimensions: {}x{}", width, height);
            }
        }

        // Test 1: Upload
        String uploadedUrl = imageProcessingService.processAndUpload(file, "Matias Prado", true);
        log.info("Uploaded image with url: <<{}>> to S3", uploadedUrl);
        assertNotNull(uploadedUrl);
        assertTrue(uploadedUrl.contains("matias-prado.jpg"));

        // Get uploaded image from S3 and check its properties
        try (var s3Object = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key("matias-prado.jpg")
                .build())) {

            // Get image size from S3
            long uploadedFileSize = s3Object.response().contentLength();
            log.info("Uploaded image size: {} bytes", uploadedFileSize);

            // Get image dimensions from S3
            BufferedImage bufferedImage = ImageIO.read(s3Object);
            if (bufferedImage != null) {
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                log.info("Uploaded image dimensions: {}x{}", width, height);
            }
        }

        // Test 2: Rename
        String renamedUrl = imageProcessingService.renameImage(uploadedUrl, "Profesor Matriz");
        log.info("Renamed image with url: <<{}>> from S3", renamedUrl);
        assertNotNull(renamedUrl);
        assertTrue(renamedUrl.contains("profesor-matriz.jpg"));

        // Test 3: Delete
        imageProcessingService.deleteImage(renamedUrl);
        log.info("Deleted image with url: <<{}>> from S3", renamedUrl);

        // Test 4: Get deleted photo, S3 throws NoSuchKeyException
        assertThrows(NoSuchKeyException.class, () ->
                s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key("profesor-matriz.jpg")
                        .build())
        );
        log.info("Retrieval of image with url: <<{}>> must have thrown an error here", uploadedUrl);
    }

}
