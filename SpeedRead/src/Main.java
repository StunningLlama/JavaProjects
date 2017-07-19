import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main extends Frame {
		private static final long serialVersionUID = 1L;
		Scrollbar speed;
		Scrollbar track;
		Label word;
		Label lSpeed;
		Label lPos;
		String[] text = {};
		int paused = 0;
		int loc = 0;
		public Main(String[] args) throws InterruptedException
		{
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent windowEvent){
					System.exit(0);
				}});
			this.setSize(560, 360);
			this.setTitle("Speed read");
			this.setLayout(new BorderLayout());
			Panel top = new Panel(new BorderLayout());
			Panel topleft = new Panel(new GridLayout(2, 1));
			Panel topright = new Panel(new GridLayout(2, 1));
			Panel bottom = new Panel(new GridLayout(2, 3));
			Button tmp_button = new Button("BACKWARD");
			tmp_button.addActionListener(new buttonListener(1, this));
			bottom.add(tmp_button);
			tmp_button = new Button("PAUSE");
			tmp_button.addActionListener(new buttonListener(2, this));
			bottom.add(tmp_button);
			tmp_button = new Button("FORWARD");
			tmp_button.addActionListener(new buttonListener(3, this));
			bottom.add(tmp_button);
			tmp_button = new Button("LAST WORD");
			tmp_button.addActionListener(new buttonListener(4, this));
			bottom.add(tmp_button);
			tmp_button = new Button("OPEN FILE");
			tmp_button.addActionListener(new buttonListener(5, this));
			bottom.add(tmp_button);
			tmp_button = new Button("NEXT WORD");
			tmp_button.addActionListener(new buttonListener(6, this));
			bottom.add(tmp_button);
			
			lSpeed = new Label("Speed: 400 WPM", 2);
			topleft.add(lSpeed);
			speed = new Scrollbar(0,400,1,60,1000);
			topright.add(speed);
			lPos = new Label("Word Position: at 0", 2);
			topleft.add(lPos);
			track = new Scrollbar(0,1,1,0,0);
			topright.add(track);
			top.add(topleft, BorderLayout.WEST);
			top.add(topright, BorderLayout.CENTER);
			this.add(top, BorderLayout.NORTH);
			this.add(bottom, BorderLayout.SOUTH);
			word = new Label("", 1);
			word.setFont(new Font("Arial", Font.BOLD, 36));
			this.add(word, BorderLayout.CENTER);
			
			this.setVisible(true);

			if (args.length == 1)
			{
				this.openFile(args[0]);
				this.paused = 1;
				word.setText(text[0]);
				Thread.sleep(1000);
			}
			else
			{
				word.setText("Open a file to speed read.");
			}

			while (true)
			{
				this.lSpeed.setText("Speed: " + this.speed.getValue() + " WPM");
				this.lPos.setText("Word Position: at " + (this.loc + 1));
				if (this.track.getValue() != this.loc)
				{
					this.loc = this.track.getValue();
				}
				if (text.length != 0)
					word.setText(text[loc]);
				if (paused != 0)
				{
					if (!(loc + paused < 0 | loc + paused >= text.length))
					{
						loc += paused;
					}
				}
				this.track.setValue(loc);
				Thread.sleep((long) (1.0 / (this.speed.getValue() / 60000.0)));
			}
		}
		public void openFile(String name)
		{
			FileInputStream infile = null;
			try {
				infile = new FileInputStream(name);
			} catch (FileNotFoundException e) {
				this.word.setText("Error: File not found");
				return;
			}
			StringBuilder input = new StringBuilder();
			int rbyte = -1;
			while (true)
			{
				try {
					rbyte = infile.read();
				} catch (IOException e) {break;}
				if (rbyte == -1) break;
				if (rbyte > 31 && rbyte < 127)
					input.append((char) rbyte);
				if (rbyte == '\n')
					input.append(' ');
			}
			this.text = input.toString().split(" ");
			this.track.setMaximum(this.text.length - 1);
			loc = 0;
			try {
				infile.close();
			} catch (IOException e) {}
		}
		public static void main(String[] args) throws InterruptedException
		{
			new Main(args);
		}
	protected void input(int val)
	{
		if (val == 1)
		{
			this.paused = -1;
		}
		if (val == 2)
		{
			this.paused = 0;
		}
		if (val == 3)
		{
			this.paused = 1;
		}
		if (val == 4)
		{
			if (this.loc - 1 >= 0)
			{
				this.loc = this.loc - 1;
				this.track.setValue(this.track.getValue() - 1);
				this.word.setText(this.text[this.loc]);
			}
		}
		if (val == 6)
		{
			if (this.loc + 1 < this.text.length)
			{
				this.loc = this.loc + 1;
				this.track.setValue(this.track.getValue() + 1);
				this.word.setText(this.text[this.loc]);
			}
		}
		if (val == 5)
		{

			FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
			fd.setVisible(true);
			String filename = fd.getDirectory() + fd.getFile();
			this.openFile(filename);
		}
	}
}
class buttonListener implements ActionListener
{
	private int id;
	private Main baseclass;
	public buttonListener(int val, Main arg_class)
	{
		this.id = val;
		this.baseclass = arg_class;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.baseclass.input(this.id);
	}
}