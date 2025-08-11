package com.vn.backend.controllers;

import com.vn.backend.dto.response.ImageUploadResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.services.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private final CloudinaryService cloudinaryService;

    public ImageUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image to Cloudinary")
    public ResponseData<ImageUploadResponse> uploadImage(
            @Schema(type = "string", format = "binary") @RequestPart("file") MultipartFile file) {
        try {
            Map<String, String> uploadResult = cloudinaryService.uploadImage(file);
            ImageUploadResponse response = new ImageUploadResponse(uploadResult.get("url"), uploadResult.get("id"));
            return ResponseData.success(response);
        } catch (Exception e) {
            return ResponseData.error(new ImageUploadResponse("Upload failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{publicId}")
    @Operation(summary = "Delete image from Cloudinary")
    public ResponseData<String> deleteImage(@PathVariable String publicId) {
        try {
            cloudinaryService.deleteImage(publicId);
            return ResponseData.success("Image deleted successfully");
        } catch (Exception e) {
            return ResponseData.error("Delete failed: " + e.getMessage());
        }
    }

}
