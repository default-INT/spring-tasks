package by.gstu.springsecurity.controller;

import by.gstu.springsecurity.exception.ImageWriteException;
import by.gstu.springsecurity.model.Image;
import by.gstu.springsecurity.service.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    public ResponseEntity<?> loadImage(final @RequestParam("file") MultipartFile file, final @RequestParam("name") String name) {
        try {
            if (file == null || name == null || name.trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            imageService.addImage(file, name);
            return new ResponseEntity<>("Image load", HttpStatus.OK);
        } catch (ImageWriteException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
