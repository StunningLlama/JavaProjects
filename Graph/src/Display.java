import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;

import javax.swing.JFrame;


public class Display extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8592695031508199539L;
	GraphCanvas canvas;
	public TextField eqn;
	Listeners l;
	public Display(MathUtil m) {
		super();
		eqn = new TextField("x^2");
		eqn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		this.setLayout(new BorderLayout());
		this.setSize(Graph.WIDTH, Graph.HEIGHT);
		setVisible(true);
		canvas = new GraphCanvas(m, this);
		this.add(eqn, BorderLayout.SOUTH);
		add(canvas, BorderLayout.CENTER);
		canvas.setPreferredSize(new Dimension(Graph.WIDTH, Graph.HEIGHT /* - CONSTANT */));
		pack();
		l = new Listeners(canvas);
		canvas.addMouseListener(l);
		canvas.addMouseWheelListener(l);
		canvas.addKeyListener(l);
		canvas.addMouseMotionListener(l);
		this.addWindowListener(l);
		eqn.addKeyListener(l);
	}
}
