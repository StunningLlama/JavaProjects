import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;


public class Main {
	public static void main(String[] args) {
		Frame f = new Frame();
		JTextArea text = new JTextArea("Input Text Here");
		text.setLineWrap(true);
		f.setLayout(new BorderLayout());
		f.add(text, BorderLayout.CENTER);
		Button start = new Button("Start Input");
		Bot b = new Bot(text);
		start.addActionListener(b);
		f.add(start, BorderLayout.SOUTH);
		f.setVisible(true);
		f.setSize(600, 600);
		b.run();
	}
	
	static int getCode(char c)
	{
		switch(c) {
		case '!':
			return KeyEvent.VK_1;
		case '?':
			return KeyEvent.VK_SLASH;
		case '\"':
			return KeyEvent.VK_QUOTE;
		case '(':
			return KeyEvent.VK_9;
		case ')':
			return KeyEvent.VK_0;
		case ':':
			return KeyEvent.VK_SEMICOLON;
		default:
			return -1;
		}
	}
}

class Bot implements Runnable, ActionListener {
	JTextArea text;
	int running = -1;
	int cPos = 0;
	int mswait = 10;
	long nextminute = 0;
	@Override
	public void run() {
		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e1) {}
		nextminute = System.currentTimeMillis() + 60000 + 10000;
		while (true) {
			try {
				Thread.sleep(mswait);
			} catch (InterruptedException e) {}
			if (running != -1) {
				if (running > 0)
					running--;
				else {
					if (System.currentTimeMillis() > nextminute) {
						System.exit(0);
					}
					if (cPos == text.getText().length()) {
						bot.keyPress(KeyEvent.VK_ENTER);
						bot.keyRelease(KeyEvent.VK_ENTER);
						cPos = 0;
						continue;
					}
					char c = text.getText().charAt(cPos);
					if (Character.isUpperCase(c) || Main.getCode(c) != -1)
						bot.keyPress(KeyEvent.VK_SHIFT);
					int keycode = KeyEvent.getExtendedKeyCodeForChar(c);
					if (Main.getCode(c) != -1)
						keycode = Main.getCode(c);
					bot.keyPress(keycode);
					bot.keyRelease(keycode);
					if (Character.isUpperCase(c) || Main.getCode(c) != -1)
						bot.keyRelease(KeyEvent.VK_SHIFT);
					cPos++;
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		running = 2000/mswait;
	}
	
	public Bot(JTextArea t) {
		text = t;
	}
}