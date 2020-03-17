package server.loader;

import org.springframework.web.multipart.MultipartFile;
import server.entities.dto.UserDto;
import server.entities.model.FileModel;

import java.io.File;

public interface FileLoader {

    FileModel uploadFile(MultipartFile file, UserDto userDto);
    File downloadFile(String storageName);
}
