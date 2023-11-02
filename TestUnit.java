import java.util.Map;

public class TestUnit {
    public static void main(String[] args) {
        System.out.println("sup");


        System.out.println("\n\n********************************\n\n");
        
        System.out.println("TESTING CLASS TESTOBJ\n");
        Map<String, Throwable> testClassResults = Unit.testClass("TestObj");

        for (String testName : testClassResults.keySet()) {
            System.out.print(testName + ": ");
            System.out.println("\t" + testClassResults.get(testName));
        }
        System.out.println("\n\n********************************\n\n");

        System.out.println("TESTING CLASS TESTQUICKCHECK\n");
        Map<String, Object[]> quickCheckResults = Unit.quickCheckClass("TestQuickCheck");

        for (String testName : quickCheckResults.keySet()) {
            System.out.print(testName + ": ");
            System.out.println("\t" + quickCheckResults.get(testName));
        }
        System.out.println("\n\n********************************\n\n");


    }
}
