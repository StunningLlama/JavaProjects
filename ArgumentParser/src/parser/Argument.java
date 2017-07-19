package parser;

public class Argument extends HasValue {
	protected boolean optional;
	protected DataType type;
	protected Argument(boolean Coptional, DataType Ctype)
	{
		optional = Coptional;
		type = Ctype;
	}
	@Override
	protected Object getValue() {
		return this.value;
	}
	@Override
	protected void setValue(Object val) {
		this.value = val;
		if (val == null)
		{
			this.type = DataType.VOID;
			return;
		}
		if (val instanceof String)
		{
			this.type = DataType.STRING;
		}
		if (val instanceof Integer)
		{
			this.type = DataType.INTEGER;
		}
		if (val instanceof Float)
		{
			this.type = DataType.FLOAT;
		}
		if (val instanceof Boolean)
		{
			this.type = DataType.BOOLEAN;
		}
	}
	public DataType getDataType()
	{
		return this.type;
	}
	public String getString()
	{
		if (this.getValue() instanceof String)
		{
			return (String) this.getValue();
		}
		return null;
	}
	public Integer getInteger()
	{
		if (this.getValue() instanceof Integer)
		{
			return (Integer) this.getValue();
		}
		return null;
	}
	public Float getFloat()
	{
		if (this.getValue() instanceof Float)
		{
			return (Float) this.getValue();
		}
		return null;
	}
	public Boolean getBoolean()
	{
		if (this.getValue() instanceof Boolean)
		{
			return (Boolean) this.getValue();
		}
		return null;
	}
	
}
