import java.awt.Button;import java.awt.Frame;import java.awt.GridLayout;import java.awt.Label;import java.awt.Scrollbar;import java.awt.Toolkit;
import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.WindowEvent;import java.awt.event.WindowListener;
import java.io.IOException;public class m {static long t;static boolean p=false;static long v;static Scrollbar d;public static void main(String a[])throws 
InterruptedException,IOException{Frame f=new Frame();f.setSize(200,150);f.setTitle("Timer");f.setLayout(new GridLayout(5,1));f.addWindowListener
(new WindowListener(){public void windowActivated(WindowEvent w){}public void windowClosed(WindowEvent w){}public void windowClosing(WindowEvent w)
{System.exit(0);}public void windowDeactivated(WindowEvent w){}public void windowDeiconified(WindowEvent w){}public void windowIconified(WindowEvent
a){}public void windowOpened(WindowEvent a){}});Button b=new Button("Reset");Button c=new Button("Pause/Resume");d=new Scrollbar(Scrollbar.
HORIZONTAL,45,50,1,290);Label l=new Label("");Label m=new Label("Minutes: 45");f.add(b);f.add(c);f.add(l);f.add(m);f.add(d);b.addActionListener(
new ActionListener(){public void actionPerformed(ActionEvent a){t=System.currentTimeMillis()+60*d.getValue()*1000;v=t-System.currentTimeMillis();}});c.addActionListener(
new ActionListener(){public void actionPerformed(ActionEvent a){if(!p) v=t-System.currentTimeMillis();p=!p;}});f.setVisible(true);t=System.currentTimeMillis()+60*d.getValue()*1000+
1000;while(true){for(int i=0;i<10;i++){Thread.sleep(100);if(p)t=System.currentTimeMillis()+v;m.setText("Minutes: "+d.getValue()
);l.setText("Time: "+String.valueOf((long)((t-System.currentTimeMillis())/1000.0)));}if (t<System.currentTimeMillis()){Toolkit.getDefaultToolkit()
.beep();if(!f.isAlwaysOnTop())f.setAlwaysOnTop(true);}else if(f.isAlwaysOnTop())f.setAlwaysOnTop(false);}}}