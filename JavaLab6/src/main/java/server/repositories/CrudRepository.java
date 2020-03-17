package server.repositories;

public interface CrudRepository<ID, PAS> {
    void addUser(ID id, PAS password);

}
