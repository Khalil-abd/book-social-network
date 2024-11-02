package com.ka.bsn.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
public class FileUtils {

    private FileUtils(){}

    public static byte[] readFileFromLocation(String fileUrl){
        if(fileUrl == null || fileUrl.isBlank()){
            return null;
        }
        try {
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        }catch (IOException e){
            log.warn("No file found in the path {}", fileUrl);
        }
        return null;
    }
}
