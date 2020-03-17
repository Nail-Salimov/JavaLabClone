package server.context;

public interface ApplicationContext {
    <T> T getComponent(String name);
}
