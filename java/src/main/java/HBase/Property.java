package HBase;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Property {
    private static Properties contextProperties;
    private final static String CONFIG_NAME = "config.properties";
    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NAME);
        System.setProperty("hadoop.home.dir", "/usr/local/service/hadoop");
        contextProperties = new Properties();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            contextProperties.load(inputStreamReader);
        } catch (IOException e) {
            System.err.println(">>>资源文件加载失败!<<<");
            e.printStackTrace();
        }
        System.out.println(">>>资源文件加载成功<<<");
    }

    public static String getStrValue(String key) {
        return contextProperties.getProperty(key);
    }

    public static Integer getIntegerValue(String key) {
        String value = getStrValue(key);
        return Integer.parseInt(value);
    }

    public static Properties getKafkaProperties(String groupId) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", Property.getStrValue("kafka.bootstrap.servers"));
        properties.setProperty("group.id", groupId);
        return properties;
    }
}
