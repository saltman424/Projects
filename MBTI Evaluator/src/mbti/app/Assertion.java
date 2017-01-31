package mbti.app;

public class Assertion {
	private Assertion() {}
	
	public static void require(boolean test, String error) throws Exception {
		if (!test) throw new Exception(error);
	}
	
	public static boolean check(boolean test, String error) {
		if (!test) System.err.println(error);
		return test;
	}
}
