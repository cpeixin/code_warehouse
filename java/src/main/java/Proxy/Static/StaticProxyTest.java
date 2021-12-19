package Proxy.Static;

public class StaticProxyTest {
    public static void main(String[] args) {
        AdminService adminService = new AdminServiceImpl();
        AdminServiceStaticProxy proxy = new AdminServiceStaticProxy(adminService);

        proxy.update();
        System.out.println("=============================");
        proxy.find();
    }
}
