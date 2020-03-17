package Service;

import DB.SellerLogical;

public class SellerServiceImpl implements SellerService {
    private SellerLogical sellerLogical;

    public SellerServiceImpl(SellerLogical sellerLogical) {
        this.sellerLogical = sellerLogical;
    }

    @Override
    public void sellProduct(String name, int count) {
        sellerLogical.addProduct(name, count);
    }

    @Override
    public boolean deleteProduct(String name) {
        return sellerLogical.deleteProduct(name);
    }
}
