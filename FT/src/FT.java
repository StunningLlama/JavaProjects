import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Scrollbar;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FT extends JFrame implements Runnable/*, MouseListener, MouseMotionListener*/ {
	
	float[] data;
	float[] fftsin;
	float[] fftcos;
	int fftsize = 10;
	int length = 20;
	Options opt;
	int prevval = 0;
	
	RenderCanvas r;
	public static void main(String[] args) {
		FT ft = new FT();
		ft.run();
	}
	
	public FT() {
		data = new float[length];
		for (int i = 0; i < length; i++) {
			data[i] = (float)(Math.random()*2.0-1.0);
		}
		this.setSize(800,800);
		this.setVisible(true);
		r= new RenderCanvas(this);
		this.add(r);
		//this.doFFT();
		opt = new Options();
		this.doFFT();
		//r.addMouseListener(this);
		//r.addMouseMotionListener(this);
	}

	int i = 0;
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {}
			if (prevval != opt.sb.getValue()) {
				prevval = opt.sb.getValue();
				System.out.println(prevval);
				this.doFFT();
				r.repaint();
			}
		}
	}
	int supersampling = 1;
	//float pshift = -0.01f;
	public float integrateandmultiply(FFTtype type, int coeff) {
		float sum = 0f;
		float c = 0f;
		for (int i = 0; i < length*supersampling; i ++) {
			float y = data[i/supersampling]*(float)(
					(type==FFTtype.SINE)?
					Math.sin(coeff*2f*Math.PI*((float)(i)/length/supersampling - - getOffset())):
					Math.cos(coeff*2f*Math.PI*((float)(i)/length/supersampling - - getOffset()))) * 1.0f/supersampling
					- c;
			float t = sum + y;
			c = (t - sum) - y;
			sum = t;
		}
		return ((type==FFTtype.SINE)?2f:1f)*sum/(length);
	}
	
	public void doFFT() {
		fftsin = new float[fftsize+1];
		fftcos = new float[fftsize+1];
		fftsin[0] = 0f;
		fftcos[0] = integrateandmultiply(FFTtype.COSINE, 0);
		for (int i = 1; i <= fftsize; i++) {
			fftsin[i] = integrateandmultiply(FFTtype.SINE, i);
			fftcos[i] = integrateandmultiply(FFTtype.COSINE, i);
		}
	}
	
	public float getOffset() {
		return ((float)opt.sb.getValue() - 100)/length/50.0f;
	}
	
	public float getInverseFFT(float x) {
		float sum = fftcos[0];
		for (int i = 1; i <= fftsize; i++) {
			sum += fftsin[i] * (float)Math.sin(2f*Math.PI*i*(x+getOffset()));
			sum += fftcos[i] * (float)Math.cos(2f*Math.PI*i*(x+getOffset()));
		}
		return sum;
	}
}

enum FFTtype {
	SINE,
	COSINE
}

class RenderCanvas extends JPanel {
	Image screen;
	FT parent;
	public static final int INTERVAL = 4;
	@Override
	public void paintComponent(Graphics real) {
		Graphics g = screen.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 800);
		g.setColor(Color.black);
		for (int i = 0; i < parent.length-1; i++)
		{
			g.drawLine(i*16 + 50, (int)(parent.data[i]*50.0) + 100, (i+1)*16 + 50, (int)(parent.data[i+1]*50.0) + 100);
		}
		for (int i = 0; i <= parent.fftsize; i++) {
			g.drawString("SIN " + i + ": " + parent.fftsin[i], 10, i*20+200);
			g.drawString("COS " + i + ": " + parent.fftcos[i], 200, i*20+200);
		}
		g.setColor(Color.red);
		for (int i = 0; i < parent.length*10; i++)
		{
			g.drawLine((int)(i*0.1*16) + 50, (int)(parent.getInverseFFT(i*0.1f/parent.length)*50.0) + 100, (int)((i+1)*0.1*16) + 50, (int)(parent.getInverseFFT((i+1)*0.1f/parent.length)*50.0) + 100);
		}
		g.setColor(Color.blue);

		for (int i = 0; i < parent.length-1; i++)
		{
			g.drawLine(i*16 + 50, (int)(parent.getInverseFFT((float)(i)/parent.length)*50.0) + 100, (i+1)*16 + 50, (int)(parent.getInverseFFT((float)(i+1)/parent.length)*50.0) + 100);
		}
		//g.drawLine(0, (int)(0*50.0) + 100, 400, (int)(0*50.0) + 100);
		//g.drawLine(0, (int)(1*50.0) + 100, 400, (int)(1*50.0) + 100);
		real.drawImage(screen, 0, 0, parent);
	}
	public RenderCanvas(FT w) {
		parent = w;
		screen = parent.createImage(800,800);
	}
}

class Options extends JFrame {
	Scrollbar sb;
	public Options() {
		this.setSize(500, 50);
		sb = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10, 0, 200 + 10);
		this.add(sb);
		this.setVisible(true);
	}
}