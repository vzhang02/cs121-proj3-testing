public class TestObj {
    private String id = "testObj";

    public TestObj() {
        System.out.println("TestObj:TestObj");
    }

    public void setID(String newID) {
        this.id = newID;
    }

    public String getID() {
        return this.id;
    }
    static final boolean DEBUG = true;

    @BeforeClass
    public static void beforeClass1() {
        if (DEBUG) {
            System.err.println("beforeClass1");
        }
    }

    @AfterClass
    public static void afterClass1() {
        if (DEBUG) {
            System.err.println("afterClass1");
        }
    }

    @Test
    public static void another_test() {
        if (DEBUG) { 
            System.err.println("another_test");
        }
        throw new RuntimeException("hey this was an intentional failure");
    }

    @Test
    public static void assertingObjectAffirmativeTest() {
        Character c = 'l';

        Assertion.assertThat((Object)c).isEqualTo('l').isNotNull().isInstanceOf(Character.class);

        Assertion.assertThat((Object)null).isNull();
    }

    @Test
    public static void assertingObjectNegativeTest() {
        Character c = 'z';

        Assertion.assertThat((Object)c).isEqualTo('a');
    }

    @Test
    public static void assertingStringAffirmativeTest() {
        String s = "hello";
        Assertion.assertThat(s).isNotNull().isEqualTo("hello").isNotEqualTo("hello!").contains("lo").startsWith("hell");
        Assertion.assertThat(null).isNull();
    }

    @Test
    public static void assertingStringNegativeTest() {
        String s = "hello";
        Assertion.assertThat(s).isNotNull().contains("ol");
    }

    @Test
    public static void test1() {
        Assertion.assertThat(true).isFalse();
    }

    @Test
    public static void test2() {
        assert false;
    }

    @Test
    public static void assertingBooleanAffirmativeTest() {
        boolean b = true;
        Assertion.assertThat(b).isEqualTo(true);
        Assertion.assertThat(false).isEqualTo(false);
        Assertion.assertThat(b).isTrue();
    }

    @Test
    public static void assertingIntegerAffirmativeTest() {
        int i = 5;
        Assertion.assertThat(i).isEqualTo(5).isGreaterThan(0).isLessThan(10).isGreaterThan(-100);
    }

    @Test
    public static void assertingIntegerNegativeTest() {
        int i = 5;
        Assertion.assertThat(i).isEqualTo(5).isGreaterThan(0).isLessThan(10).isGreaterThan(100);
    }

    @Test
    public void test_string_assertion() {
        Assertion.assertThat("testString").isNotNull();
        Assertion.assertThat("testString").isEqualTo("testString");
        Assertion.assertThat("testString").isNotEqualTo(new Object());
        Assertion.assertThat("testString").startsWith("test");
        Assertion.assertThat("").isEmpty();
        Assertion.assertThat("testString").contains("Str");
    }

    @Test
    public void test_string_assertion_exception_1() {
        Assertion.assertThat("testString").isNull();
    }

    @Test
    public void test_string_assertion_exception_2() {
        Assertion.assertThat("testString").isEqualTo("");
    }

    @Test
    public void test_string_assertion_exception_3() {
        Assertion.assertThat("testString").isNotEqualTo("testString");
    }

    @Test
    public void test_string_assertion_exception_4() {
        Assertion.assertThat("testString").startsWith("a");
    }

    @Test
    public void test_string_assertion_exception_5() {
        Assertion.assertThat("testString").isEmpty();
    }

    @Test
    public void test_string_assertion_exception_6() {
        Assertion.assertThat("testString").contains("a");
    }

    @Test
    public void test_boolean_assertion() {
        Assertion.assertThat(false).isFalse();
        Assertion.assertThat(true).isTrue();
        Assertion.assertThat(false).isEqualTo(false);
    }

    @Test
    public void test_boolean_assertion_exception_1() {
        Assertion.assertThat(false).isTrue();
    }

    @Test
    public void test_boolean_assertion_exception_2() {
        Assertion.assertThat(true).isFalse();
    }

    @Test
    public void test_boolean_assertion_exception_3() {
        Assertion.assertThat(false).isEqualTo(true);
    }

    @Test
    public void test_int_assertion() {
        Assertion.assertThat(1).isLessThan(2);
        Assertion.assertThat(1).isGreaterThan(0);
        Assertion.assertThat(1).isEqualTo(1);
    }

    @Test
    public void test_int_assertion_exception_1() {
        Assertion.assertThat(1).isEqualTo(0);
    }

    @Test
    public void test_int_assertion_exception_2() {
        Assertion.assertThat(1).isLessThan(0);
    }

    @Test
    public void test_int_assertion_exception_3() {
        Assertion.assertThat(1).isGreaterThan(2);
    }


    @AfterClass
    public static void test_after_class_1() {
        System.out.println("TestObj:test_after_class_1");
    }

    @Test
    public void test_exception_1() {
        System.out.println("TestObj:test_exception_1");
        throw new RuntimeException("oh no");
    }

    @AfterClass
    public static void test_after_class_2() {
        System.out.println("TestObj:test_after_class_2");
    }

    @BeforeClass
    public static void test_before_class_2() {
        System.out.println("TestObj:test_before_class_2");
    }

    @BeforeClass
    public static void test_before_class_1() {
        System.out.println("TestObj:test_before_class_1");
    }

    @Before
    public void test_before_1() {
        System.out.println("TestObj:test_before_1");
    }

    @Before
    public void test_before_2() {
        System.out.println("TestObj:test_before_2");
        throw new RuntimeException("ruh roh");
    }

    @Test
    public void test_test_2() {
        System.out.println("TestObj:test_test_2");
    }

    @Test
    public void test_exception_2() {
        System.out.println("TestObj:test_exception_2");
        throw new RuntimeException(":(");
    }

    @After
    public void test_after_1() {
        System.out.println("TestObj:test_after_1");
    }

    @After
    public void test_after_2() {
        System.out.println("TestObj:test_after_2");
    }

    @Test
    public void test_test_1() {
        System.out.println("TestObj:test_test_1");
    }

}