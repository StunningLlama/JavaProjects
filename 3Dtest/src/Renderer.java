import java.awt.Color;
import java.awt.Graphics;


public class Renderer {
	Vector3F camerapos;
	Vector3F camerarot;
	Vector3F[] vertices;
	int displaywidth;
	int displayheight;
	double mul = 200.0;
	public Renderer()
	{
		camerapos = new Vector3F(0f, 0f, 0f);
		camerarot = new Vector3F(0f, 0f, 0f);
		displaywidth = 1200;
		displayheight = 720;
	}
	int index = 0;
	public void createArray(int size) {
		vertices = new Vector3F[size*2];
	}
	public void addLine(Vector3F a, Vector3F b) {
		vertices[index] = new Vector3F(a);
		index++;
		vertices[index] = new Vector3F(b);
		index++;
	}
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		for (int i = 0; i < index; i+=2)
		{
			Vector2F p1 = MathUtils.ProjectTo2D(camerapos, vertices[i], camerarot);
			Vector2F p2 = MathUtils.ProjectTo2D(camerapos, vertices[i+1], camerarot);
			//System.out.println((p1.x*mul)+displaywidth/2);
			g.drawLine((int)(p1.x*mul)+displaywidth/2, (int)(p1.y*mul)+displayheight/2, (int)(p2.x*mul)+displaywidth/2, (int)(p2.y*mul)+displayheight/2);
		}
	}
}
