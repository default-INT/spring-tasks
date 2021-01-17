package com.asistlab.imagemaker.service;

import com.asistlab.imagemaker.dto.ImageDto;
import com.asistlab.imagemaker.dto.UserDto;
import com.asistlab.imagemaker.exception.FileWriteException;
import com.asistlab.imagemaker.exception.ImageWriteException;
import com.asistlab.imagemaker.model.Image;
import com.asistlab.imagemaker.repository.ImageRepository;
import com.asistlab.imagemaker.util.FileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final static Logger logger = LogManager.getLogger();

    private final static String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public ImageDto addImage(MultipartFile file, String name, UserDto currentUser) {
        String fileName = file.getOriginalFilename();
        String contextFilePath = UUID.randomUUID().toString() + "." + fileName;
        String filePath = Paths.get(UPLOAD_DIR, contextFilePath).toString();

        try {
            FileHelper.writeData(filePath, file);
        } catch (FileWriteException e) {
            logger.warn(e.getMessage());
            throw new ImageWriteException(e);
        }

        Image image = Image.of(name, filePath, file.getContentType(), currentUser.getId());

        imageRepository.save(image);

        return ImageDto.of(image);
    }
}
