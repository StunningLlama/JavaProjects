
public class OperatorData {
	public static int TYPE_OPERATOR = 0;
	public static int TYPE_FUNC = 1;
	public static int ASSOC_NONE = -1;
	public static int ASSOC_LEFT = 0;
	public static int ASSOC_RIGHT = 1;
	public int precedence;
	public int type;
	public int assoc;
	public Op operator;
	public OperatorData(int p, int t, int a, Op o)
	{
		precedence = p;
		type = t;
		assoc = a;
		operator = o;
	}
}
