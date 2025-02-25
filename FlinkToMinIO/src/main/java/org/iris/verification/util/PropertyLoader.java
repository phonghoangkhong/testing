package org.iris.verification.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = PropertyLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file cấu hình application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi load file cấu hình", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}