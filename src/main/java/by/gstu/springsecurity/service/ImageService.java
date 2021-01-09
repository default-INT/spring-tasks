package by.gstu.springsecurity.service;

import by.gstu.springsecurity.exception.ImageWriteException;
import by.gstu.springsecurity.model.Image;
import by.gstu.springsecurity.repository.ImageEffectRepository;
import by.gstu.springsecurity.repository.ImageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final static Logger logger = LogManager.getLogger();

    private final static String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    private final ImageRepository imageRepository;
    private final ImageEffectRepository imageEffectRepository;

    public ImageService(ImageRepository imageRepository, ImageEffectRepository imageEffectRepository) {
        this.imageRepository = imageRepository;
        this.imageEffectRepository = imageEffectRepository;
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public void addImage(MultipartFile file, String name) {
        String fileName = file.getOriginalFilename();
        String contextFilePath = UUID.randomUUID().toString() + "." + fileName;
        String filePath = Paths.get(UPLOAD_DIR, contextFilePath).toString();

        BufferedImage bufferedImage;

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)))) {
            stream.write(file.getBytes());
            bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            logger.warn(e.getMessage());
            throw new ImageWriteException(e);
        }
        Image image = new Image();

        image.setName(name);
        image.setContentType(file.getContentType());
        image.setFilePath(contextFilePath);
        image.setHeight(bufferedImage.getHeight());
        image.setWidth(bufferedImage.getWidth());

        imageRepository.save(image);
    }
}
