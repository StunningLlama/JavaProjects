package parser;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMap {
	public List<Argument> args;

	public ArgumentMap()
	{
		args = new ArrayList<Argument>();
	}
	public Argument getArgument(int index)
	{
		return args.get(index);
	}
}
