import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.Annotation;

public class TestMethods {
    private static List<Method> tests;
    private static List<Method> before;
    private static List<Method> beforeClass;
    private static List<Method> after;
    private static List<Method> afterClass;
    private static Object o;
    private static Class<?> c;
    

    private enum MethType {
        TESTS,
        BEFORE,
        BEFORECLASS,
        AFTER,
        AFTERCLASS
    }

    private class BadClassException extends RuntimeException {}

    public TestMethods(String name) {
       try {
            c = Class.forName(name);
            o = c.getConstructor().newInstance();
            tests = new ArrayList<>();
            before = new ArrayList<>();
            beforeClass = new ArrayList<>();
            after = new ArrayList<>();
            afterClass = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getCause());
            e.printStackTrace();
            throw new BadClassException();
        } catch (Exception e) {
            System.out.println(e.getCause());
            e.printStackTrace();
            throw new RuntimeException(); 
        }
        Method[] meths = c.getMethods();
        processMethods(meths);
    }
    /* Adds all methods to corresponding lists and sorts list */
    private static void processMethods(Method[] meths) {
        for (Method m : meths) {
            Annotation[] a = m.getAnnotations();
            int numAnnotations = annotationCounter(a);
            if (numAnnotations == 1) {
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
            } else if (numAnnotations > 1) { 
                throw new RuntimeException("Too many annotations");
            }
        }
        sort();
    }   

    private static int annotationCounter(Annotation[] as) {
        int counter = 0;
        for (Annotation a : as) {
            if (a instanceof Test
                || a instanceof Before
                || a instanceof After
                || a instanceof BeforeClass
                || a instanceof AfterClass) {
                    counter++;
                } 
        }
        return counter;
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
                runTestMethod(results, m);
                runMethods(results, MethType.AFTER);
            }
            return;
        }
        for (Method m : meths) {
            run(m);
        }
    }
    /* invokes the non-Test Methods */
    private static void run(Method m) {
        try {
            m.invoke(o);
        } catch (IllegalAccessException e) {
            System.out.println(e.getCause());
            throw new RuntimeException("access not allowed");
        } catch (Exception e) {
            System.out.print(e.getCause());
            System.out.println(" was encountered while running non-TestMethods");
        }
    }
    /* invokes the Test method and catches any exceptions */
    private static void runTestMethod(Map<String, Throwable> results, Method m) {
        try {
            m.invoke(o);
            results.put(m.getName(), null);
        } catch (Exception ex) {
            results.put(m.getName(), ex.getCause());
        }
    }
}
