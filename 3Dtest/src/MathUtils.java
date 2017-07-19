
public class MathUtils {
	public static Vector2F ProjectTo2D(Vector3F campos, Vector3F objpos, Vector3F rot)
	{
		Vector2F pixel = new Vector2F(0f, 0f);
		
		//Transformation
		Vector3F tmp = new Vector3F(objpos.x - campos.x, objpos.y - campos.y, objpos.z - campos.z);
		
		//We don't need scaling? Maybe
		//Start rotations
		float sin;
		float cos;
		Vector3F applied;
		
		applied = new Vector3F(tmp);
		/*	sin = (float)Math.sin((float)rot.x);
		cos = (float)Math.cos((float)rot.x);
		tmp.x = applied.x * cos - applied.z * sin;
		tmp.z = applied.x * sin + applied.z * cos;
		sin = (float)Math.sin((float)rot.y);
		cos = (float)Math.cos((float)rot.y);
		tmp.y = applied.y * cos - tmp.z * sin;
		applied.z = tmp.y * sin - tmp.z * cos;
		sin = (float)Math.sin((float)rot.z);
		cos = (float)Math.cos((float)rot.z);
		applied.x = tmp.x * cos - tmp.y * sin;
		applied.y = tmp.x * sin + tmp.y * cos;*/
		
		//Project onto 2D
		pixel.x = applied.x/applied.z;
		pixel.y = applied.y/applied.z;
		//System.out.println(pixel.x + "  " + pixel.y);
		return pixel;
	}
}
