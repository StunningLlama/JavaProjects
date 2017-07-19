
public class Physics {
	
	public Physics(Camera cami) {
		cam = cami;
		eqn = new Coil(cami, 400);
		setParams(0, eqn.getDomain(), 400);
		dlength = new double[samples];
		for (int i = 0; i < samples; i++) {
			dlength[i] = Vec3.sub(eqn.func(convertIndexToNum(i)), eqn.func(convertIndexToNum((i+1)%samples))).mag();
		}
	}
	
	ParametricEquation eqn;
	double min;
	double max;
	double interval;
	int samples;
	//int triangleindex = 0;
	Camera cam;
	
	Triangle[] area = new Triangle[1000];
	double[] dlength;
	int swapindex = 0;
	
	public void setParams(double mini, double maxi, int samplesi)
	{
		min = mini;
		max = maxi;
		samples = samplesi;
		interval = (max-min)/(samples-1.0);
	}
	
	public double convertIndexToNum(int index)
	{
		return min + (max-min)*(index/(samples-1.0));
	}
	
	int currentpoint = 0;
	public int newtriangle(float[] vertices, float[] colors, float[] normals, int triangleindex)
	{
		area[triangleindex/3] = new Triangle(eqn.func(convertIndexToNum(currentpoint)), eqn.func(convertIndexToNum(currentpoint + 1)), eqn.centerpoints[eqn.swappointers[swapindex]]);
		triangleindex = area[triangleindex/3].putInArray(vertices, colors, normals, triangleindex);
		if (eqn.swapindexes[swapindex] == currentpoint+1) {
			System.out.println("SWAP");
			area[triangleindex/3] = new Triangle(eqn.centerpoints[eqn.swappointers[swapindex + 1]], eqn.centerpoints[eqn.swappointers[swapindex]], eqn.func(convertIndexToNum(currentpoint+1)));
			triangleindex = area[triangleindex/3].putInArray(vertices, colors, normals, triangleindex);
			swapindex++;
			if (swapindex >= eqn.totalswappoints)
				swapindex = 0;
		}
		currentpoint++;
		return triangleindex;
	}
	
	public int fillCoilData(float[] coilvertices, float[] coilcolors) {
		int index = 0;
		for (int i = 0; i < samples; i ++)
		{
			Vec3 point = eqn.func(convertIndexToNum(i));
			coilvertices[index*4 + 0] = (float)point.x;
			coilvertices[index*4 + 1] = (float)point.y;
			coilvertices[index*4 + 2] = (float)point.z;
			coilvertices[index*4 + 3] = 1f;
			coilcolors[index*4 + 0] = (float)(0.5 * ((double)i)/samples + 0.25);
			coilcolors[index*4 + 1] = 0f;
			coilcolors[index*4 + 2] = 0f;
			coilcolors[index*4 + 3] = 1f;
			index++;
		}
		return index;
	}

	public Vec3 getBfield(double x, double y, double z, double current)
	{
		Vec3 pos = new Vec3(x, y, z);
		return getBfield(pos, current);
	}
	
	public static final double mconst = 0.0000001;
	
	public Vec3 getBfield(Vec3 pos, double current) //current:Amps
	{
		Vec3 total = new Vec3();
		for (int i = 0; i < samples; i ++)
		{
			Vec3 point = eqn.func(convertIndexToNum(i));
			Vec3 velocity = Vec3.unitvector(Vec3.sub(eqn.func(convertIndexToNum(i)+interval), point)).mul(current);
			Vec3 displacement = Vec3.sub(pos, point);
			double mag = displacement.mag();
			total.add(Vec3.mul(Vec3.cross(velocity, displacement), dlength[i]/(mag*mag*mag)));
		}
		return total.mul(mconst);
	}
	
	public double getInductance(int triangles, MagDisplay display, double current) {
		double total = 0;
		for (int i = 0; i < triangles; i++) {
			double term = area[i].integrateFlux(this, 1, 10, display, current);
			total += term;
		}
		display.willupdatefield = true;
		return total/current;
	}
	

	public static String convertToUnits(double number, String unit) {
		String output = "null";
		if (number < 1E-12) {
			output = String.valueOf(number*1E15) + "p" + unit;
		}
		else if (number < 1E-9) {
			output = String.valueOf(number*1E12) + "n" + unit;
		}
		else if (number < 1E-6) {
			output = String.valueOf(number*1E9) + "u" + unit;
		}
		else if (number < 1E-3) {
			output = String.valueOf(number*1E6) + "\u00b5" + unit;
		}
		else if (number < 1) {
			output = String.valueOf(number*1E3) + "m" + unit;
		}
		else if (number < 1E3) {
			output = String.valueOf(number) + "" + unit;
		}
		else if (number < 1E6) {
			output = String.valueOf(number/1E3) + "k" + unit;
		}
		else if (number < 1E9) {
			output = String.valueOf(number/1E6) + "M" + unit;
		}
		else if (number < 1E12) {
			output = String.valueOf(number/1E9) + "G" + unit;
		}
		return output;
	}
}
