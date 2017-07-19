import java.awt.Graphics2D;


abstract class RenderableObject {
	public double x;
	public double y;
	public abstract void render(Graphics2D g, double x, double y);
}
