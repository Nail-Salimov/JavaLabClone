package server.propertysource;

public class PropertySource {

    private String propertyPath;

    public PropertySource(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }
}
