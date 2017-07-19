
public class Vector3F {
	//XZ is flat, Y is height
	float x; // Roll
	float y; // Pitch
	float z; // Yaw
	public Vector3F(float xd, float yd, float zd) {
		x = xd;
		y = yd;
		z = zd;
	}
	public Vector3F(Vector3F v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
}
