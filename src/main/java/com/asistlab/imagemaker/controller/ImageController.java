package com.asistlab.imagemaker.controller;

import com.asistlab.imagemaker.exception.ImageWriteException;
import com.asistlab.imagemaker.model.Image;
import com.asistlab.imagemaker.service.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/uploads")
@CrossOrigin
public class ImageController {

    private final static Logger logger = LogManager.getLogger();

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public List<Image> findAllImages() {
        return imageService.findAll();
    }

    @PostMapping("/load-img")
    public Image loadImage(final @RequestParam("file") MultipartFile file, final @RequestParam("name") String name) {
        try {
            if (file == null || name == null || name.trim().isEmpty()) {
                throw new ImageWriteException();
            }
            return imageService.addImage(file, name);
        } catch (ImageWriteException e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
