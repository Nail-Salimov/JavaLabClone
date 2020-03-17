package server.repositories;

public interface SellerRepository extends CrudRepository<Long, String> {
    void addProduct(String name, int count);
    boolean deleteProduct(String name);
}
