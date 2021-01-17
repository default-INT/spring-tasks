package com.asistlab.imagemaker.controller;

import com.asistlab.imagemaker.dto.ImageDto;
import com.asistlab.imagemaker.exception.ImageWriteException;
import com.asistlab.imagemaker.model.Image;
import com.asistlab.imagemaker.service.ImageService;
import com.asistlab.imagemaker.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uploads")
@CrossOrigin
public class ImageRestController {

    private final static Logger logger = LogManager.getLogger();

    private final ImageService imageService;
    private final UserService userService;

    public ImageRestController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping
    public List<Image> findAllImages() {
        return imageService.findAll();
    }

    @PostMapping("/load-img")
    public ImageDto loadImage(final ImageDto imageDto) {
        try {
            if (imageDto.getFile() == null || imageDto.getName() == null || imageDto.getName().trim().isEmpty()) {
                throw new ImageWriteException();
            }
            return imageService.addImage(imageDto.getFile(), imageDto.getName(), userService.getCurrentUser());
        } catch (ImageWriteException e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
