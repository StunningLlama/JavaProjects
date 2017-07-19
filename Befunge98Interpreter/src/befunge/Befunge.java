package befunge;

import java.io.File;
import java.util.*;

public class Befunge {
	List<Pointer> threads;
	Field field;
	StackStack mainstack;
	public Befunge(Vector<Vector<Integer>> init)
	{
		field = new Field(init);
		construct();
	}
	public Befunge(File src)
	{
		field = new Field(src);
		construct();
	}
	public void construct()
	{
		
	}
	void tick()
	{
		
	}
}
