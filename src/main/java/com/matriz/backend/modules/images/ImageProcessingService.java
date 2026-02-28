package com.matriz.backend.modules.images;

import com.matriz.backend.shared.interfaces.PhotoOwner;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;

@Service
public class ImageProcessingService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String publicUrl;

    public ImageProcessingService(
            S3Client s3Client,
            @Value("${cloud.r2.bucket-name}") String bucketName,
            @Value("${cloud.r2.public-url}") String publicUrl) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.publicUrl = publicUrl;
    }

    public String processAndUpload(MultipartFile photo, String professorName, boolean isProfessor) throws IOException {
        String fileName = formatNames(professorName) + ".jpg";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.Builder<? extends InputStream> thumbnailBuilder = Thumbnails.of(photo.getInputStream());

        if (isProfessor) {
            // For professors, create a square 800x800 image, cropping from the center if necessary.
            thumbnailBuilder.crop(Positions.CENTER).size(800, 800);
        } else {
            // For other images, resize to fit within 1280x720, preserving aspect ratio.
            thumbnailBuilder.size(1280, 720);
        }

        thumbnailBuilder
                .outputFormat("jpg")
                .outputQuality(0.85)
                .toOutputStream(outputStream);

        byte[] processedImage = outputStream.toByteArray();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(processedImage));

        return publicUrl + "/" + fileName;
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }
        String fileName = getFileNameFromUrl(imageUrl);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public String renameImage(String oldImageUrl, String newProfessorName) {
        if (oldImageUrl == null || oldImageUrl.isBlank()) {
            return null;
        }
        String oldFileName = getFileNameFromUrl(oldImageUrl);
        String newFileName = formatNames(newProfessorName) + ".jpg";

        if (oldFileName.equals(newFileName)) {
            return oldImageUrl;
        }

        CopyObjectRequest copyReq = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(oldFileName)
                .destinationBucket(bucketName)
                .destinationKey(newFileName)
                .build();

        s3Client.copyObject(copyReq);
        deleteImage(oldImageUrl);

        return publicUrl + "/" + newFileName;
    }

    public void handlePhotoUpdate(
            PhotoOwner entity,
            MultipartFile newPhoto,
            String newName,
            boolean isProfessor) throws IOException {

        String oldPhotoUrl = entity.getPhotoUrl();
        boolean nameChanged = !entity.getName().equals(newName);

        if (newPhoto != null) {
            deleteImage(oldPhotoUrl);
            entity.setPhotoUrl(processAndUpload(newPhoto, newName, isProfessor));
        } else if (nameChanged) {
            entity.setPhotoUrl(renameImage(oldPhotoUrl, newName));
        }
    }

    /* Helper methods */
    private String getFileNameFromUrl(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }
        // Take everything after the last slash.
        // This works regardless of what publicUrl is currently configured to.
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash == -1 || lastSlash == url.length() - 1) {
            // No slash found, or slash is the last character — URL is malformed
            throw new IllegalArgumentException("Cannot extract filename from URL: " + url);
        }
        return url.substring(lastSlash + 1);
    }

    private String formatNames(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        s = s.toLowerCase();
        return s.replace(" ", "-");
    }
}
