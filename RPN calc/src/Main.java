import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;
	JTextArea stackdisplay;
	JTextField inputArea;
	JLabel info;
	Stack<Double> main_stk;
	int angle;
	public Main()
	{
		this.angle = 0;
		this.main_stk = new Stack<Double>();
		this.setTitle("Reverse Polish Notation Calculator");
		this.setLayout(new BorderLayout());
		JPanel input = new JPanel(new BorderLayout());
		JPanel numbers = new JPanel(new GridLayout(14, 3));
		JButton tmp_button = new JButton("1");
		tmp_button.addActionListener(new buttonListener(1, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("2");
		tmp_button.addActionListener(new buttonListener(2, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("3");
		tmp_button.addActionListener(new buttonListener(3, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("4");
		tmp_button.addActionListener(new buttonListener(4, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("5");
		tmp_button.addActionListener(new buttonListener(5, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("6");
		tmp_button.addActionListener(new buttonListener(6, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("7");
		tmp_button.addActionListener(new buttonListener(7, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("8");
		tmp_button.addActionListener(new buttonListener(8, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("9");
		tmp_button.addActionListener(new buttonListener(9, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("0");
		tmp_button.addActionListener(new buttonListener(0, this));
		numbers.add(tmp_button);
		tmp_button = new JButton(".");
		tmp_button.addActionListener(new buttonListener(21, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("+/-");
		tmp_button.addActionListener(new buttonListener(22, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("E+-");
		tmp_button.addActionListener(new buttonListener(23, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("<--");
		tmp_button.addActionListener(new buttonListener(24, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("CE");
		tmp_button.addActionListener(new buttonListener(25, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("PUSH");
		tmp_button.addActionListener(new buttonListener(31, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("POP");
		tmp_button.addActionListener(new buttonListener(32, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("CLEAR");
		tmp_button.addActionListener(new buttonListener(33, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("DUP");
		tmp_button.addActionListener(new buttonListener(34, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("SWAP");
		tmp_button.addActionListener(new buttonListener(35, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("ROT");
		tmp_button.addActionListener(new buttonListener(36, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("+");
		tmp_button.addActionListener(new buttonListener(100, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("*");
		tmp_button.addActionListener(new buttonListener(101, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("^");
		tmp_button.addActionListener(new buttonListener(102, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("-");
		tmp_button.addActionListener(new buttonListener(103, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("/");
		tmp_button.addActionListener(new buttonListener(104, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("%");
		tmp_button.addActionListener(new buttonListener(105, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("sin");
		tmp_button.addActionListener(new buttonListener(106, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("cos");
		tmp_button.addActionListener(new buttonListener(107, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("tan");
		tmp_button.addActionListener(new buttonListener(108, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("asin");
		tmp_button.addActionListener(new buttonListener(109, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("acos");
		tmp_button.addActionListener(new buttonListener(110, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("atan");
		tmp_button.addActionListener(new buttonListener(111, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("sqrt");
		tmp_button.addActionListener(new buttonListener(112, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("log");
		tmp_button.addActionListener(new buttonListener(113, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("ln");
		tmp_button.addActionListener(new buttonListener(114, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("DEG");
		tmp_button.addActionListener(new buttonListener(115, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("RAD");
		tmp_button.addActionListener(new buttonListener(116, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("GRA");
		tmp_button.addActionListener(new buttonListener(117, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("\u03C0");
		tmp_button.addActionListener(new buttonListener(118, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("e");
		tmp_button.addActionListener(new buttonListener(119, this));
		numbers.add(tmp_button);
		tmp_button = new JButton("\u03c6");
		tmp_button.addActionListener(new buttonListener(120, this));
		numbers.add(tmp_button);
		input.add(numbers, BorderLayout.NORTH);
		Panel display = new Panel(new BorderLayout());
		this.inputArea = new JTextField("");
		this.stackdisplay = new JTextArea("");
		this.stackdisplay.setEditable(false);
		this.info = new JLabel("");
		display.add(this.stackdisplay, BorderLayout.CENTER);
		display.add(this.inputArea, BorderLayout.SOUTH);
		display.add(info, BorderLayout.NORTH);
		this.add(input, BorderLayout.WEST);
		this.add(display, BorderLayout.CENTER);
		this.inputArea.addKeyListener(this);
		this.updateStackGphx();
		this.setVisible(true);
		this.setSize(numbers.getWidth()+this.getInsets().left+this.getInsets().right+300, numbers.getHeight()+this.getInsets().top+this.getInsets().bottom);
	}
	public static void main(String[] args)
	{
		new Main();
	}
	protected void input(int val)
	{
		if (val < 10)
		{
			this.inputArea.setText(this.inputArea.getText() + String.valueOf(val));
		}
		if (val == 21)
		{
			if (!this.inputArea.getText().contains("."))
				this.inputArea.setText(this.inputArea.getText() + ".");
		}
		if (val == 22)
		{
			if (this.inputArea.getText().length() == 0)
				this.inputArea.setText("-");
			else if (this.inputArea.getText().charAt(0) == '+')
				this.inputArea.setText("-" + this.inputArea.getText().substring(1));
			else if (this.inputArea.getText().charAt(0) == '-')
				this.inputArea.setText("+" + this.inputArea.getText().substring(1));
			else
				this.inputArea.setText("-" + this.inputArea.getText());
		}
		if (val == 23)
		{
			if (!this.inputArea.getText().contains("E"))
			{
				this.inputArea.setText(this.inputArea.getText() + "E");
			}
			else if ((this.inputArea.getText().indexOf('E') + 1) == this.inputArea.getText().length())
			{
				this.inputArea.setText(this.inputArea.getText() + "-");
			}
			else
			{
				String before = this.inputArea.getText().split("E")[0];
				String after = this.inputArea.getText().substring(this.inputArea.getText().indexOf('E') + 1);
				if (this.inputArea.getText().charAt(this.inputArea.getText().indexOf('E') + 1) == '+')
					this.inputArea.setText(before + "E-" + after.substring(1));
				else if (this.inputArea.getText().charAt(this.inputArea.getText().indexOf('E') + 1) == '-')
					this.inputArea.setText(before + "E+" + after.substring(1));
				else
					this.inputArea.setText(before + "E-" + after);
			}
		}
		if (val == 24)
		{
			if (!this.inputArea.getText().equals(""))
				this.inputArea.setText(this.inputArea.getText().substring(0, this.inputArea.getText().length() - 1));
		}
		if (val == 25)
		{
			this.inputArea.setText("");
		}
		if (val == 31)
		{
			try {
				this.main_stk.push(Double.valueOf(this.inputArea.getText()));
				this.inputArea.setText("");
			}
			catch (NumberFormatException e)
				{return;}
		}
		if (val == 32)
		{
			if (!this.main_stk.isEmpty())
				this.main_stk.pop();
		}
		if (val == 33)
		{
			this.main_stk.clear();
			this.inputArea.setText("");
		}
		if (val == 34)
		{
			this.main_stk.push(this.main_stk.peek());
		}
		if (val == 35)
		{
			if (this.main_stk.size() > 1)
			{
				Double a = this.main_stk.pop();
				Double b = this.main_stk.pop();
				this.main_stk.push(a);
				this.main_stk.push(b);
			}
		}
		if (val == 36)
		{
			if (this.main_stk.size() > 2)
			{
				Double a = this.main_stk.pop();
				Double b = this.main_stk.pop();
				Double c = this.main_stk.pop();
				this.main_stk.push(b);
				this.main_stk.push(a);
				this.main_stk.push(c);
			}
		}
		if ((val > 99) & (val < 106))
		{
			if (this.main_stk.size() < 2)
				return;
			Double b = this.main_stk.pop();
			Double a = this.main_stk.pop();
			if (val == 100)
				this.main_stk.push(a + b);
			if (val == 101)
				this.main_stk.push(a * b);
			if (val == 102)
				this.main_stk.push(Math.pow(a, b));
			if (val == 103)
				this.main_stk.push(a - b);
			if (val == 104)
				this.main_stk.push(a / b);
			if (val == 105)
				this.main_stk.push(a % b);
		}
		if ((val > 105) & (val < 115))
		{
			if (this.main_stk.size() < 1)
				return;
			Double a = this.main_stk.pop();
			if (val < 109)
			{
				Double b = 0.0;
				if (this.angle == 0)
					b = Math.toRadians(a);
				else if (this.angle == 1)
					b = a;
				else if (this.angle == 2)
					b = Math.toRadians(a * 0.9);
				if (val == 106)
					this.main_stk.push(Math.sin(b));
				if (val == 107)
					this.main_stk.push(Math.cos(b));
				if (val == 108)
					this.main_stk.push(Math.tan(b));
			}
			else if (val < 112)
			{
				Double b = 0.0;
				if (val == 109)
					b = Math.asin(a);
				if (val == 110)
					b = Math.acos(a);
				if (val == 111)
					b = Math.atan(a);
				if (this.angle == 0)
					b = Math.toDegrees(b);
				else if (this.angle == 2)
					b = Math.toDegrees(b * 0.9);
				this.main_stk.push(b);
			}
			if (val == 112)
				this.main_stk.push(Math.sqrt(a));
			if (val == 113)
				this.main_stk.push(Math.log10(a));
			if (val == 114)
				this.main_stk.push(Math.log(a));
		}
		if (val == 115)
		{
			this.angle = 0;
		}
		if (val == 116)
		{
			this.angle = 1;
		}
		if (val == 117)
		{
			this.angle = 2;
		}
		if (val == 118)
		{
			this.main_stk.push(3.1415926535897932384626433832795);
		}
		if (val == 119)
		{
			this.main_stk.push(2.7182818284590452353602874713526);
		}
		if (val == 120)
		{
			this.main_stk.push(1.6180339887498948482045868343656);
		}
		this.updateStackGphx();
	}
	private void updateStackGphx()
	{
		this.stackdisplay.setText("=== STACK ===\n");
		int ind = 1;
		for (Double d : this.main_stk)
		{
			this.stackdisplay.setText(this.stackdisplay.getText() + ind + ":\t" + d + "\n");
			ind ++;
		}
		String ang = "";
		if (this.angle == 0)
			ang = "Degrees";
		else if (this.angle == 1)
			ang = "Radians";
		else if (this.angle == 2)
			ang = "Gradians";
		this.info.setText("Angle: " + ang + ", Stack size: " + this.main_stk.size());
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			try {
				this.main_stk.push(Double.valueOf(this.inputArea.getText()));
				this.inputArea.setText("");
				this.updateStackGphx();
			}
			catch (NumberFormatException ex)
				{return;}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
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
