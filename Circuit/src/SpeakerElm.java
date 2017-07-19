import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.StringTokenizer;

class SpeakerElm extends CircuitElm {
	double resistance;
	public SpeakerElm(int xx, int yy)
	{
		super(xx, yy);
		resistance = 100;
	}

	public SpeakerElm(int xa, int ya, int xb, int yb, int f,
		      StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    resistance = new Double(st.nextToken()).doubleValue();
	}
	int getDumpType() { return 299; }
	String dump() {
	    return super.dump() + " " + resistance;
	}
	void calculateCurrent() {
	    current = (volts[0]-volts[1])/resistance;
	    //System.out.print(this + " res current set to " + current + "\n");
	}
	
	Point ps3, ps4;
	void setPoints() {
	    super.setPoints();
	    calcLeads(32);
	    ps3 = new Point();
	    ps4 = new Point();
	}
	
	void draw(Graphics g) {
	    int segments = 16;
	    int i;
	    int ox = 0;
	    int hs = sim.euroResistorCheckItem.getState() ? 6 : 8;
	    double v1 = volts[0];
	    double v2 = volts[1];
	    setBbox(point1, point2, hs);
	    draw2Leads(g);
	    setPowerColor(g, true);
	    double segf = 1./segments;
		// draw rectangle
		setVoltageColor(g, v1);
		interpPoint2(lead1, lead2, ps1, ps2, 0, hs);
		drawThickLine(g, ps1, ps2);
		for (i = 0; i != segments; i++) {
		    double v = v1+(v2-v1)*i/segments;
		    setVoltageColor(g, v);
		    interpPoint2(lead1, lead2, ps1, ps2, i*segf, hs);
		    interpPoint2(lead1, lead2, ps3, ps4, (i+1)*segf, hs);
		    drawThickLine(g, ps1, ps3);
		    drawThickLine(g, ps2, ps4);
		}
		interpPoint2(lead1, lead2, ps1, ps2, 1, hs);
		drawThickLine(g, ps1, ps2);
	    if (sim.showValuesCheckItem.getState()) {
		String s = getShortUnitText(resistance, "");
		drawValues(g, s, hs);
	    }
	    doDots(g);
	    drawPosts(g);
	}

	void stamp() {
	    sim.stampResistor(nodes[0], nodes[1], resistance);
	}
	void getInfo(String arr[]) {
	    arr[0] = "speaker";
	    getBasicInfo(arr);
	    arr[3] = "R = " + getUnitText(resistance, sim.ohmString);
	    arr[4] = "P = " + getUnitText(getPower(), "W");
	}
	public EditInfo getEditInfo(int n) {
	    // ohmString doesn't work here on linux
	    if (n == 0)
		return new EditInfo("Resistance (ohms)", resistance, 0, 0);
	    return null;
	}
	public void setEditValue(int n, EditInfo ei) {
	    if (ei.value > 0)
	        resistance = ei.value;
	}
	
	public void updateAudio() {
		//System.out.println(volts[0]-volts[1]);
	   AudioOut.INSTANCE.putVal(volts[0]-volts[1]);
	}
}
