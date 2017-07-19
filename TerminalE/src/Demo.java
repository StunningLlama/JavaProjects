import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class Demo implements ConsoleListener {
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		new Demo();
	}

	public Demo()
	{
		System.out.println(System.getProperty("user.dir") + ">");
		int width = (int) Math.ceil(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() / 8.0);
		int height = (int) Math.ceil(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight() / 12.0);
		Terminal instance = new Terminal();
		/*for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 32; x++)
			{
				instance.setChar(x, y, y * 32 + x);
			}
		}
		instance.drawText(0, 17, "Hello, world!");*/
		instance.setConsoleListener(this);
		instance.run();
	}
	
	@Override
	public void onKeyPress(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyRelease(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMousePress(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseRelease(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWindowClose() {
		// TODO Auto-generated method stub
		
	}
}
