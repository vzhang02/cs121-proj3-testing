import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.Annotation;


public class Unit {

    /* Runs all class test methods */
    public static Map<String, Throwable> testClass(String name) {
        
        TestMethods t = new TestMethods(name);
        Map<String, Throwable> results = new HashMap<>();
        t.executeMethods(results);
        return results;
        
    }

    /* Runs @Property methods */
    public static Map<String, Object[]> quickCheckClass(String name) {
        try {
            Class<?> c = Class.forName(name);
            // try {
            //     o = c.getConstructor().newInstance();
            // } catch (Exception e) {
            //     throw new IllegalArgumentException(); 
            // }
            Method[] meths = c.getMethods();
            Map<String, Object[]> results = new HashMap<>();
            PropertyMethods.processMethods(meths);
            return results;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found");
        }
    }

}