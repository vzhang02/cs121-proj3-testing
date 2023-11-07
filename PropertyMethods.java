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
            property = new ArrayList<>();
        } catch (Exception e) {
            System.err.println(e.getCause());
            e.printStackTrace();
            throw new RuntimeException(); 
        }
        Method[] meths = c.getMethods();
        processMethods(meths);
    }
    
    private static void processMethods(Method[] meths) {
        for (Method m : meths) {
            Annotation a = m.getAnnotation(Property.class);
            if (a != null) {
                property.add(m);
            }
        }
        Collections.sort(property, new MethodComparator());
    }

    public void executeMethods(Map<String, Object[]> results) {
        for (Method m : property) {
            List<Object[]> params = getParams(m); 
            results.put(m.getName(), run(params, m));
        }
    }

    private static Object[] run(List<Object[]> params, Method m) {
        int numTimes = params.size() < 100 ? params.size() : 100;
        for (int i = 0; i < numTimes; i++) {
            Object[] args = params.get(i);
            try {
                Object res = m.invoke(o, args);
                if (res instanceof Boolean && !(Boolean)res) {
                    return args;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Access not allowed for this method");
            } catch (Throwable e) {
                System.out.println(e.getCause());
                e.printStackTrace();
                return args;
            }
        }
        return null;
    }

    private static List<Object[]> getParams(Method m) {
        Parameter[] params = m.getParameters(); 
        List<List<Object>> argsLists = new ArrayList<>();
        for (Parameter p : params) {
            checkParam(p);
            argsLists.add(generateArgs(p.getAnnotatedType()));
        }
        int numArgs = argsLists.size();
        List<Object[]> possibleArguments = new ArrayList<>(numArgs);
        findArgPermutations(possibleArguments, argsLists, new Object[numArgs], 0, 0, numArgs);
        return possibleArguments;
    }

    private static void checkParam(Parameter p) {
        Class<?> c = p.getType();
        if (!((c == Integer.class && p.getAnnotation(IntRange.class) != null) 
            || (c == String.class && p.getAnnotation(StringSet.class) != null)
            || (c == List.class && p.getAnnotation(ListLength.class) != null)
            || (c == Object.class && p.getAnnotation(ForAll.class) != null))) {
                throw new RuntimeException("Incompatible annotation for given class\n");
        }
    }

    private static List<Object> generateArgs(AnnotatedType p) {
        if (p.getAnnotation(IntRange.class) != null) {
            IntRange i = p.getAnnotation(IntRange.class);
            return new ArrayList<Object>(IntStream.rangeClosed(i.min(), i.max()).boxed().toList()); 
        } else if (p.getAnnotation(StringSet.class) != null) {
            StringSet s = p.getAnnotation(StringSet.class);
            return new ArrayList<Object>(Arrays.asList(s.strings()));
        } else if (p.getAnnotation(ListLength.class) != null) {
            return listArgs(p);
        } else {
            return objArgs(p);
        }
    }

    public static List<Object> listArgs(AnnotatedType p) {
        ListLength l = p.getAnnotation(ListLength.class);
        List<List<Object>> args = new ArrayList<>();
        List<Object> possibleElem = generateArgs(((AnnotatedParameterizedType)p).getAnnotatedActualTypeArguments()[0]);
        findListPermutations(args, new ArrayList<>(), possibleElem, l.min(), l.max());
        return new ArrayList<Object>(args);
    }
    
    private static void findListPermutations(List<List<Object>> args, List<Object> curr, List<Object> elems, int min, int max) {
        
        if (curr.size() == max) {
            if (! args.contains(curr)) {
                args.add(new ArrayList<>(curr));
            }
        } else {
            if (min == 0 && curr.size() == 0) {
                args.add(curr);
            }
            for (int i = 0 ; i < elems.size() ; i++) { 
                curr.add(elems.get(i)); 
                if (curr.size() >= min) {
                    args.add(new ArrayList<>(curr));
                }
                findListPermutations(args, curr, elems, min, max);
                curr.remove(curr.size() - 1);
            }
        }
    }

    private static List<Object> objArgs(AnnotatedType p) {
        ForAll f = p.getAnnotation(ForAll.class);
        List<Object> args = new ArrayList<Object>(f.times());

        try {
            Method m = c.getMethod(f.name());
            for (int i = 0; i < f.times(); i++) {
                args.add(m.invoke(o, (Object[])null));
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
            e.printStackTrace();
            throw new RuntimeException();
        } 
        return args;
    }

    private static void findArgPermutations(List<Object[]> argPerms, List<List<Object>> argsLists,
                                            Object[] curr, int pos, int argNum, int max) {
        if (pos == max) {
            Object[] deepCopy = new Object[max];
            int count = 0;
            for (Object o : curr) {
                deepCopy[count] = o;
                count++;
            }
            argPerms.add(deepCopy);
            return;
        } 
        for (int i = 0; i < argsLists.get(argNum).size(); i++) {
            List<Object> l = argsLists.get(argNum);
            curr[pos] = l.get(i);
            findArgPermutations(argPerms, argsLists, curr, pos+1, argNum+1, max);
            curr[pos] = null;
        }
    } 
}
