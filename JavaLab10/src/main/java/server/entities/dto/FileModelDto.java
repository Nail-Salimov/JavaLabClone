package server.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.entities.model.FileModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileModelDto {
    private String originalName;
    private String storageName;

    public static FileModelDto getFileDto(FileModel file){
        return new FileModelDto(file.getOriginalName(), file.getStorageName());
    }
}
