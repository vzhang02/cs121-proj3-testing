public class AssertionObject {
    private Object o;
    public AssertionObject(Object o) {
        this.o = o;
    }

    public AssertionObject isNotNull() {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        return this;
    }

    public AssertionObject isNull() {
        if (o != null) {
            throw new RuntimeException("o is not null");
        }
        return this;
    }

    public AssertionObject isEqualTo(Object o2) {
        if (!o.equals(o2)) {
            throw new RuntimeException("these objects are not equal");
        }
        return this;
    }

    public AssertionObject isNotEqualTo(Object o2) {
        if (o.equals(o2)) {
            throw new RuntimeException("these objects are equal");
        }
        return this;
    }

    public AssertionObject isInstanceOf(Class<?> c) {
        if (!c.isInstance(o)) {
            throw new RuntimeException("this object is not an instance of the provided class");
        }
        return this;
    }

}
