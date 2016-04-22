package design.demo;

public class LeveledLoggerFilter implements LoggerFilter {
    @Override
    public boolean filter(String message, int level) {
        return level > 1;
    }
}
