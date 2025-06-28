package compile;

import java.io.*;
import java.nio.charset.StandardCharsets;

// 为了能执行javac编译命令，和java运行指令。
public class CommandUtil {
    // run 方法，要执行一个命令就要创建一个进程来运行一个命令。
    // 要进行编译和运行，来确定这个类中的参数，包含3个参数。
    // 1.要进行编译的命令
    // 2.要是编译和运行都成功，则是标准输出的内容，放在一个文件中，用stdoutFile来表示。
    // 3.要是编译出错或者运行出错，则表示的是标准错误，放在stderrFile文件中。
    // Run方法用于进行创建进程并执行编译和运行命令。
    // cmd 表示要执行的命令, 比如 javac
    // stdoutFile 指定标准输出写到哪个文件中
    // stderrFile 执行标准错误写到哪个文件中
    // 进程的返回值用0来表示进程结束，-1表示的是进程没有结束，或者进程异常。
    public static int run(String cmd, String stdoutFile,
                          String stderrFile) throws IOException, InterruptedException {
        // 进行多进程操作。
        // 用 Runtime 这样的类表示进程的创建。Runtime这个类在创建的时候，不用手动去创建。
        // 里边的实例只有一个，这就是设计模式中的"单例模式"，直接调用里边的.getRunTime().exec()方法即可。
        // 下边表示创建一个进程，用process表示。
        Process process = Runtime.getRuntime().exec(cmd);
        // 当新的进程跑起来之后, 就需要获取到新进程的输出结果，结果无非有以下几种。
        // 1.要是标准输出中有内容，也就是下边的文件的内容不是空的，给标准输出进行重定向。
        if (stdoutFile != null) {
            // getInputStream 得到的是标准输出，得到里边的内容，为了后边进行重定向。
            // 使用InputStreamReader包装，并指定UTF-8编码
            try (InputStreamReader stdoutReader = new InputStreamReader(
                    process.getInputStream(), StandardCharsets.UTF_8);
            // 可以理解为先弄一个存放标准输出的箱子，将上边的标准输出的内容重定向在这个箱子里。
                 // 使用OutputStreamWriter包装，并指定UTF-8编码
                 OutputStreamWriter stdoutWriter = new OutputStreamWriter(
                         new FileOutputStream(stdoutFile), StandardCharsets.UTF_8)) {
                
                // 使用缓冲区读取数据
                char[] buffer = new char[1024];
                int len;
                while ((len = stdoutReader.read(buffer)) != -1) {
                    stdoutWriter.write(buffer, 0, len);
                }
            }
        }

        // 同上，再针对标准错误进行重定向
        // 要是标准错误文件中有内容，就将输出的内容进行重定向。
        if (stderrFile != null) {
            // getErrorStream 得到的是标准错误，使用InputStreamReader包装并指定UTF-8编码
            try (InputStreamReader stderrReader = new InputStreamReader(
                    process.getErrorStream(), StandardCharsets.UTF_8);
                 // 表示的弄了一个箱子，用来存放标准错误中的内容，使用OutputStreamWriter并指定UTF-8编码
                 OutputStreamWriter stderrWriter = new OutputStreamWriter(
                         new FileOutputStream(stderrFile), StandardCharsets.UTF_8)) {
                
                // 使用缓冲区读取数据
                char[] buffer = new char[1024];
                int len;
                while ((len = stderrReader.read(buffer)) != -1) {
                    stderrWriter.write(buffer, 0, len);
                }
            }
        }
        // 等待新的进程的结束，在结束退出码。就是等待刚创建的run进程的结束才获取退出码的
        int exitCode = process.waitFor();// 等待一下，等新的进程运行结束
        return exitCode;
    }

    public static void main(String[] args) {
        // 下边来进行调用。
        try {
            // run方法的返回值是int型的，0表示进程成功结束，-1表示的是错误。
            int ret = CommandUtil.run("javac", "./stdout.txt", "./stderr.txt");
            System.out.println(ret);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
