public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
    static AssertionObject assertThat(Object o) {
        return new AssertionObject(o);
    }
    static AssertionString assertThat(String s) {
	    return new AssertionString(s);
    }
    static AssertionBool assertThat(boolean b) {
	    return new AssertionBool(b);
    }
    static AssertionInt assertThat(int i) {
	    return new AssertionInt(i);
    }
}