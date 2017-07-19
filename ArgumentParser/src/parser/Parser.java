package parser;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	private ParserOptions options;
	private OptionMap switches;
	private ArgumentMap args;
	private String rawargs;
	public static String[] Tokenize(String str)
	{
		List<String> finalarr = new ArrayList<String>();
		boolean quotationmode = false;
		String curstr = "";
		Character curc;
		str = str + " ";
		for (int si = 0; si < str.length(); si ++)
		{
			curc = str.charAt(si);
			if (str.charAt(si) == '"')
			{
				quotationmode = !quotationmode;
			}
			else
			{
				if (curc.equals('^'))
				{
					si++;
					curc = str.charAt(si);
					curstr += curc;
				}
				else
				{
					if (curc.equals(' ') & (!quotationmode))
					{
						finalarr.add(curstr);
						curstr = "";
					}
					else
					{
						curstr += curc;
					}
				}
			}
		}
		return (String[]) finalarr.toArray();
	}
	public Argument getArgFromStringAndType(String str, DataType type)
	{
		Argument newarg = new Argument(false, type);
		if (type == DataType.VOID)
		{
			newarg.value = null;
		}
		if (type == DataType.STRING)
		{
			newarg.value = str;
		}
		if (type == DataType.INTEGER)
		{
			try {newarg.value = Integer.valueOf(str);}
			catch (IllegalArgumentException e) {newarg.value = null;}
		}
		if (type == DataType.FLOAT)
		{
			try {newarg.value = Float.valueOf(str);}
			catch (IllegalArgumentException e) {newarg.value = null;}
		}
		if (type == DataType.BOOLEAN)
		{
			if (str.equalsIgnoreCase("true")) { newarg.value = true; }
			else if (str.equalsIgnoreCase("false")) { newarg.value = false; }
			else { newarg.value = null; }
		}
		return newarg;
	}
	public Parser(ParserOptions InitOptions, String[] InitialArgs)
	{
		rawargs = "";
		options = InitOptions;
		StringBuilder text = new StringBuilder("");
		for (int i = 0; i < InitialArgs.length; i++)
		{
			text.append(InitialArgs[i]);
			text.append(" ");
		}
		rawargs = text.substring(0, text.length() - 2);
		args = new ArgumentMap();
		switches = new OptionMap();
	}
	public void Parse()
	{
		String[] Tokens = Parser.Tokenize(rawargs);
		int argid = 0;
		DataType type;
		for (int cs = 0; cs < Tokens.length; cs ++)
		{
			if (Tokens[cs].startsWith("-"))
			{
				type = this.options.switches.getArgument(Tokens[cs]).getDataType();
				if (type == null || type == DataType.VOID)
				{
					this.switches.args.put(Tokens[cs], null);
				}
				else
				{
					Argument newarg = this.getArgFromStringAndType(Tokens[cs+1], type);
					this.switches.args.put(Tokens[cs], newarg);
					cs ++;
				}
			}
			else
			{
				type = this.options.arguments.getArgument(argid).getDataType();
				Argument newarg = this.getArgFromStringAndType(Tokens[cs+1], type);
				this.args.args.set(argid, newarg);
				argid ++;
			}
		}
	}
	public OptionMap getSwitches()
	{
		return this.switches;
	}
	public ArgumentMap getArguments()
	{
		return this.args;
	}
	public ParserOptions getOptions()
	{
		return this.options;
	}
}
