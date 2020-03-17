package context;

public interface Context {
    <T> T getComponent(String name);
}
