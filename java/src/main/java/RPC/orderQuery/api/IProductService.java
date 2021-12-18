package RPC.orderQuery.api;

import RPC.orderQuery.api.bean.Product;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/16 8:50 上午
 * @describe
 */
public interface IProductService {
    public Product queryId(long id);
}
