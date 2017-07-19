
public class Test {
	
	public static int SIZE = 100000;
	public static void main(String[] args) {
		
		float[] arrayA = new float[SIZE];
		float[] arrayB = new float[SIZE];
		double[] arrayC = new double[SIZE];
		double[] arrayD = new double[SIZE];
		
		for (int i = 0; i < SIZE; i++) {
			arrayA[i] = (float)Math.random();
			arrayB[i] = (float)Math.random();
			arrayC[i] = Math.random();
			arrayD[i] = Math.random();
		}

		float test;
		double test2;
		long starttime;
		long elapsedtime;
		
		/*starttime = System.nanoTime();
		for (int i = 0; i < SIZE; i++) {
			test2 = arrayC[i] * arrayD[i] - arrayC[i];
		}
		elapsedtime = (System.nanoTime() - starttime);
		System.out.println("TestB took " + elapsedtime + " nanos.");*/
		

		starttime = System.nanoTime();
		for (int i = 0; i < SIZE; i++) {
			test = arrayA[i] * arrayB[i] - arrayA[i];
		}
		elapsedtime = (System.nanoTime() - starttime);
		System.out.println("TestA took " + elapsedtime + " nanos.");
	}
}
