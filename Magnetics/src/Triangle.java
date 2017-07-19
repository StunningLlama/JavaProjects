
public class Triangle {
	
	private Vec3 PointA;
	private Vec3 PointB;
	private Vec3 PointC;
	double distAB;
	double distBC;
	double distCA;
	public double area;
	private Vec3 MidPointAB;
	private Vec3 CToA;
	private Vec3 AToB;
	private Vec3 normal;
	private double altitude;
	
	//private int basedivisions;
	//private int heightdivisions;
	
	//current flowing from A to B.
	public Triangle (Vec3 inA, Vec3 inB, Vec3 inC)
	{
		PointA = inA;
		PointB = inB;
		PointC = inC;
		distAB = Vec3.sub(inA, inB).mag();
		distBC = Vec3.sub(inB, inC).mag();
		distCA = Vec3.sub(inC, inA).mag();
		double semiperim = (distAB + distBC + distCA) * 0.5;
		area = Math.sqrt(semiperim*(semiperim - distAB)*(semiperim - distBC)*(semiperim - distCA));
		MidPointAB = Vec3.add(PointA, PointB).mul(0.5);
		normal = Vec3.cross(Vec3.sub(PointB, PointA), Vec3.sub(PointC, PointA)).unitvector();
		CToA = Vec3.sub(PointA, PointC);
		AToB = Vec3.sub(PointB, PointA);
		altitude = 2.0*area/distAB;
	}
	
	float colorr = 0f;
	float colorg = 0.5f;
	float colorb = 1f;
	float colora = 0.5f;
	
	public int putInArray(float[] vertices, float[] colors, float[] normals, int index)
	{
		vertices[index*4 + 0] = (float)PointA.x;
		vertices[index*4 + 1] = (float)PointA.y;
		vertices[index*4 + 2] = (float)PointA.z;
		vertices[index*4 + 3] = 1f;
		colors[index*4 + 0] = colorr;
		colors[index*4 + 1] = colorg;
		colors[index*4 + 2] = colorb;
		colors[index*4 + 3] = colora;
		normals[index*3 + 0] = (float)normal.x;
		normals[index*3 + 1] = (float)normal.y;
		normals[index*3 + 2] = (float)normal.z;
		index++;
		vertices[index*4 + 0] = (float)PointB.x;
		vertices[index*4 + 1] = (float)PointB.y;
		vertices[index*4 + 2] = (float)PointB.z;
		vertices[index*4 + 3] = 1f;
		colors[index*4 + 0] = colorr;
		colors[index*4 + 1] = colorg;
		colors[index*4 + 2] = colorb;
		colors[index*4 + 3] = colora;
		normals[index*3 + 0] = (float)normal.x;
		normals[index*3 + 1] = (float)normal.y;
		normals[index*3 + 2] = (float)normal.z;
		index++;
		vertices[index*4 + 0] = (float)PointC.x;
		vertices[index*4 + 1] = (float)PointC.y;
		vertices[index*4 + 2] = (float)PointC.z;
		vertices[index*4 + 3] = 1f;
		colors[index*4 + 0] = colorr;
		colors[index*4 + 1] = colorg;
		colors[index*4 + 2] = colorb;
		colors[index*4 + 3] = colora;
		normals[index*3 + 0] = (float)normal.x;
		normals[index*3 + 1] = (float)normal.y;
		normals[index*3 + 2] = (float)normal.z;
		index++;
		return index;
	}
	
	//current: Ampls, totalflux: webers/amp
	public double integrateFlux(Physics physics, int basedivisions, int heightdivisions, MagDisplay display, double current) {
		double totalflux = 0;
		double xoffset = 0.5/basedivisions;
		double yoffset = 0.5/heightdivisions;
		double areaconst = (distAB * altitude) / (basedivisions * heightdivisions * heightdivisions);
		//System.out.println("1: " + distAB);
		//System.out.println("2: " + altitude);
		//System.out.println("3: " + basedivisions);
		//System.out.println("4: " + heightdivisions);
		for (int x = 0; x < basedivisions; x++)
		{
			for (int y = 0; y < heightdivisions; y++)
			{
				double percent = ((double)y)/heightdivisions + yoffset;
				
				Vec3 pos = Vec3.add(PointC, Vec3.mul(CToA, percent)).add(Vec3.mul(AToB, percent * (((double)x)/basedivisions + xoffset)));
				Vec3 BField = physics.getBfield(pos, current);
				totalflux += (areaconst * (y + 0.5)) * Vec3.dot(BField, normal);
				display.addFieldVec(Vec3.mul(BField, 10000.0), pos);
			}
		}
		return totalflux;
	}
}
