import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Camera {
	private long windowID;
	
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float pitch = 0;
	public float yaw = 0;
	public float roll = 0;
	
	public float coilx = 0;
	public float coily = 0;
	public float coilz = 0;
	
	
	public float sensM = 0.01f;
	public float sensR = 0.1f;
	MagDisplay display;
	public DoubleBuffer pcPosX = BufferUtils.createDoubleBuffer(1);
	public DoubleBuffer pcPosY = BufferUtils.createDoubleBuffer(1);
	public DoubleBuffer cPosX = BufferUtils.createDoubleBuffer(1);
	public DoubleBuffer cPosY = BufferUtils.createDoubleBuffer(1);
	
	public Camera(long window, MagDisplay in) {
		windowID = window;
		display = in;
		cPosX.put(0, 0);
		cPosY.put(0, 0);
		pcPosX.put(0, 0);
		pcPosY.put(0, 0);
	}
	
	
	public void handleKeyPress(long window, int key, int scancode, int action, int mods) {
		if ( key == GLFW.GLFW_KEY_B && action == GLFW.GLFW_PRESS )
			System.out.println("Inductance: " + Physics.convertToUnits(display.physics.getInductance(display.numareaparts/3, display, 1.0), "H"));
		if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
			glfwSetWindowShouldClose(window, true);
		if ( key == GLFW.GLFW_KEY_C && action == GLFW.GLFW_PRESS )
			display.willupdatearea = true; // We will detect this in the rendering loop
		if ( key == GLFW.GLFW_KEY_T && action == GLFW.GLFW_PRESS )
			display.willupdatefield = true;
	}
	
	public void update() {
		GLFW.glfwGetCursorPos(windowID, cPosX, cPosY);
		double dx = cPosX.get(0) - pcPosX.get(0);
		double dy = cPosY.get(0) - pcPosY.get(0);
		pcPosX.put(0, cPosX.get(0));
		pcPosY.put(0, cPosY.get(0));
		
		pitch = bound(-90f, pitch + dy*sensR, 90f);
		yaw = (float)((yaw + dx*sensR)%360);
		
		float lx = 0;
		float ly = 0;
		Vec3 movementvec = new Vec3();
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
			movementvec.add(0, 0, 1);
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
			movementvec.add(0, 0, -1);
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
			movementvec.add(1, 0, 0);
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
			movementvec.add(-1, 0, 0);
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
			movementvec.add(0, -1, 0);
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
			movementvec.add(0, 1, 0);
		}
		
		boolean coilmovement = false;
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_I) == GLFW.GLFW_PRESS) {
			coilx += sensM;
			coilmovement = true;
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_K) == GLFW.GLFW_PRESS) {
			coilx -= sensM;
			coilmovement = true;
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_J) == GLFW.GLFW_PRESS) {
			coilz -= sensM;
			coilmovement = true;
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_L) == GLFW.GLFW_PRESS) {
			coilz += sensM;
			coilmovement = true;
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_U) == GLFW.GLFW_PRESS) {
			coily += sensM;
			coilmovement = true;
		}
		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_M) == GLFW.GLFW_PRESS) {
			coily -= sensM;
			coilmovement = true;
		}
		if (coilmovement)
			display.willupdatecoil = true;
		

		if (GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
			display.willupdatefield = true;
		}

		if ( GLFW.glfwGetKey(windowID, GLFW.GLFW_KEY_V) == GLFW.GLFW_PRESS )
			display.willupdatearea = true; // We will detect this in the rendering loop
		{
			Vector4f dest = new Vector4f(0, 0, 0, 0);
			if (display.viewMatrix2 != null) {
				Vector4f src = movementvec.unitvector().mul(sensM).toVec4f();
				Matrix4f tmp = new Matrix4f();
				Matrix4f.transpose(display.viewMatrix2, tmp);
//				System.out.println(tmp.toString());
				Matrix4f.transform(tmp, src, dest);
//				System.out.println(display.viewMatrix2.toString() + " and " + dest.toString() + " and " + src.toString());
			}
			x += dest.z;
			y += dest.x;
			z += dest.y;
			/*float angleoffset = (float)Math.atan2(ly, lx);
			double fx = Math.cos(Math.toRadians(yaw) + angleoffset);
			double fy = -Math.sin(Math.toRadians(yaw) + angleoffset);
			double cz = -Math.sin(Math.toRadians(pitch));
			z += cz*sensM*-lx;
			float sc = (float)( (Math.abs(lx) > 0.5)? Math.cos(Math.toRadians(pitch)) : 1.0);
			x += fx*sensM*sc;
			y += fy*sensM*sc;*/
		}
	}

	public float bound(float min, double val, float max) {
		if (val < min)
			return min;
		if (val > max)
			return max;
		return (float)val;
	}
}
