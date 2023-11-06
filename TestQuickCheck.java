import java.util.*;

public class TestQuickCheck {
    private boolean b = false;

    @Property
    public boolean absNonNeg(@IntRange(min=-10, max=10) Integer i) {
        return Math.abs(i.intValue()) >= 0;
    }

    @Property
    public boolean strings(@StringSet(strings={"s1", "s2", "s3", "s4"}) String s) {
        if (b) {
            System.out.println("ruh roh");
            System.out.println(s);
        }
        return true;
    }

    @Property
    public boolean intList(@ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer> l) {
        if (b) {
            System.out.println("ruh roh");
            System.out.println(l.toString());
        }
        return true;
    }

    @Property
    public boolean listOfList(@ListLength(min=0, max=2) List<@ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer>> l) {
        if (b) {
            System.out.println("ruh roh");
            System.out.println(l.toString());
        }
        return true;
    }

    @Property
    public boolean tooManyLists(@ListLength(min=0, max=2) List<@ListLength(min=0, max=2) List<@ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer>>> l) {
        if (b) {
            System.out.println("ruh roh");
            System.out.println(l.toString());
        }
        return true;
    }

    @Property 
    public boolean testFoo(@ForAll(name="genIntSet", times=10) Object o) {
        Set s = (Set) o;
        s.add("foo");
        return s.contains("foo");
    }
        
    int count = 0;
    public Object genIntSet() {
        Set s = new HashSet();
        for (int i=0; i<count; i++) { s.add(i); }
        count++;
        return s;
    }

}
