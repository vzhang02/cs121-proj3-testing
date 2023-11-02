public class AssertionInt {
    private Integer i; 
    public AssertionInt(int i) {
        this.i = i;
    }

    public AssertionInt isEqualTo(int i2) {
        if (!i.equals(i2)) {
            throw new RuntimeException("ints are not equal");
        }
        return this;
    }
    public AssertionInt isLessThan(int i2) {
        if (Integer.compare(Integer.valueOf(i), i2) >= 0) {
            throw new RuntimeException("1st int isn't < 2nd int");
        }
        return this;
    }

    public AssertionInt isGreaterThan(int i2) {
        if (Integer.compare(Integer.valueOf(i), i2) <= 0) {
            throw new RuntimeException("1st int isn't > 2nd int");
        }
        return this;
    }
}
