package server.services;

import org.springframework.web.multipart.MultipartFile;
import server.entities.dto.FileModelDto;
import server.entities.dto.UserDto;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileService {
    public FileModelDto saveFile(MultipartFile file, UserDto userDto);
    public Optional<FileModelDto> findFileByStorageName(String storageName);
    public List<FileModelDto> findAllFiles();
    public File downloadFile(String storageName);
}
