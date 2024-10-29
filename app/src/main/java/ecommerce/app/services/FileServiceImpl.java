package ecommerce.app.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths; 
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

@Override
public String uploadImageToServer(String path,MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        if(originalFileName == null) {
            throw new RuntimeException("Please choose a file to upload");
        }
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String filePath = path+File.separator+fileName;
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return fileName;
    }

}

