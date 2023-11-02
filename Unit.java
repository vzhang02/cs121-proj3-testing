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
        try {
            Class<?> c = Class.forName(name);
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found");
        }
        
    }

    /* Runs @Property methods */
    public static Map<String, Object[]> quickCheckClass(String name) {
	throw new UnsupportedOperationException();
    }

    /* Adds all methods to corresponding lists and sorts list */
    private static void processMethods(Method[] meths) {
        for (Method m : meths) {
            System.out.println(m);
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
        Collections.sort(tests, new MethComparator());
        Collections.sort(before, new MethComparator());
        Collections.sort(beforeClass, new MethComparator());
        Collections.sort(after, new MethComparator());
        Collections.sort(afterClass, new MethComparator());
    }

    /* executes all methods in correct order */
    private static void executeMethods(Map<String, Throwable> results) {
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