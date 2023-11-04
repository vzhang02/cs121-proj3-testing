import java.util.*;
import java.lang.reflect.*;
import java.util.stream.*;
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
    
    private static void processMethods(Method[] meths) {
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

    // public void executeMethods(Map<String, Object[]> results) {
        
    // }
    private static List<Object[]> getParams(Method m) {
        List<List<Object>> argList = new ArrayList<>(); 
        Parameter[] params = m.getParameters(); 
        for (Parameter p : params) {
            checkParam(p);
            List<Object> possibleArgs = generateArgs(p);


        }
        return new ArrayList<>();
    }

    private static void checkParam(Parameter p) {
        Class<?> c = p.getType();
        if ((c == Integer.class && p.getAnnotation(IntRange.class) != null) 
            || (c == String.class && p.getAnnotation(StringSet.class) != null)
            || (c == List.class && p.getAnnotation(ListLength.class) != null)
            || (c == Object.class && p.getAnnotation(ForAll.class) != null)) {
                throw new RuntimeException("Incompatible annotation for given class\n");
        }
    }

    private static List<Object> generateArgs(Parameter p) {
        if (p.getAnnotation(IntRange.class) != null) {
            IntRange i = p.getAnnotation(IntRange.class);
            return new ArrayList<Object>(IntStream.rangeClosed(i.min(), i.max()).boxed().toList()); 
        } else if (p.getAnnotation(StringSet.class) != null) {
            StringSet s = p.getAnnotation(StringSet.class);
            return new ArrayList<Object>(Arrays.asList(s.strings()));
        } else if (p.getAnnotation(ListLength.class) != null) {
            throw new RuntimeException("yikes list :(\n");
        } else {
            ForAll f = p.getAnnotation(ForAll.class);
            List<Object> l = new ArrayList<Object>(f.times());

            try {
                Method m = c.getMethod(f.name());
                for (int i = 0; i < f.times(); i++) {
                    l.add(m.invoke(o, (Object[])null));
                }
                return l;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Method not found\n");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Access not allowed\n");
            }  catch (InvocationTargetException e) {
                throw new RuntimeException("Method threw an exception\n");
            }
        }
    }
 }
