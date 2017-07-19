import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public interface ConsoleListener {
	public void onKeyPress(KeyEvent event);
	public void onKeyRelease(KeyEvent event);
	public void onMousePress(MouseEvent event);
	public void onMouseRelease(MouseEvent event);
	public void onWindowClose();
}
