package DB;

public interface SellerLogical {
    void addProduct(String name, int count);
    boolean deleteProduct(String name);
}
