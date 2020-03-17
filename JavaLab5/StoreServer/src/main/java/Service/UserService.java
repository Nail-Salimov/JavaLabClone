package Service;

import DB.BdLogical;

public class UserService {
    private BdLogical bdLogical;

    public UserService(BdLogical bdLogical) {
        this.bdLogical = bdLogical;
    }

    public boolean buy(String name, int count){
        return bdLogical.buyProduct(name, count);
    }
}
