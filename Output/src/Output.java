import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Output extends Frame implements WindowListener, Runnable {
	
	private static final long serialVersionUID = 1L;
	private List<String> buff;
	TextArea text;
	
	public static void main(String[] args) 
	{
		Output instance = new Output();
		instance.run();
	}
	
	public Output()
	{
		this.setSize(600, 350);
		this.buff = new ArrayList<String>();
		text = new TextArea();
		text.setEditable(false);
		text.setColumns(65536);
		this.setLayout(new BorderLayout());
		this.add(text, BorderLayout.CENTER);
		this.setTitle("Standard Output");
		this.setVisible(true);
		this.addWindowListener(this);
	}
	
	public void run()
	{
		while (true)
		{
			String line = "";
			while (true)
			{
				char c = (char) -1;
				try {
					c = (char) System.in.read();
				} catch (IOException e) {}
				if (c != -1 && c != '\n')
					line += c;
				else if (c == -1)
					System.exit(0);
				else
					break;
			}
			buff.add(line);
			if (buff.size() > 300)
				buff.remove(0);
			StringBuilder textstr = new StringBuilder("");
			for (String i : buff)
			{
				textstr.append(i);
				textstr.append("\n");
			}
			text.setCaretPosition(text.getText().length());
			text.setText(textstr.toString());
		}
	}

	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}

	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}
}
