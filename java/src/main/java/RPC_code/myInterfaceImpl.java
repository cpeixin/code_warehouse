package RPC_code;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/22 1:07 下午
 * @describe
 */
public class myInterfaceImpl implements myInterface{
    @Override
    public String findName(String studentId) {
        return GeekTimeStudentDB.getStudentName(studentId);
    }
    @Override
    public long getProtocolVersion(String s, long l) throws IOException {
        return myInterface.versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String s, long l, int i) throws IOException {
        return null;
    }

}
