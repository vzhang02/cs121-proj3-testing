public class AssertionString {
    private String s;
    public AssertionString(String s) {
        this.s = s;
    }

    public AssertionString isNotNull() {
        if (s == null) {
            throw new NullPointerException("o is null");
        }
        return this;
    }

    public AssertionString isNull() {
        if (s != null) {
            throw new RuntimeException("o is not null");
        }
        return this;
    }

    public AssertionString isEqualTo(Object o2) {
        if (!s.equals(o2)) {
            throw new RuntimeException("these objects are not equal");
        }
        return this;
    }

    public AssertionString isNotEqualTo(Object o2) {
        if (s.equals(o2)) {
            throw new RuntimeException("these objects are equal");
        }
        return this;
    }

    public AssertionString isInstanceOf(Class<?> c) {
        if (!c.isInstance(s)) {
            throw new RuntimeException("this object is not an instance of the provided class");
        }
        return this;
    }

    public AssertionString startsWith(String s2) {
        if (!s.startsWith(s2)) {
            throw new RuntimeException("this string does not start with specified string");
        }
        return this;
    }

    public AssertionString isEmpty() {
        if (!s.isEmpty()) {
            throw new RuntimeException("string is not empty");
        }
        return this;
    }

    public AssertionString contains(String s2) {
        if (!s.contains(s2)) {
            throw new RuntimeException("the specified string was not found");
        }
        return this;
    }

}
