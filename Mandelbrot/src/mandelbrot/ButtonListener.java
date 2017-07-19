package mandelbrot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class ButtonListener implements ActionListener
{
	OptionWindow i;
	int id;
	
	public void actionPerformed(ActionEvent arg0) {
		i.button(id);
	}
	
	public ButtonListener(OptionWindow o, int a)
	{
		i = o;
		id = a;
	}
}