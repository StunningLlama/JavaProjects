import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;


public class Chameleon extends JFrame implements ActionListener {
	Button bRed;
	Button bGreen;
	Button bBlue;
	Label lRed;
	Label lGreen;
	Label lBlue;
	int cRed = 17;
	int cGreen = 13;
	int cBlue = 15;
	public Chameleon() {
		bRed = new Button("Red Chameleon");
		bGreen = new Button("Green Chameleon");
		bBlue = new Button("Blue Chameleon");
		bRed.addActionListener(this);
		bGreen.addActionListener(this);
		bBlue.addActionListener(this);
		lRed = new Label(String.valueOf(cRed));
		lGreen = new Label(String.valueOf(cGreen));
		lBlue = new Label(String.valueOf(cBlue));
		this.setLayout(new GridLayout(2, 3));
		this.add(lRed);
		this.add(lGreen);
		this.add(lBlue);
		this.add(bRed);
		this.add(bGreen);
		this.add(bBlue);
		this.setSize(400, 100);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		new Chameleon();
	}
	
	private void updateLabels() {
		lRed.setText(String.valueOf(cRed));
		lGreen.setText(String.valueOf(cGreen));
		lBlue.setText(String.valueOf(cBlue));
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bRed) {
			if (cGreen > 0 && cBlue > 0) {
				cGreen--;
				cBlue--;
				cRed += 2;
				updateLabels();
			}
		} else if (e.getSource() == bGreen) {
			if (cRed > 0 && cBlue > 0) {
				cRed--;
				cBlue--;
				cGreen += 2;
				updateLabels();
			}
		} else if (e.getSource() == bBlue) {
			if (cGreen > 0 && cRed > 0) {
				cGreen--;
				cRed--;
				cBlue += 2;
				updateLabels();
			}
		}
	};
}
