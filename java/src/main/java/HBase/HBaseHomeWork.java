package HBase;
import HBase.HBaseClient;

import java.io.IOException;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/31 9:43 下午
 * @describe
 */
public class HBaseHomeWork {
    public static void main(String[] args) throws IOException {
        // 创建表
        HBaseClient.createTable("congpeixin:student","info","score");

        //插入数据
        try {
            HBaseClient.putData("congpeixin:student","Tom","info", "student_id","20210000000001");
            HBaseClient.putData("congpeixin:student","Tom","info", "class","1");
            HBaseClient.putData("congpeixin:student","Tom","score", "understanding","75");
            HBaseClient.putData("congpeixin:student","Tom","score", "programming","82");

            HBaseClient.putData("congpeixin:student","congpeixin","info", "student_id","G20200343070326");
            HBaseClient.putData("congpeixin:student","congpeixin","info", "class","5");
            HBaseClient.putData("congpeixin:student","congpeixin","score", "understanding","100");
            HBaseClient.putData("congpeixin:student","congpeixin","score", "programming","100");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
