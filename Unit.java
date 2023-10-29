import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.Annotation;


public class Unit {
    private static List<Method> tests = new ArrayList<>();
    private static List<Method> before = new ArrayList<>();
    private static List<Method> beforeClass = new ArrayList<>();
    private static List<Method> after = new ArrayList<>();
    private static List<Method> afterClass = new ArrayList<>();

    private static Object o;

    /* Comparator class for methods */
    private static class MethComparator implements Comparator<Method> {
        public int compare(Method m1, Method m2) {
            return m1.getName().compareTo(m2.getName()); 
        }
    }

    private enum MethType {
        TESTS,
        BEFORE,
        BEFORECLASS,
        AFTER,
        AFTERCLASS
    }

    /* Runs all class test methods */
    public static Map<String, Throwable> testClass(String name) {
        Class<?> c = name.getClass();
        try {
            o = c.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(); 
        }
        Method[] meths = c.getMethods();
        Map<String, Throwable> results = new HashMap<>();
        processMethods(meths);
        executeMethods(results);
        return results;
    }

    /* Runs @Property methods */
    public static Map<String, Object[]> quickCheckClass(String name) {
	throw new UnsupportedOperationException();
    }

    /* Adds all methods to corresponding lists and sorts list */
    private static void processMethods(Method[] meths) {
        for (Method m : meths) {
            Annotation[] a = m.getAnnotations();
            if (a.length > 1) { throw new IllegalArgumentException(); }

            if (a[0] instanceof Test) {
                tests.add(m);
            } else if (a[0] instanceof Before) {
                before.add(m);
            } else if (a[0] instanceof After) {
                after.add(m);
            } else {
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
        sort();
    }   

    /* sorts all lists */
    private static void sort() {
        Collections.sort(tests, new MethComparator());
        Collections.sort(before, new MethComparator());
        Collections.sort(beforeClass, new MethComparator());
        Collections.sort(after, new MethComparator());
        Collections.sort(afterClass, new MethComparator());
    }

    private static void executeMethods(Map<String, Throwable> results) {
        runMethods(results, MethType.BEFORECLASS);
        runMethods(results, MethType.TESTS);
        runMethods(results, MethType.AFTERCLASS);
    }

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
                run(results, m);
                runMethods(results, MethType.AFTER);
            }
            meths = null;
        }
        for (Method m : meths) {
            run(results, m);
        }

    }
    private static void run(Map<String, Throwable> results, Method m) {
        try {
            m.invoke(o, (Object[])null);
        } catch (InvocationTargetException e) {
            results.put(m.getName(), e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError();
        }
    }

}