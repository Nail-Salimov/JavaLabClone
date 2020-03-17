package server.entities.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileModel {
    private String originalName;
    private String storageName;

    public FileModel(String originalName){
        this.originalName = originalName;
    }
}
