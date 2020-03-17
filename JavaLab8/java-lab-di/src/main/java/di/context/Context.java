package di.context;

import javax.servlet.annotation.WebFilter;

public interface Context {
    <T> T getComponent(String name);
}
