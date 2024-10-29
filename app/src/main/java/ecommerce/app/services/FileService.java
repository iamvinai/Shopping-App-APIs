package ecommerce.app.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String uploadImageToServer(String path,MultipartFile image) throws IOException;
}
