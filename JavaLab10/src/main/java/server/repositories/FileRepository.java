package server.repositories;

import server.entities.model.FileModel;

import java.util.Optional;

public interface FileRepository extends CrudRepository<String, FileModel> {
    Optional<FileModel> findByOriginalName(String originalName);
    Optional<FileModel> findByStorageName(String storageName);

    void deleteByOriginalName(String originalName);
    void deleteByStorageName(String storageName);


}
