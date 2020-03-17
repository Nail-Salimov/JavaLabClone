package di.controller;

import java.util.Map;
import java.util.Set;

public class ControllerKit {
    private Map<Set<String>, Controller> controllerMap;

    public ControllerKit(Map<Set<String>, Controller> controllerMap) {
        this.controllerMap = controllerMap;
    }

    public Controller getController(String url) {
        for (Map.Entry<Set<String>, Controller> entry : controllerMap.entrySet()) {
            if(entry.getKey().contains(url)){
                return entry.getValue();
            }
        }
        return null;
    }

    public Map<Set<String>, Controller> getControllerMap() {
        return controllerMap;
    }

    public void setControllerMap(Map<Set<String>, Controller> controllerMap) {
        this.controllerMap = controllerMap;
    }
}
