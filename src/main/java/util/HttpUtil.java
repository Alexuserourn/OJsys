package util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class HttpUtil {
    public static String readBody(HttpServletRequest req) throws UnsupportedEncodingException {
        // 1. 先获取到 body 的长度并分配好对应的内存空间
        //    单位是 字节
        int contentLength = req.getContentLength();
        byte[] buf = new byte[contentLength];
        // 2. 根据 req 对象, 拿到读取 body 的 InputStream 对象
        try (InputStream inputStream = req.getInputStream()) {
            inputStream.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 把读到的 buf 给转换成一个 String 对象
        return new String(buf, "utf-8");
    }
}