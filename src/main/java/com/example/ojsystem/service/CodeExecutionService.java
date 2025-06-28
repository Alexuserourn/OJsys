package com.example.ojsystem.service;

import com.example.ojsystem.model.ExecutionResult;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * 代码执行服务类
 */
@Service
public class CodeExecutionService {

    private static final int TIMEOUT_SECONDS = 5;

    /**
     * 执行代码
     * @param code 代码字符串
     * @param args 执行参数
     * @return 执行结果
     */
    public ExecutionResult executeCode(String code, Object... args) {
        // 创建临时目录
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("oj_");
            String className = "Solution";
            Path javaFile = tempDir.resolve(className + ".java");
            
            // 写入代码到临时文件
            Files.write(javaFile, code.getBytes(StandardCharsets.UTF_8));
            
            // 编译代码
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            boolean compileResult = compile(javaFile, diagnostics);
            
            if (!compileResult) {
                StringBuilder errorMsg = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    errorMsg.append(diagnostic.getMessage(null)).append("\n");
                }
                return ExecutionResult.compileError(errorMsg.toString());
            }
            
            // 执行代码
            return runCode(tempDir, className, args);
            
        } catch (Exception e) {
            return ExecutionResult.runtimeError("代码执行异常: " + e.getMessage());
        } finally {
            // 清理临时文件
            if (tempDir != null) {
                try {
                    deleteDirectory(tempDir);
                } catch (IOException e) {
                    // 忽略
                }
            }
        }
    }
    
    /**
     * 编译Java代码
     */
    private boolean compile(Path javaFile, DiagnosticCollector<JavaFileObject> diagnostics) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(javaFile.toFile());
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            return task.call();
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * 运行编译后的代码
     */
    private ExecutionResult runCode(Path classPath, String className, Object[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ExecutionResult> future = executor.submit(() -> {
            try {
                // 创建类加载器
                URLClassLoader classLoader = new URLClassLoader(
                    new java.net.URL[] { classPath.toUri().toURL() },
                    this.getClass().getClassLoader()
                );
                
                // 加载类
                Class<?> solutionClass = classLoader.loadClass(className);
                Object instance = solutionClass.getDeclaredConstructor().newInstance();
                
                // 查找匹配的方法
                Method[] methods = solutionClass.getDeclaredMethods();
                if (methods.length == 0) {
                    return ExecutionResult.runtimeError("没有找到可执行的方法");
                }
                
                // 优先选择名为test的方法
                Method methodToInvoke = Arrays.stream(methods)
                    .filter(m -> m.getName().equals("test"))
                    .findFirst()
                    .orElse(methods[0]); // 如果没有test方法，选择第一个方法
                
                // 执行方法
                Object result = methodToInvoke.invoke(instance, args);
                
                // 关闭类加载器
                classLoader.close();
                
                return ExecutionResult.success(result);
                
            } catch (Exception e) {
                Throwable cause = e.getCause() != null ? e.getCause() : e;
                return ExecutionResult.runtimeError(cause.getClass().getName() + ": " + cause.getMessage());
            }
        });
        
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return ExecutionResult.timeoutError("代码执行超时 (>" + TIMEOUT_SECONDS + "秒)");
        } catch (Exception e) {
            return ExecutionResult.runtimeError("执行异常: " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }
    
    /**
     * 删除目录及其内容
     */
    private void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    /**
     * 自定义Java文件对象
     */
    private static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;
        
        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }
        
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
} 