package server.context;

public class Typer {
    Object instance;

    public Typer(Object instance) {
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
