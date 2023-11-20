import java.util.*;

public class TestQuickCheck {
    static final boolean DEBUG = false;
    static final boolean b = false;
    private Integer counter = 7;

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

    // @Property 
    // public boolean testFoo(@ForAll(name="genIntSet", times=10) Object o) {
    //     Set s = (Set) o;
    //     s.add("foo");
    //     return s.contains("foo");
    // }
        
    // int count = 0;
    // public Object genIntSet() {
    //     Set s = new HashSet();
    //     for (int i=0; i<count; i++) { s.add(i); }
    //     count++;
    //     return s;
    // }

    @Property
    public boolean absNonNeg(@IntRange(min=-10, max=10) Integer i) {
        return Math.abs(i.intValue()) >= 0; 
    } 

    @Property
    public boolean isS2(@StringSet(strings={"s1", "s2", "s3", "a1"}) String s, @IntRange(min=0, max=2) Integer i) {
        if(DEBUG) {
            System.out.println(s);
        }
        return true;
    }

    @Property
    public boolean listOfInts(@ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer> l) {
        if(DEBUG) {
            System.out.println(l.toString());
        }
        return true;
    }

    @Property
    public boolean listOfLists(@ListLength(min=0, max=2) List<@ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer>> l) {
        if(DEBUG) {
            System.out.println(l.toString());
        }
        return true;
    }

    @Property
    public boolean deepLists(@ListLength(min=0, max=2) List<@ListLength(min=0, max=2) List<@ListLength(min=0, max=2) List<@ListLength(min=0, max=2) List<@IntRange(min=5, max=5) Integer>>>> l) {
        if(DEBUG) {
            System.out.println(l.toString());
        }
        return true;
    }

    @Property
    public boolean objectGen(@ForAll(name="genSeven", times=10) Object i) {
        if(DEBUG) {
            System.out.println(i.toString());
        }

        if (i instanceof Integer) {
            Integer i_nt = (Integer)i;
            i_nt += 1;
        } 
        return true;
    }

    @Property
    public boolean intGen(@ForAll(name="genInt", times=10) Object i) {
        Integer in = (Integer)i;
        return in < 15;
    }

    public Object genSeven() {
        return 7;
    }

    public Object genInt() {
        counter++;
        return (Object)counter;
    }

}
