package com.asistlab.imagemaker.dto;

import com.asistlab.imagemaker.model.Image;
import org.springframework.web.multipart.MultipartFile;

public class ImageDto {
    public static ImageDto of(Image image) {
        ImageDto imageDto = new ImageDto();

        imageDto.setId(image.getId());
        imageDto.setName(image.getName());
        imageDto.setUser(UserDto.of(image.getUser()));
        imageDto.setFilePath(image.getFilePath());
        imageDto.setContentType(image.getContentType());
        imageDto.setHeight(image.getHeight());
        imageDto.setWidth(image.getWidth());

        return imageDto;
    }

    private Long id;
    private MultipartFile file;
    private String name;
    private String filePath;
    private String contentType;
    private UserDto user;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
