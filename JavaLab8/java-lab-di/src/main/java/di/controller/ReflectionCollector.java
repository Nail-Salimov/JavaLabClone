package di.controller;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionCollector {

    private String packageName;
    private Map<Set<String>, Controller> controllerMap;
    private List<Class<?>> controllerList;

    public ReflectionCollector(String packageName) {
        this.packageName = packageName;
        controllerMap = new HashMap<>();
        controllerList = new LinkedList<>();
    }

    public ControllerKit  collect() {

        try {
            ScanResult scanResult = new ClassGraph()
                    .enableAllInfo()
                    .whitelistPackages(packageName)
                    .scan();

            String controllerClassName = Controller.class.getName();
            ClassInfoList classes = scanResult.getClassesImplementing(controllerClassName);
            controllerList = classes.loadClasses();


            for (Class<?> c: controllerList) {
                Mapping mapping = c.getAnnotation(Mapping.class);

                Pattern pattern = Pattern.compile("^/\\w+");

                TreeSet<String> set = new TreeSet<String>();
                for (String str: mapping.value()) {
                    Matcher matcher = pattern.matcher(str);
                    if(matcher.find()){
                        set.add(str);
                    }
                }
                controllerMap.put(set, (Controller) c.newInstance());
            }

            return new ControllerKit(controllerMap);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }
}
