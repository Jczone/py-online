package com.wxj.py_online;

import com.wxj.util.TimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@SpringBootTest
class PyOnlineApplicationTests {

    @Test
    void pyTest() throws IOException {
       /* // 创建py文件
        OutputStream f = Files.newOutputStream(Paths.get("f:/Desktop/py_online/hello.py"));
        OutputStreamWriter writer = new OutputStreamWriter(f, StandardCharsets.UTF_8);
        writer.append("var1 = 'Hello World!'\n" +
                "var2 = \"Runoob\"\nprint(var1)\nprint(var2)");
        writer.close();
        f.close();
        // Java调用 dos命令
        String cmd = "python f:\\Desktop\\py_online\\hello.py";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            while (content != null) {
                System.out.println(content);
                content = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
