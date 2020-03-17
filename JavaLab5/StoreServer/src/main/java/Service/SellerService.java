package Service;

public interface SellerService {
    void sellProduct(String name, int count);
    boolean deleteProduct(String name);
}
