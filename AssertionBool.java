public class AssertionBool {
    private Boolean b;
    public AssertionBool(boolean b) {
        this.b = b;
    }

    public AssertionBool isEqualTo(boolean b2) {
        if (!b.equals(b2)) {
            throw new RuntimeException("bools are not equal");
        }
        return this; 
    }

    public AssertionBool isTrue() {
        if (!b) {
            throw new RuntimeException("bool is false");
        }
        return this;
    }

    public AssertionBool isFalse() {
        if (b) {
            throw new RuntimeException("bool is true");
        }
        return this;
    }
}
