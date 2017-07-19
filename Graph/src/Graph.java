import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;


public class Graph implements Runnable {
	public static int WIDTH = 1600;
	public static int HEIGHT = 900;
	private Display d;
	MathUtil math;
	private long nextframe = 0;
	private long nextsec = 0;
	public int framesdrawn = 0;
	static Graph g;
	public Graph() {
		//WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
		//HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
		math = new MathUtil();
		registerOperators();
		d = new Display(math);
		String expr = "x^2";
		Queue<String> input = new LinkedList<String>();
		for (String s: math.parseEquation(expr)) {
			input.add(s);
		}
		math.generateRPN(input);
	}
	public static void main(String[] args)
	{
		Graph r = new Graph();
		g = r;
		r.run();
	}
	private void registerOperators() {
		math.registerOperator("+", 1, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return a+b;
			}});
		math.registerOperator("-", 1, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return a-b;
			}});
		math.registerOperator("*", 2, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return a*b;
			}});
		math.registerOperator("/", 2, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return a/b;
			}});
		math.registerOperator("^", 3, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_RIGHT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return Math.pow(a, b);
			}});
		math.registerOperator("%", 2, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return a%b;
			}});

		math.registerOperator("<", 0, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return (a<b)? 1: 0;
			}});
		math.registerOperator(">", 0, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return (a>b)? 1: 0;
			}});
		/*math.registerOperator("=", 0, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return (a==b)? 1: 0;
			}});
		math.registerOperator("~", 0, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return (a!=b)? 1: 0;
			}});*/

		math.registerOperator("u_sub", 4, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_RIGHT, new UnaryOp() {
			@Override public double operate(double a) {
				return -a;
			}});
		math.registerOperator("u_add", 4, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_RIGHT, new UnaryOp() {
			@Override public double operate(double a) {
				return a;
			}});
		math.registerOperator("!", 4, OperatorData.TYPE_OPERATOR, OperatorData.ASSOC_LEFT, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.sqrt(2*Math.PI*a)*Math.pow(a/Math.E, a)*(1+1/(12*a)+1/(288*a*a));
			}});

		math.registerOperator("sqrt", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.sqrt(a);
			}});
		math.registerOperator("ln", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.log(a);
			}});
		
		math.registerOperator("sin", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.sin(a);
			}});
		math.registerOperator("cos", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.cos(a);
			}});
		math.registerOperator("tan", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.tan(a);
			}});
		math.registerOperator("asin", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.asin(a);
			}});
		math.registerOperator("acos", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.acos(a);
			}});
		math.registerOperator("atan", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.atan(a);
			}});
		math.registerOperator("sinh", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.sinh(a);
			}});
		math.registerOperator("cosh", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.cosh(a);
			}});
		math.registerOperator("tanh", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.tanh(a);
			}});
		math.registerOperator("log", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return Math.log(a)/Math.log(b);
			}});

		math.registerOperator("abs", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.abs(a);
			}});
		math.registerOperator("min", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return Math.min(a, b);
			}});
		math.registerOperator("max", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new BinaryOp() {
			@Override public double operate(double b, double a) {
				return Math.max(a, b);
			}});
		math.registerOperator("ciel", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.ceil(a);
			}});
		math.registerOperator("floor", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.floor(a);
			}});
		math.registerOperator("round", 0, OperatorData.TYPE_FUNC, OperatorData.ASSOC_NONE, new UnaryOp() {
			@Override public double operate(double a) {
				return Math.round(a);
			}});
		
		math.setConstant("pi", Math.PI);
		math.setConstant("e", Math.E);
	}
	public void run(){
		while (true)
		{
			if (nextframe-System.currentTimeMillis() > 0)
			{
				try {
					Thread.sleep(nextframe-System.currentTimeMillis());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			nextframe = System.currentTimeMillis() + 17;
			if (System.currentTimeMillis() > nextsec) {
				d.canvas.fps = framesdrawn+1;
				if (framesdrawn != 0) {
					d.canvas.computeTime = d.canvas.totalCompute / framesdrawn / 1e6;
					d.canvas.renderTime = d.canvas.totalRender / framesdrawn / 1e6;
				}
				d.canvas.totalCompute = 0;
				d.canvas.totalRender = 0;
				nextsec = System.currentTimeMillis() + 1000;
				framesdrawn = 0;
			}
			startCounter();
			try {
				for (double ix = 0; ix < Graph.WIDTH; ix += GraphCanvas.SAMPLE_RATE)
				{
					d.canvas.values[(int) ix] = math.function((ix-d.canvas.getWidth()/2)/d.canvas.zoom+d.canvas.x);
				}
			} catch (EmptyStackException e) {
				d.canvas.error = "Error: malformed expression";
			}
			d.canvas.totalCompute += endCounter();
			d.canvas.repaint();
			//framesdrawn++;
		}
	}
	public void startCounter() {
		counter = System.nanoTime();
	}
	private long counter = 0;
	public long endCounter() {
		return System.nanoTime() - counter;
	}
}

/*
Function	Description

a+b			Addition
a-b			Subtraction
a*b			Multiply
a/b			Divide
a^b			Exponentation
a%b			Mod
a!			Factorial
sqrt(x)		Square root
ln(x)		Log base e
log(x, y)	Log base y of x
abs(x)		Absolute value
min(x, y)	Minimum of x and y
max(x, y)	Maximum of x and y
ceil(x)		Ceiling of x
floor(x)	Floor of x
round(x)	Round x
sin(x)		Sine
cos(x)		Cosine
tan(x)		Tangent
asin(x)		Inverse sine
acos(x)		Inverse cosine
atan(x)		Inverse tangent
sinh(x)		Hyperbolic sine
cosh(x)		Hyperbolic cosine
tanh(x)		Hyperbolic tangent
pi			3.14159265359
e			2.718...
*/