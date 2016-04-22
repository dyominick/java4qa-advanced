package design.demo;

public interface LoggerFilter {
    boolean filter(String message, int level);
}
