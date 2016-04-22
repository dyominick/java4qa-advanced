package design.demo;

public class ConsoleLoggerAppender implements LoggerAppender {
    @Override
    public void append(String message, int level) {
        System.out.println(message);
    }
}
