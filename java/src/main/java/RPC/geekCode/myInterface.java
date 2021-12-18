package RPC.geekCode;

import org.apache.hadoop.ipc.VersionedProtocol;

/**
 * @author Congpeixin
 * @version 1.0ID
 * @date 2021/7/22 1:05 下午
 * @describe
 */
public interface myInterface extends VersionedProtocol {
    public static final long versionID = 1L;
    public String findName(String studentId);
}
