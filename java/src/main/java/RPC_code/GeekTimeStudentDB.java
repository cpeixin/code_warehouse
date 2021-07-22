package RPC_code;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/22 2:20 下午
 * @describe
 */
public class GeekTimeStudentDB {
    public static String getStudentName(String studentID) {
        HashMap<String, String> studentDB = new HashMap<String, String>();
        studentDB.put("20200343070326", "丛培欣");
        studentDB.put("20210123456789", "心心");
        return studentDB.get(studentID);
    }
}
