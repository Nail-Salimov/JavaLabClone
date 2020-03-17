package server.context;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import server.dispatcher.RequestDispatcher;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class ApplicationContextReflectionBased implements ApplicationContext {
    private List<Class<?>> neededClasses;
    private Map<Class<?>, Typer> instance;
    private String packageName;

    public ApplicationContextReflectionBased(String packageName) {
        this.packageName = packageName;
        try {
            ScanResult scanResult = new ClassGraph()
                    .enableAllInfo()
                    .whitelistPackages(packageName)
                    .scan();

            String componentName = Component.class.getName();
            ClassInfoList classes = scanResult.getClassesImplementing(componentName);
            neededClasses = classes.loadClasses();

            instance = new HashMap<>();
            for (Class<?> thisClass : neededClasses) {
                instance.put(thisClass, new Typer(thisClass.newInstance()));
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException(e);
        }

    }

    public void buildProject() {
        try {
            for (Class<?> thisClass : neededClasses) {
                Method[] methods = thisClass.getMethods();
                List<Method> setters = new LinkedList<>();

                for (Method method : methods) {
                    if (method.getName().startsWith("set")) {
                        setters.add(method);
                    }
                }

                for (Method method : setters) {
                    Object thisObject = null;
                    for (Map.Entry<Class<?>, Typer> entry : instance.entrySet()) {
                        if (thisClass.equals(entry.getKey())) {
                            thisObject = entry.getValue().getInstance();
                        }
                    }

                    Class<?> typeClass = method.getParameters()[0].getType();

                    for (Map.Entry<Class<?>, Typer> entry : instance.entrySet()) {
                        if (entry.getKey().getInterfaces().length > 0) {
                            if ((entry.getKey().equals(typeClass)) || (entry.getKey().getInterfaces()[0].equals(typeClass))) {
                                method.invoke(thisObject, entry.getValue().getInstance());
                            }
                        } else {
                            if ((entry.getKey().equals(typeClass))) {
                                method.invoke(entry.getValue().getInstance());
                            }
                        }
                    }
                }
            }

            RequestDispatcher requestDispatcher = null;
            for (Map.Entry<Class<?>, Typer> entry : instance.entrySet()) {
                if (entry.getKey().getName().equals("server.dispatcher.RequestDispatcher")) {
                    requestDispatcher = (RequestDispatcher) entry.getValue().getInstance();
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public void addConnection(String propertyPath) {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(propertyPath));
            String name = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            String url = properties.getProperty("db.url");
            Connection connection = DriverManager.getConnection(url, name, password);
            instance.put(Connection.class, new Typer(connection));
        } catch (SQLException | IOException e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public <T> T getComponent(String name) {
        T component = null;
        for (Map.Entry<Class<?>, Typer> entry : instance.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                component = (T)entry.getValue().getInstance();
                break;
            }
        }
        return component;
    }
}
