package htmlparser;

public class ForDebug {
	public static final void doAssert(boolean cond) {
		doAssert(cond, "");
	}
	public static final void doAssert(boolean cond, String msg) {
		if(cond==true){
			return;
		}
		throw new AssertionError(msg);
	}
}
