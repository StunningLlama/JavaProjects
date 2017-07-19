import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class Planet extends Body {

	public Planet(double ix, double iz, double ivx, double ivz, double imass) {
		super(ix, iz, ivx, ivz, imass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Render(Graphics g, int x, int y) {
		g.setColor(Color.BLUE);
		//g.fillOval(x-10, y-10, 20, 20);
		g.fillRect(x-1, y-1, 3, 3);
		g.setColor(new Color(0, 127, 127));
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.drawString(name, x+5, y+8);
	}
	
}
