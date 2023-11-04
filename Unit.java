import java.util.*;


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
        PropertyMethods p = new PropertyMethods(name);
        Map<String, Object[]> results = new HashMap<>();
       // p.executeMethods(results);
        return results;
    }

}