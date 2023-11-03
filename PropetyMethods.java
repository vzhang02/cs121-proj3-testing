import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.Annotation;

public class PropetyMethods {
    private static List<Method> property = new ArrayList<>();
    private static Object o;

    public static void processMethods(Method[] meths) {
        for (Method m : meths) {
            Annotation[] a = m.getAnnotations();
            if (a.length == 1) {
                if (a[0] instanceof Property) {
                    property.add(m);
                }
            }
        }
    }
}
