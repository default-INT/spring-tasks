package com.asistlab.imagemaker.service;

import com.asistlab.imagemaker.dto.ImageDto;
import com.asistlab.imagemaker.dto.UserDto;
import com.asistlab.imagemaker.exception.FileWriteException;
import com.asistlab.imagemaker.exception.ImageWriteException;
import com.asistlab.imagemaker.model.Image;
import com.asistlab.imagemaker.repository.ImageRepository;
import com.asistlab.imagemaker.repository.UserRepository;
import com.asistlab.imagemaker.util.FileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final static Logger logger = LogManager.getLogger();

    private final static String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    public ImageService(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public List<ImageDto> findAll() {
        return imageRepository.findAll().stream().map(ImageDto::of).collect(Collectors.toList());
    }

    public ImageDto addImage(ImageDto imageDto, UserDto currentUser) {
        String fileName = imageDto.getFile().getOriginalFilename();
        String contextFilePath = UUID.randomUUID().toString() + "." + fileName;
        String filePath = Paths.get(UPLOAD_DIR, contextFilePath).toString();

        try {
            FileHelper.writeData(filePath, imageDto.getFile());
        } catch (FileWriteException e) {
            logger.warn(e.getMessage());
            throw new ImageWriteException(e);
        }
        Image image = Image.of(imageDto.getName(), contextFilePath, imageDto.getFile().getContentType(), imageDto.getWidth(),
                imageDto.getHeight(), userRepository.findById(currentUser.getId())
                .orElseThrow(IllegalArgumentException::new));

        imageRepository.save(image);

        return ImageDto.of(image);
    }
}
