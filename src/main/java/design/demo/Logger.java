package design.demo;

public class Logger {
    private LoggerFilter loggerFilter = FilterFactory.create();
    private LoggerAppender loggerAppender;

    public Logger(LoggerAppender loggerAppender) {
        this.loggerAppender = loggerAppender;
    }

    public void log(String message, int level) {
        if (loggerFilter.filter(message, level)) {
            loggerAppender.append(message, level);
        }
    }
}

class LoggerTest {
    public static void main(String[] args) {
        new Logger(new ConsoleLoggerAppender()).log("message", 3);
    }
}
