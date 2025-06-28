package util;

import java.io.*;
import java.nio.charset.StandardCharsets;

// 使用这个类封装一下 Java 的文件操作.
// 让后面的代码能够更方便的读写整个文件.
public class FileUtil {
    // 从指定的文件中一次把所有的内容读出来，使用UTF-8编码
    public static String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            char[] buffer = new char[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    // 把 content 中的内容一次写入到 filePath 对应的文件中，使用UTF-8编码
    public static void writeFile(String filePath, String content) {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
