package RPC.orderQuery.api;

import RPC.orderQuery.api.bean.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/16 8:51 上午
 * @describe
 */
public class ProductService implements IProductService{
    private static final Map<Long, String> productMap = new HashMap<Long, String>();
    static {
        productMap.put(1025L, "Nike Air 970,42323");
        productMap.put(1026L, "yeez,3290");
    }

    @Override
    public Product queryId(long id) {
        Product product = new Product();
        String detail = productMap.get(id);
        product.setId(id);
        product.setName(detail.split(",")[0]);
        product.setPrice(Integer.parseInt(detail.split(",")[1]));
        return product;
    }
}
