package design.demo;

public class FilterFactory {
    public static LoggerFilter create() {
        return new LeveledLoggerFilter();
    }
}
