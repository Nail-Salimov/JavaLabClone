package server.loader;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import server.entities.dto.UserDto;
import server.entities.model.FileModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component()
public class FileLoaderImpl implements FileLoader {

    @Value("${db.filePath}")
    private String storagePath;

    @Override
    public FileModel uploadFile(MultipartFile file, UserDto userDto) {
        try {


            String originalName = file.getOriginalFilename();
            FileModel fileModel = new FileModel(originalName, createStorageName(originalName));

            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(getPath() + fileModel.getStorageName())));
            stream.write(bytes);
            stream.close();

            return fileModel;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File downloadFile(String storageName) {
        return new File(getPath() + storageName);
    }

    private String getPath() {
        return storagePath;
    }

    private static String createStorageName(String originalName) {
        String storageName = UUID.randomUUID().toString();
        String type = FilenameUtils.getExtension(originalName);
        return storageName + "." + type;
    }

}

