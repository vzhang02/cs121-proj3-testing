import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.Annotation;

public class PropertyMethods {
    private static List<Method> property = new ArrayList<>();
    private static Class<?> c;
    private static Object o;

    public PropertyMethods(String name) {
        try {
            c = Class.forName(name);
            o = c.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found");
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        Method[] meths = c.getMethods();
        processMethods(meths);
    }
    
    public static void processMethods(Method[] meths) {
        for (Method m : meths) {
            Annotation[] a = m.getAnnotations();
            if (a.length == 1) {
                if (a[0] instanceof Property) {
                    property.add(m);
                }
            }
        }
        Collections.sort(property, new MethodComparator());
    }
}
