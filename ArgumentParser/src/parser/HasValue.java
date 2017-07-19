package parser;

public abstract class HasValue {
	protected DataType type;
	protected Object value;
	protected abstract Object getValue();
	protected abstract void setValue(Object val);
}
