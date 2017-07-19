import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;


public class CircleClock extends Applet {

		Choice color;
		Choice basecolor;
		Choice pattern;
		Stack<Layer> layers;
		String nbt;
		TextArea display;
		
		public void init()
		{
			layers = new Stack<Layer>();
			display = new TextArea("", 1, 1, TextArea.SCROLLBARS_VERTICAL_ONLY);
			display.setEditable(false);
			this.setSize(500, 400);
			this.setLayout(new BorderLayout());
			Panel top = new Panel(new GridLayout(1, 4));
			Panel bottom = new Panel(new GridLayout(1, 3));
			color = new Choice();
			color.add("Black");
			color.add("Red");
			color.add("Green");
			color.add("Brown");
			color.add("Blue");
			color.add("Purple");
			color.add("Cyan");
			color.add("Light gray");
			color.add("Gray");
			color.add("Pink");
			color.add("Lime");
			color.add("Yellow");
			color.add("Light blue");
			color.add("Magenta");
			color.add("Orange");
			color.add("White");
			
			basecolor = new Choice();
			basecolor.add("Black");
			basecolor.add("Red");
			basecolor.add("Green");
			basecolor.add("Brown");
			basecolor.add("Blue");
			basecolor.add("Purple");
			basecolor.add("Cyan");
			basecolor.add("Light gray");
			basecolor.add("Gray");
			basecolor.add("Pink");
			basecolor.add("Lime");
			basecolor.add("Yellow");
			basecolor.add("Light blue");
			basecolor.add("Magenta");
			basecolor.add("Orange");
			basecolor.add("White");
			
			pattern = new Choice();
			pattern.add("Gradient");
			pattern.add("Top left square");
			pattern.add("Top right square");
			pattern.add("Lower left square");
			pattern.add("Lower right square");
			pattern.add("Left stripe");
			pattern.add("Right stripe");
			pattern.add("Top stripe");
			pattern.add("Bottom stripe");
			pattern.add("Rhombus");
			pattern.add("Circle");
			pattern.add("Horizontal half");
			pattern.add("Vertical half");
			pattern.add("Center stripe");
			pattern.add("Middle stripe");
			pattern.add("Cross");
			pattern.add("Left diagonal");
			pattern.add("Right diagonal");
			pattern.add("Top saw");
			pattern.add("Bottom saw");
			pattern.add("Bottom triangle");
			pattern.add("Top triangle");
			pattern.add("Down right diagonal");
			pattern.add("Down left diagonal");
			pattern.add("Small stripes");
			pattern.add("Straight Cross");
			pattern.add("Border");

			pattern.add("Brick Pattern");
			pattern.add("Creeper Face");
			pattern.add("Flower");
			pattern.add("Skull and Bones");
			pattern.add("Mojang Logo");
			pattern.add("Curly Border");
			
			Button tmpa = new Button("Add Layer");
			tmpa.addActionListener(new ButtonCaller(this, 1));
			Button tmpb = new Button("Delete Layer");
			tmpb.addActionListener(new ButtonCaller(this, 2));
			bottom.add(new Label("Banner color ->"));
			bottom.add(basecolor);
			Button tmpc = new Button("Set banner color");
			tmpc.addActionListener(new ButtonCaller(this, 3));
			bottom.add(tmpc);
			top.add(color);
			top.add(pattern);
			top.add(tmpa);
			top.add(tmpb);
			this.add(top, BorderLayout.NORTH);
			this.add(bottom, BorderLayout.SOUTH);
			this.add(display, BorderLayout.CENTER);
			this.setVisible(true);
			this.GenerateNBT();
		}
		
		public void onButton(int id)
		{
			if (id == 1)
			{
				this.layers.push(new Layer(this.color.getSelectedIndex(), Layer.ToPattern(this.pattern.getSelectedIndex())));
			}
			if (id == 2)
			{
				if (this.layers.size() != 0)
					this.layers.pop();
			}
			this.GenerateNBT();
		}
		
		public void GenerateNBT()
		{
			StringBuilder tag = new StringBuilder("{BlockEntityTag:{Base:" + this.basecolor.getSelectedIndex() + ",Patterns:[");
			for (Layer l : this.layers)
			{
				tag.append("{Color:" + String.valueOf(l.color) + ",Pattern:" + l.pattern + "},"); 
			}
			if (this.layers.size() != 0)
				tag = new StringBuilder(tag.substring(0, tag.length() - 1));
			tag.append("]}}");
			nbt = tag.toString();
			display.setText(nbt);
		}
	}

	class Layer
	{
		int color;
		String pattern;

		public Layer(int c, String p)
		{
			color = c;
			pattern = p;
		}
		
		public static String ToPattern(int i)
		{
			switch(i)
			{
			 case 0: return "gra";
			 case 1: return "tl";
			 case 2: return "tr";
			 case 3: return "bl";
			 case 4: return "br";
			 case 5: return "ls";
			 case 6: return "rs";
			 case 7: return "ts";
			 case 8: return "bs";
			 case 9: return "mr";
			case 10: return "mc";
			case 11: return "hh";
			case 12: return "vh";
			case 13: return "cs";
			case 14: return "ms";
			case 15: return "cr";
			case 16: return "ld";
			case 17: return "rd";
			case 18: return "tts";
			case 19: return "bts";
			case 20: return "bt";
			case 21: return "tt";
			case 22: return "drs";
			case 23: return "dls";
			case 24: return "ss";
			case 25: return "sc";
			case 26: return "bo";
			case 27: return "bri";
			case 28: return "cre";
			case 29: return "flo";
			case 30: return "sku";
			case 31: return "moj";
			case 32: return "cbo";
			default: return "?";
			}
		}
	}

	class ButtonCaller implements ActionListener
	{
		CircleClock t; int id;
		public ButtonCaller(CircleClock a, int b) { t=a; id=b; }
		public void actionPerformed(ActionEvent arg0) {t.onButton(id);}
	}
