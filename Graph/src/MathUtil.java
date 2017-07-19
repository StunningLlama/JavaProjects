import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class MathUtil {
	private HashMap<String, OperatorData> info;
	private HashMap<String, Double> constants;
	private List<String> rpn;
	private static String regex;
	private static String regexnumber = "[0-9]+(\\.[0-9]+)?([Ee][\\+\\-]?[0-9]+)?";
	
	public MathUtil()
	{
		info = new HashMap<String, OperatorData>();
		constants = new HashMap<String, Double>();
		rpn = new ArrayList<String>();
		MathUtil.initRegex();
	}
	
	public void setConstant(String s, double c) {
		constants.put(s, c);
	}
	
	public Double getConstant(String s) {
		return constants.get(s.toLowerCase());
	}
	
	public void registerOperator(String str, int precedence, int type, int assoc, Op operation)
	{
		info.put(str, new OperatorData(precedence, type, assoc, operation));
	}
	
	private OperatorData getData(String str, boolean ThrowException)
	{
		OperatorData d = info.get(str);
		if (ThrowException && d == null) throw new InvalidExpressionException("Invalid token " + str);
		return d;
	}
	
	private static void initRegex() {
		String num = "[0-9]"; //+(\\.[0-9]+)?([Ee][\\+\\-]?[0-9]+)?
		String alpha = "[A-Za-z]+";
		String punc = "[^a-zA-Z0-9\\.](?<![Ee])";
		String alpha_punc = "(?<=(" + alpha + "))(?=(" + punc + "))";
		String punc_alpha = "(?<=(" + punc + "))(?=(" + alpha + "))";
		String num_punc = "(?<=(" + num + "))(?=(" + punc + "))";
		String punc_num = "(?<=(" + punc + "))(?=(" + num + "))";
		String punc_punc = "(?<=(" + punc + "))(?=(" + punc + "))";
		regex = alpha_punc + "|" + punc_alpha + "|" + num_punc + "|" + punc_num + "|" + punc_punc;
	}
	
	public double function(double x)
	{
		Stack<Double> rpnstack = new Stack<Double>();
		for (String i: this.rpn) {
			//System.out.println(i);
			if (isNumber(i))
				if (i.equals("x"))
					rpnstack.push(x);
				else if (getConstant(i) != null)
					rpnstack.push(constants.get(i));
				else
					rpnstack.push(Double.valueOf(i));
			else {
				OperatorData op = this.getData(i, false);
				if (op.operator instanceof UnaryOp) {
					rpnstack.push(((UnaryOp) op.operator).operate(rpnstack.pop()));
				} else if (op.operator instanceof BinaryOp) {
					rpnstack.push(((BinaryOp) op.operator).operate(rpnstack.pop(), rpnstack.pop()));
				}
			}
		}
		if (!rpnstack.isEmpty())
			return -rpnstack.peek();
		else
			return Double.NaN;
	}
	
	public String[] parseEquation(String s) {
		String[] parts = s.split(regex);
		int l = 0;
		for (int i = 0; i < parts.length; i++)
			if (!parts[i].matches("\\s*")) 
				l++;
		String[] tokens = new String[l];
		l = 0;
		for (int i = 0; i < parts.length; i++)
			if (!parts[i].matches("\\s*")) {
				tokens[l] = parts[i];
				l++;
			}
		for (int i = tokens.length - 1; i >= 0 ; i--) 
			if (tokens[i].equals("-") && (i == 0 || (i > 0 && shouldBeUnary(tokens[i-1]))))
				tokens[i] = "u_sub";
		return tokens;
	}
	
	private boolean shouldBeUnary(String s) {
		if (s.equals("(")) return true;
		if (isNumber(s)) return false;
		if (this.getData(s, false) == null)
			return false;
		return true;
	}
	
	public void generateRPN(Queue<String> tokens)
	{
		List<String> output = new ArrayList<String>();
		Stack<String> operatorstack = new Stack<String>();
		//try
		{
			while(!tokens.isEmpty())
			{
				String token = tokens.poll();
				//System.out.println("Iteration a: " + token);
				if (isNumber(token))
					output.add(token);
				else if (token.equals("("))
					operatorstack.push(token);
				else if (token.equals(")")) {
					while (!operatorstack.peek().equals("("))
						output.add(operatorstack.pop());
					operatorstack.pop();
					if (!operatorstack.isEmpty()) {
						OperatorData o = getData(operatorstack.peek(), false);
					 	if (o != null && o.type == OperatorData.TYPE_FUNC)
					 		output.add(operatorstack.pop());
					}
				}
				else if (token.equals(","))
					while (!operatorstack.peek().equals("("))
						output.add(operatorstack.pop());
				else if (getData(token, true).type == OperatorData.TYPE_FUNC)
					operatorstack.push(token);
				else if (getData(token, true).type == OperatorData.TYPE_OPERATOR) {
					OperatorData first = getData(token, true);
					while (!operatorstack.isEmpty() && getData(operatorstack.peek(), false) != null) {
						OperatorData second = getData(operatorstack.peek(), false);
						if (second.type == OperatorData.TYPE_OPERATOR && ((first.assoc == OperatorData.ASSOC_LEFT && first.precedence <= second.precedence) || (first.assoc == OperatorData.ASSOC_RIGHT && first.precedence < second.precedence)))
							output.add(operatorstack.pop());
						else
							break;
					}
					operatorstack.push(token);
				}
			}
		}
		//catch(EmptyStackException e)
		//{
		//	throw new InvalidExpressionException("Mismatched Parenthesis!");
		//}
		while (!operatorstack.isEmpty())
		{
			//System.out.println("Iteration b");
			if (operatorstack.peek().equals("(") || operatorstack.peek().equals(")"))
				throw new InvalidExpressionException("Mismatched Parenthesis!");
			output.add(operatorstack.pop());
		}
		rpn = output;
	}
	
	private boolean isNumber(String str)
	{
		if (str.matches(regexnumber)) return true;
		if (str.equals("x")) return true;
		if (getConstant(str) != null) return true;
		return false;
	}
}
