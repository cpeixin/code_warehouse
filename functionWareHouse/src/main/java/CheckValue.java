import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/5/18 8:44 上午
 * @describe
 */
public class CheckValue {

    public static<T> T checkNotNull(@Nullable T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }



    public static void main(String[] args){
//        String reference = "Flink Job ID 24y23748724";
        String reference = null;
        String errorMessage = "error to get job name";
        String res = checkNotNull(reference, errorMessage);
        System.out.println(res);
    }


}
