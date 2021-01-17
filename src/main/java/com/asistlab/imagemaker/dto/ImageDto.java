package com.asistlab.imagemaker.dto;

import com.asistlab.imagemaker.model.Image;
import org.springframework.web.multipart.MultipartFile;

public class ImageDto {
    public static ImageDto of(Image image) {
        ImageDto imageDto = new ImageDto();

        imageDto.setName(image.getName());
        imageDto.setUser(UserDto.of(image.getUser()));
        imageDto.setFilePath(image.getFilePath());
        imageDto.setContentType(image.getContentType());

        return imageDto;
    }

    private MultipartFile file;
    private String name;
    private String filePath;
    private String contentType;
    private UserDto user;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto userDto) {
        this.user = userDto;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
