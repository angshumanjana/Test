package Octagonal;
import java.applet.*;
import java.awt.TextArea;
public class SyntaxOut extends Applet
{
	public TextArea main(String str)
	{
		System.out.println("Entered Syntax Print");
		TextArea tf = new TextArea();
		tf.setBounds(20,100,700,700);
		tf.setText(str);
		System.out.println("Syntax Print Ended");
		return tf;
	}
}
