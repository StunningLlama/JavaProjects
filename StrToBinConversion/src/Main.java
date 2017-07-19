import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main extends Frame {
	
	private static final long serialVersionUID = 1L;

	TextArea input;
	TextArea output;
	Button convert;
	
	public static void main(String[] args)
	{
		new Main();
	}
	
	public Main()
	{
		this.setSize(300, 500);
		this.setLayout(new BorderLayout());
		Panel upper = new Panel(new BorderLayout());
		input = new TextArea();
		output = new TextArea();
		output.setEditable(false);
		output.setText("Nums here");
		convert = new Button("Convert");
		convert.addActionListener(new ConvListener(this));
		upper.add(input, BorderLayout.CENTER);
		upper.add(convert, BorderLayout.SOUTH);
		this.add(upper, BorderLayout.NORTH);
		this.add(output, BorderLayout.CENTER);
		this.setVisible(true);
	}
}

class ConvListener implements ActionListener {
	
	Main holder;
	
	public ConvListener(Main ins)
	{
		this.holder = ins;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		holder.output.setText("");
		StringBuilder out = new StringBuilder("");
		for (int i = 0; i < holder.input.getText().length(); i++)
		{
			out.append((int) holder.input.getText().charAt(i));
			out.append(" (");
			out.append(this.toHex((int) holder.input.getText().charAt(i)));
			out.append(")\n");
		}
		holder.output.setText(out.toString());
	}
	
	public String toHex(int val)
	{
		int i = 0;
		while (true)
		{
			if (Math.pow(16, i) > val)
			{
				break;
			}
			i++;
		}
		if (i == 0) return "0x0";
		String retstr = "0x";
		String[] glyphs = {
				"0", "1", "2", "3",
				"4", "5", "6", "7",
				"8", "9", "A", "B",
				"C", "D", "E", "F"};
		int cv = val;
		for (int pwr = i - 1; pwr >= 0; pwr--)
		{
			int curdigit = cv / (int) Math.pow(16, pwr);
			retstr += glyphs[curdigit];
			cv -= curdigit * (int) Math.pow(16, pwr);
		}
		return retstr;
	}
}
