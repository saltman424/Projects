package mbti.app.user;

import mbti. app.*;

public class MBTIPair {
	public final MBTI a, b;
	
	public MBTIPair(MBTI a, MBTI b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public int hashCode() {
		return Math.min(a.ordinal(), b.ordinal()) * 100 + Math.max(a.ordinal(), b.ordinal());
	}
}
