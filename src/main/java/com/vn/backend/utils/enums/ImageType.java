package com.vn.backend.utils.enums;

public enum ImageType {
    COVER(1, "Cover image"),         // Ảnh bìa chính
    GALLERY(2, "Gallery image"),     // Ảnh phụ (nhiều góc chụp)
    BANNER(3, "Banner image"),       // Ảnh banner quảng cáo
    OTHER(4, "Other image");         // Loại ảnh khác

    private final int value;
    private final String description;

    ImageType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

}
