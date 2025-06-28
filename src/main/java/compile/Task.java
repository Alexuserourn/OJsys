package compile;

import util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

// 这个类表示一个完整的编译运行的过程
public class Task {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);
    
    // 此处罗列出需要的临时文件(用于进程间通信)文件名约定
    // 所有的临时文件要放到这个目录中
    private String WORK_DIR;
    // 要编译执行的类的名字, 影响到源代码的文件名
    private String CLASS = "Solution";
    // 要编译执行的文件名
    private String CODE;
    // 程序标准输出放置的文件
    private String STDOUT;
    // 程序标准错误放置的文件
    private String STDERR;
    // 程序编译出错的详细信息放置的文件
    private String COMPILE_ERROR;

    public Task() {
        // 先生成唯一的id, 根据这个 id 来拼装出目录的名字
        WORK_DIR = "./tmp/" + UUID.randomUUID().toString() + "/";
        // 然后再生成后续的这些文件名
        CODE = WORK_DIR + "Test.java"; // 修改为Test.java，因为我们使用的主类是Test
        STDOUT = WORK_DIR + "stdout.txt";
        STDERR = WORK_DIR + "stderr.txt";
        COMPILE_ERROR = WORK_DIR + "compile_error.txt";
    }

    // Question 表示用户提交的代码
    // Answer 表示代码的编译运行结果
    public Answer compileAndRun(Question question) throws IOException, InterruptedException {
        Answer answer = new Answer();
        // 0. 给这些临时文件准备好一个目录
        //    判定 WORD_DIR 是否存在. 如果存在就跳过. 不存在就创建目录
        File file = new File(WORK_DIR);
        if (!file.exists()) {
            // 创建对应的目录
            file.mkdirs();
        }
        // 1. 先要准备好需要用到的临时文件
        //    要编译的源代码的文件(首先搞这个)
        //    编译出错要放进一个文件
        //    最终运行的标准输出标准错误也要分别放到文件中
        
        // 检查代码中是否包含public class名称
        String publicClassName = getPublicClassName(question.getCode());
        if (publicClassName != null && !publicClassName.isEmpty()) {
            // 如果找到了public class，使用它作为主类文件名
            logger.info("检测到public类: {}", publicClassName);
            CODE = WORK_DIR + publicClassName + ".java";
        } else {
            logger.info("未检测到public类，使用默认文件名Test.java");
        }
        
        logger.info("写入代码到文件: {}", CODE);
        FileUtil.writeFile(CODE, question.getCode());

        // 2. 构造编译指令(javac), 并进行执行. 预期得到的结果
        //    就是一个对应的 .class 文件, 以及编译出错的文件
        //    -d 表示 生成的.class文件放置的位置
        //    javac -encoding utf-8 ./tmp/Solution.java -d ./tmp/
        // 确保javac命令使用UTF-8作为源码编码和错误消息编码
        String compileCmd = String.format("javac -encoding utf-8 -J-Dfile.encoding=UTF-8 %s -d %s",
                CODE, WORK_DIR);
        logger.info("编译命令: {}", compileCmd);
        // 创建子进程进行编译, 此处不关心 javac 的标准输出(没输出啥内容)
        // 只关心 javac 的标准错误. 标准错误中就包含了编译出错的信息
        CommandUtil.run(compileCmd, null, COMPILE_ERROR);
        // 此处判定一下编译是否出错. 看一下 COMPILE_ERROR 这个文件是不是空着就知道了
        String compileError = FileUtil.readFile(COMPILE_ERROR);
        if (!compileError.equals("")) {
            // 编译错误的文件不为空, 说明编译出错了
            logger.error("编译出错:\n{}", compileError);
            answer.setErrno(1);// 将退出码设置成1.
            // 确保编译错误信息使用UTF-8编码
            answer.setReason(compileError);
            return answer;
        }

        // 3. 构造运行指令(java), 并进行执行. 预期得到的结果
        //    就是这个代码的标准输出的文件和标准错误的文件
        //    为了让 java 命令找到 .class 文件的位置, 还需要加上一个选项
        //    -classpath 通过这个选项来执行 .class 文件放到哪个目录里了
        //    java Solution
        // 确保java运行命令使用UTF-8作为默认编码
        
        // 确定要运行的主类名
        String mainClass = publicClassName != null ? publicClassName : "Test";
        String runCmd = String.format("java -Dfile.encoding=UTF-8 -classpath %s %s", WORK_DIR, mainClass);
        logger.info("运行命令: {}", runCmd);
        CommandUtil.run(runCmd, STDOUT, STDERR);
        // 尝试读取 STDERR 这个文件里的内容, 如果不为空, 就认为是运行出错
        // 如果程序抛出异常, 异常的调用栈信息就是通过 stderr 来输出的
        String runError = FileUtil.readFile(STDERR);
        if (!runError.equals("")) {
            // 运行出错
            logger.error("运行出错:\n{}", runError);
            answer.setErrno(2);// 将退出码设置成2
            answer.setReason(runError);
            return answer;
        }
        
        // 4. 把最终结果构造成 Answer 对象, 并返回
        answer.setErrno(0);
        String runStdout = FileUtil.readFile(STDOUT);
        logger.info("运行成功，输出:\n{}", runStdout);
        answer.setStdout(runStdout);
        return answer;
    }
    
    /**
     * 从代码中提取public class的名称
     * @param code 代码字符串
     * @return public class的名称，如果没有找到则返回null
     */
    private String getPublicClassName(String code) {
        // 使用正则表达式查找public class名称
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("public\\s+class\\s+(\\w+)");
        java.util.regex.Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Task task = new Task();
        Question question = new Question();
        question.setCode("public class Solution {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"中文输出测试\");\n" +
                "        String s = null;\n" +
                "        System.out.println(s.length());\n" +
                "    }\n" +
                "}\n");
        Answer answer = task.compileAndRun(question);
        System.out.println(answer);
    }
}
