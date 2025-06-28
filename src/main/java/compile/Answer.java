package compile;

// 编译运行的结果，里边是最终反馈给用户信息的。
public class Answer {
    // 0 表示编译运行都成功. 1 表示编译出错. 2 表示运行抛异常
    private int errno;// 表示错误码，错误的话包含编译错误和运行错误
    // errno 1 , reason 包含了编译错误的信息
    // errno 2 , reason 包含了异常的调用栈信息
    // 出错原因。
    private String reason;
    // 程序的标准输出
    private String stdout;
    // 程序的标准错误
    private String stderr;

    // get和set一下。
    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "errno=" + errno +
                ", reason='" + reason + '\'' +
                ", stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                '}';
    }
}
