import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.Annotation;

public class TestMethods {
    private static List<Method> tests = new ArrayList<>();
    private static List<Method> before = new ArrayList<>();
    private static List<Method> beforeClass = new ArrayList<>();
    private static List<Method> after = new ArrayList<>();
    private static List<Method> afterClass = new ArrayList<>();
    private static Object o;
    private static Class<?> c;
    

    private enum MethType {
        TESTS,
        BEFORE,
        BEFORECLASS,
        AFTER,
        AFTERCLASS
    }

    public TestMethods(String name) {
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
    /* Adds all methods to corresponding lists and sorts list */
    private static void processMethods(Method[] meths) {
        for (Method m : meths) {
            Annotation[] a = m.getAnnotations();
            if (a.length > 1) { 
                throw new IllegalArgumentException(); }
            else if (a.length == 1) { 
                if (a[0] instanceof Test) {
                    tests.add(m);
                } else if (a[0] instanceof Before) {
                    before.add(m);
                } else if (a[0] instanceof After) {
                    after.add(m);
                } else if (a[0] instanceof BeforeClass || a[0] instanceof AfterClass) {
                    if (!Modifier.isStatic(m.getModifiers())) {
                        throw new IllegalArgumentException("Before_Class or After_Class method not static\n");
                    }
                    
                    if (a[0] instanceof AfterClass) {
                        afterClass.add(m);
                    } else {
                        beforeClass.add(m);
                    }
                }
            }
        }
        sort();
    }   

    /* sorts all lists */
    private static void sort() {
        MethodComparator m = new MethodComparator();
        Collections.sort(tests, m);
        Collections.sort(before, m);
        Collections.sort(beforeClass, m);
        Collections.sort(after, m);
        Collections.sort(afterClass, m);
    }

    /* executes all methods in correct order */
    public void executeMethods(Map<String, Throwable> results) {
        runMethods(results, MethType.BEFORECLASS);
        runMethods(results, MethType.TESTS);
        runMethods(results, MethType.AFTERCLASS);
    }

    /* runs the specified methods */
    private static void runMethods(Map<String, Throwable> results, MethType mt) {
        List<Method> meths;
        if (mt == MethType.BEFORECLASS) {
            meths = beforeClass;
        } else if (mt == MethType.BEFORE) {
            meths = before;
        } else if (mt == MethType.AFTERCLASS) {
            meths = afterClass;
        } else if (mt == MethType.AFTER) {
            meths = after;
        } else {
            for (Method m : tests) {
                runMethods(results, MethType.BEFORE);
                run(results, m, true);
                runMethods(results, MethType.AFTER);
            }
            return;
        }
        for (Method m : meths) {
            run(results, m, false);
        }

    }

    /* invokes the method and catches any exceptions */
    private static void run(Map<String, Throwable> results, Method m, boolean test) {
        try {
            m.invoke(o, (Object[])null);
            if (test) {
                results.put(m.getName(), null);
            }
        } catch (InvocationTargetException e) {
            if (test) {
                results.put(m.getName(), e.getCause());
            } else {
                throw new RuntimeException(e.getCause());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError();
        }
    }
}
