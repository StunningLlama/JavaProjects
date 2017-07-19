
public class InvalidExpressionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public String error;
	public InvalidExpressionException(String msg)
	{
		error = msg;
	}
}
