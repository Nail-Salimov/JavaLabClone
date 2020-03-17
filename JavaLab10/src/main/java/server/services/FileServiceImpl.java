package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import server.entities.dto.FileModelDto;
import server.entities.dto.UserDto;
import server.entities.model.FileModel;
import server.loader.FileLoader;
import server.repositories.FileRepository;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileLoader fileLoader;

    @Override
    public FileModelDto saveFile(MultipartFile file, UserDto userDto) {

        FileModel fileModel = fileLoader.uploadFile(file, userDto);
        fileRepository.save(fileModel);
        return FileModelDto.getFileDto(fileModel);
    }

    @Override
    public Optional<FileModelDto> findFileByStorageName(String storageName) {
        Optional<FileModel> optionalFile = fileRepository.findByStorageName(storageName);
        return optionalFile.map(FileModelDto::getFileDto);
    }

    @Override
    public List<FileModelDto> findAllFiles() {
        List<FileModel> list = fileRepository.findAll();
        List<FileModelDto> fileDtoList = new LinkedList<>();
        for (FileModel f : list) {
            fileDtoList.add(FileModelDto.getFileDto(f));
        }
        return fileDtoList;
    }

    @Override
    public File downloadFile(String storageName){
        return fileLoader.downloadFile(storageName);
    }
}


