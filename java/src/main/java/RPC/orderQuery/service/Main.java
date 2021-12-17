package RPC.orderQuery.service;

import RPC.orderQuery.api.IProductService;
import RPC.orderQuery.api.bean.Product;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/16 9:01 上午
 * @describe
 */
public class Main {
    public static void main(String[] args) {
        IProductService productService = (IProductService) RPC.orderQuery.api.Main.rpc(IProductService.class);
        Product product = productService.queryId(1025);
        System.out.println(product);
    }
}
