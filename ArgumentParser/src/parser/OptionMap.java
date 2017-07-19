package parser;

import java.util.HashMap;

public class OptionMap {
	public HashMap<String, Argument> args;

	public Argument getArgument(String index)
	{
		return args.get(index);
	}
	public OptionMap()
	{
		args = new HashMap<String, Argument>();
	}
}
