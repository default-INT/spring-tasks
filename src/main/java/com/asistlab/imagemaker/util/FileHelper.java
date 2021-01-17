package com.asistlab.imagemaker.util;

import com.asistlab.imagemaker.exception.FileWriteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    private static final Logger logger = LogManager.getLogger();

    public static void writeData(String filePath, MultipartFile file) {
        try {
            writeData(filePath, file.getBytes());
        } catch (IOException e) {
            logger.warn(e.getMessage());
            throw new FileWriteException(e);
        }
    }

    public static void writeData(String filePath, byte[] data) {
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)))) {
            stream.write(data);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            throw new FileWriteException(e);
        }
    }
}
