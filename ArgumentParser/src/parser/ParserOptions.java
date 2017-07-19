package parser;

public class ParserOptions {
	protected OptionMap switches;
	protected ArgumentMap arguments;
	public void addSwitch(String name, DataType type, boolean optional)
	{
		switches.args.put(name, new Argument(optional, type));
	}
	public void addArgument(DataType type, boolean optional)
	{
		arguments.args.add(new Argument(optional, type));
	}
}
