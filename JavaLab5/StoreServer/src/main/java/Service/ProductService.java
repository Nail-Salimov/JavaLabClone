package Service;

import DB.BdLogical;

public class ProductService {
    private BdLogical bdLogical;

    public ProductService(BdLogical bdLogical) {
        this.bdLogical = bdLogical;
    }

    public void addProduct(String name, int count) {
        bdLogical.addProduct(name , count);
    }

}
