import java.io.IOException;
import java.io.InputStream;


public class ProcThread extends Thread {
	String cmd;
	Terminal t;
	Process proc;
	
	@Override
	public void run()
	{
		t.input = false;
		try {
			proc = Runtime.getRuntime().exec(cmd);
			InputStream i = proc.getInputStream();
			String str = "";
			while (i.available() > 0 )
			{
				int n = i.read();
				if (n == -1)
					break;
				else
				{
					str += (char) n;
					if (i.available() == 0)
					{
						t.writeString(str);
						str = "";
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();}
		catch (NullPointerException e) {
			System.out.println("asd3");}
		t.sx = 0;
		t.sy++;
		t.writeString( System.getProperty("user.dir") + ">a");
		t.input = true;
	}
	
	public void input(String str)
	{
		try {
		for (int i = 0; i < str.length(); i++)
			proc.getOutputStream().write(str.charAt(i));
		} catch (IOException e) {}
	}	
	
	public ProcThread(String name, Terminal ins)
	{
		cmd = name;
		t = ins;
	}
}
