package htmlparser;

public class HtmlParser
{
	private CharSequence source;
	private int pos=0;
	private int len=0;
	
	
	
	private void init(CharSequence cs){
		this.source=cs;
		this.len=cs.length();
	}
}
