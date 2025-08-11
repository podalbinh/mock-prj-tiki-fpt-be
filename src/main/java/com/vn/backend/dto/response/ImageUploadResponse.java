package com.vn.backend.dto.response;

public class ImageUploadResponse {
    private String url;
    private String id;
    private String error;

    public ImageUploadResponse(String url, String id) {
        this.url = url;
        this.id = id;
    }

    public ImageUploadResponse(String error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getError() {
        return error;
    }
}
