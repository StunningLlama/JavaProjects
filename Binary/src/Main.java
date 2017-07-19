import java.awt.event.*;
import java.awt.*;


public class Main extends Frame{
	Label nlabel;
	Float num;
	Button inc;
	Button init;
	Label finlabel;
	TextField input;
	Float incr = 0.5F;
	public Main()
	{
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}});
		this.setSize(400, 200);
		this.setLayout(new BorderLayout());
		this.nlabel = new Label("-");
		this.input = new TextField();
		this.finlabel = new Label("Bin: ");
		this.init = new Button("Enter");
		this.init.addActionListener(new buttonListener(0, this));
		this.inc = new Button("Increment");
		this.inc.addActionListener(new buttonListener(1, this));
		this.add(nlabel, BorderLayout.CENTER);
		this.add(input, BorderLayout.SOUTH);
		this.add(finlabel, BorderLayout.NORTH);
		this.add(init, BorderLayout.WEST);
		this.add(inc, BorderLayout.EAST);
		this.setVisible(true);
	}
	public static void main(String[] args)
	{
		new Main();
	}
	public void input(int id)
	{
		if (id == 0)
		{
			this.num = Float.valueOf(this.input.getText());
			this.nlabel.setText("n = " + String.valueOf(this.num) + "; incr = " + "0.5");
		}
		if (id == 1)
		{
			if (this.num >= this.incr)
			{
				this.num = this.num - this.incr;
				this.finlabel.setText(this.finlabel.getText() + "1");
			}
			else
			{
				this.finlabel.setText(this.finlabel.getText() + "0");
			}
			this.incr = this.incr / 2;
			this.nlabel.setText("n = " + String.valueOf(this.num) + "; incr = " + this.incr);
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
