import java.util.*; 

public class MathTest {
    
    public MathTest() {} // empty constructor 

    @Property
    public boolean absNonNeg(@IntRange(min=-10, max=10) Integer i) {
        return Math.abs(i.intValue()) >= 0;
    }

    @Property
    public boolean stringSetMethod(@StringSet(strings={"0", "1", "2"}) String s) {
        return Integer.valueOf(s) >= 0;  
    }

    @Property
    public boolean listLengthMethodIntRange(@ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer> intList) {
        System.out.println(intList);
        return intList.size() >= 0;  
    }

    @Property
    public boolean listLengthMethodStringSet(@ListLength(min=0, max=2) List<@StringSet(strings={"a", "b", "c"}) String> strList) {
        return strList.size() >= 0;  
    }

    @Property
    public boolean listLengthMethodFailIntRange(@IntRange(min=0, max=2) Integer i) {
        return (i + i) == (i * 3); 
    }

    @Property
    public boolean emptyListPermTest(@ListLength(min=0, max=0) List<@IntRange(min=0, max=0) Integer> intList) {
        return false; 
    }

    // @Property 
    // public boolean testFoo(@ForAll(name="genIntSet", times=10) Object o) {
    //     Set s = (Set) o;
    //     s.add("ur mom");
    //     return s.contains("foo");
    // }
    
    // public Object genIntSet() {
    //     int count = 0;
    //     Set s = new HashSet();
    //     for (int i=0; i<count; i++) { s.add(i); }
    //     count++;
    //     return s;
    // }

    @Property
    public Boolean multiParamCheck(@IntRange(min=2, max=4) Integer i, @StringSet(strings={"a","b"}) String str) {
        return ("" + String.valueOf(i) + str).length() > 0; 
    }

    @Property
    public Boolean multiParamCheckFail(@IntRange(min=2, max=4) Integer i, @StringSet(strings={"a","b"}) String str) {
        System.out.println("trying " + i + " and " + str);
        return ("" + String.valueOf(i) + str).length() < 0; 
    }

    @Test
    public void dummyFunc() {
        System.out.println("dummy function called"); 
    }
}
