package com.qlsv.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lớp đọc cấu hình kết nối CSDL từ file {@code config.properties}.
 * Thứ tự tìm file: thư mục chạy hiện tại -> classpath. Nếu không tìm thấy
 * sẽ dùng giá trị mặc định để ứng dụng vẫn chạy được.
 */
public final class ConfigManager {

    private static final Properties PROPS = new Properties();

    // Giá trị mặc định (dùng khi không có file cấu hình)
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/qlsv?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "root";

    static {
        load();
    }

    private ConfigManager() {
        // Lớp tiện ích - không cho khởi tạo
    }

    /** Nạp file cấu hình (ưu tiên file trong thư mục chạy, sau đó tới classpath). */
    private static void load() {
        // 1) Thử đọc config.properties trong thư mục chạy
        try (InputStream in = new FileInputStream("config.properties")) {
            PROPS.load(in);
            return;
        } catch (IOException ignore) {
            // Không có file ngoài -> thử classpath
        }
        // 2) Thử đọc từ classpath
        try (InputStream in = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException ignore) {
            // Dùng mặc định
        }
    }

    public static String getDbUrl() {
        return PROPS.getProperty("db.url", DEFAULT_URL);
    }

    public static String getDbUser() {
        return PROPS.getProperty("db.user", DEFAULT_USER);
    }

    public static String getDbPassword() {
        return PROPS.getProperty("db.password", DEFAULT_PASS);
    }
}
