package hello;

import java.awt.*;
import java.util.ArrayList;

public class Run {
	public static void main(String[] args) throws InterruptedException
	{
		Frame window = new Frame("The window");
		window.setSize(400, 400);
		window.setLayout(new GridLayout(4,1));
		Scrollbar refreshrate = new Scrollbar(Scrollbar.HORIZONTAL, 1000/6, 10, 0, 1000);
		Label a = new Label();
		Label b = new Label();
		Canvas c = new Canvas();
		window.add(refreshrate);
		window.add(a);
		window.add(b);
		window.add(c);
		//Graphics gphx = c.getGraphics();
		//gphx.drawOval(0, 0, 100, 50);
		a aa = new a();
		a ab;
		ab = aa;
		aa.va = 67;
		ArrayList<a> ac = new ArrayList<a>();
		b bb = new b();
		try {
			Integer av = null;
			//Integer bv = av * 7;
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			System.out.println("\n\n\n\n\n\n\n\n");
			StackTraceElement first = e.getStackTrace()[0];
			String msg = e.getClass().getName() + "At:\nClass: " + first.getClassName() + "\nMethod: " + first.getMethodName() + "\nFile: " + first.getFileName() + "Line: " + first.getLineNumber();
			System.out.print(msg);
		}
		ac.add((a) bb);
		((b) ac.get(0)).neww();
		window.setVisible(true);
		System.out.print(aa.va + "  " + ab.va);
		int[] arr = {4, 5, 0};
		int[] arrb = arr;
		arrb[2] = 56;
		for (int i : arr)
			System.out.println(i);
		for (int i : arrb)
			System.out.println(i);
		while(true)
		{
			Thread.currentThread();
			Thread.sleep(refreshrate.getValue());
			
			system.o(":" + window.isFocusableWindow());
			b.setText("The Y is: " + window.getY());
		}
	}
}
