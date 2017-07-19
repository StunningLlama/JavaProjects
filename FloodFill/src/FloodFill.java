import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FloodFill extends JFrame implements Runnable {
	BufferedImage input;
	public int[][] grid;
	public static final int XW = 64;
	public static final int YW = 64;
	Display canvas;
	
	public void resetImg() {

		for (int x = 0; x < 64; x++)
			for (int y = 0; y < 64; y++) {
				if ((input.getRGB(x, y)&0xFF)<128)
					grid[x][y] = 1;
				else
					grid[x][y] = 0;

			}
	}
	
	public FloodFill()
	{
		grid = new int[XW][YW];
		input = null;
		try {
		    input = ImageIO.read(new File("C:/Users/Brandon/Desktop/fftest.png"));
		} catch (IOException e) {
		}
		resetImg();
		setSize(500, 500);
		setVisible(true);
		canvas = new Display(this);
		add(canvas);
		Insets i = this.getInsets();
		setPreferredSize(new Dimension(XW*4+i.left+i.right+1, YW*4+i.top+i.bottom+1));
		pack();
	}

	public static void main(String[] args)
	{
		FloodFill instance = new FloodFill();
		instance.run();
	}
	
	// simple call stack based flood fill
	// cpu speed: O(n) 			Memory: Program stack - O(n)
	public void floodfill1(int px, int py, int x, int y) {
		if (grid[x][y] == 1 || grid[x][y] == 2) return;
		//upd();
		grid[x][y] = 2;
		floodfill1(x, y, x+1, y);
		floodfill1(x, y, x-1, y);
		floodfill1(x, y, x, y+1);
		floodfill1(x, y, x, y-1);
		
	}

	// Queue based flood fill, uses integer array to store coords
	// cpu speed: O(n) 			Memory: Heap - O(n)
	public void floodfill2N(int x, int y) {
		Queue<Integer[]> stack = new LinkedList<Integer[]>();
		stack.add(new Integer[] {x, y});
		while (stack.size() > 0) {
			Integer[] cur = stack.poll();
			int xx = cur[0];
			int yy = cur[1];
			//	upd();
			//	if (!(grid[xx][yy] == 0)) continue;
			grid[xx][yy] = 2;
			if (grid[xx+1][yy] == 0) {
				grid[xx+1][yy] = -1;
				stack.add(new Integer[] {xx+1,yy});
			}if (grid[xx-1][yy] == 0) {
				grid[xx-1][yy] = -1;
				stack.add(new Integer[] {xx-1,yy});
			}if (grid[xx][yy+1] == 0) {
				grid[xx][yy+1] = -1;
				stack.add(new Integer[] {xx,yy+1});
			}if (grid[xx][yy-1] == 0) {
				grid[xx][yy-1] = -1;
				stack.add(new Integer[] {xx,yy-1});
			}
		}
	}

	//Queue based, uses two separate queues to store coordinates
	public void floodfill2L(int x, int y) {
		Queue<Integer> stackx = new LinkedList<Integer>();
		Queue<Integer> stacky = new LinkedList<Integer>();
		stackx.add(x);
		stacky.add(y);
		while (stackx.size() > 0) {
			int xx = stackx.poll();
			int yy = stacky.poll();
			//upd();
			grid[xx][yy] = 2;
			if (grid[xx+1][yy] == 0) {
				grid[xx+1][yy] = -1;
				stackx.add(xx+1);
				stacky.add(yy);
			}
			if (grid[xx-1][yy] == 0) {
				grid[xx-1][yy] = -1;
				stackx.add(xx-1);
				stacky.add(yy);
			}
			if (grid[xx][yy+1] == 0) {
				grid[xx][yy+1] = -1;
				stackx.add(xx);
				stacky.add(yy+1);
			}
			if (grid[xx][yy-1] == 0) {
				grid[xx][yy-1] = -1;
				stackx.add(xx);
				stacky.add(yy-1);
			}
		}
	}
	
	//Ring queue based flood fill
	int head = 0;
	int tail = 0;
	public void floodfill2(int x, int y) {
		int size = 1024;
		int max = 1024-1;
		int[] stack = new int[size];
		 head = 0;
		 tail = 0;
		stack[head] = x;
		head++;
		stack[head] = y;
		head++;
		while (head != tail) {
			int xx = stack[tail];
			tail++;
			int yy = stack[tail];
			tail++;
			tail &= max;
			//upd();
			grid[xx][yy] = 2;
			if (grid[xx+1][yy] == 0) {
				grid[xx+1][yy] = -1;
				stack[head] = xx+1;
				head++;
				stack[head] = yy;
				head++;
			}
			head &= max;
			if (grid[xx-1][yy] == 0) {
				grid[xx-1][yy] = -1;
				stack[head] = xx-1;
				head++;
				stack[head] = yy;
				head++;
			}
			head &= max;
			if (grid[xx][yy+1] == 0) {
				grid[xx][yy+1] = -1;
				stack[head] = xx;
				head++;
				stack[head] = yy+1;
				head++;
			}
			head &= max;
			if (grid[xx][yy-1] == 0) {
				grid[xx][yy-1] = -1;
				stack[head] = xx;
				head++;
				stack[head] = yy-1;
				head++;
			}
			head &= max;
		}
	}
	
	// Queue based flood fill, with horizontal filling optimization
	// cpu speed: O(sqrt(n))? 			Memory: Heap - O(n)
	public void floodfill3(int x, int y) {
		Queue<Integer[]> stack = new LinkedList<Integer[]>();
		stack.add(new Integer[] {x, y});
		while (stack.size() > 0) {
			Integer[] cur = stack.poll();
			int xx = cur[0];
			int yy = cur[1];
			if (grid[xx][yy] == 0) {
			//	upd();
				int west = xx;
				int east = xx;
				while (grid[west-1][yy] == 0) west--;
				while (grid[east+1][yy] == 0) east++;
				for (int i = west; i <= east; i++) {
					grid[i][yy] = 2;
					stack.add(new Integer[] {i,yy+1});
					stack.add(new Integer[] {i,yy-1});
				}
			}
		}
	}

	// Call stack based flood fill with horizontal filling optimization
	public int floodfill4(int x, int y) {
		//upd();
		int west = x;
		int east = x;
		while (grid[west-1][y] == 0) west--;
		while (grid[east+1][y] == 0) east++;
		for (int i = west; i <= east; i++) {
			grid[i][y] = 2;
			if (grid[i][y+1] == 0)
				i += floodfill4(i, y+1);
		}
		for (int i = west; i <= east; i++) {
			if (grid[i][y-1] == 0)
				i += floodfill4(i, y-1);
		}
		return east - x;
	}
		// cpu speed: O(n)				Memory: Heap - O(n)

	//Explicit stack based flood fill, coords are stored on same stack
	public void floodfill5AL(int x, int y) {
		ArrayList<Integer[]> stack = new ArrayList<Integer[]>();
		stack.add(new Integer[] {x, y});
		while (stack.size() > 0) {
			Integer[] cur = stack.remove(stack.size()-1);
			int xx = cur[0];
			int yy = cur[1];
				upd();
			//	if (!(grid[xx][yy] == 0)) continue;
			grid[xx][yy] = 2;
			if (grid[xx+1][yy] == 0) {
				grid[xx+1][yy] = 2;
				stack.add(new Integer[] {xx+1,yy});
			}if (grid[xx-1][yy] == 0) {
				grid[xx-1][yy] = 2;
				stack.add(new Integer[] {xx-1,yy});
			}if (grid[xx][yy+1] == 0) {
				grid[xx][yy+1] = 2;
				stack.add(new Integer[] {xx,yy+1});
			}if (grid[xx][yy-1] == 0) {
				grid[xx][yy-1] = 2;
				stack.add(new Integer[] {xx,yy-1});
			}
		}
	}
	
	//Explicit stack based flood fill (stack is fixed size), coords are stored on same stack
	public void floodfill5(int x, int y) {
		int[] stack = new int[64*64*2];
		int ptr = 0;
		stack[ptr] = x;
		ptr++;
		stack[ptr] = y;
		ptr++;
		
		while (ptr > 0) {
			ptr--;
			int yy = stack[ptr];
			ptr--;
			int xx = stack[ptr];
			//	upd();
			//	if (!(grid[xx][yy] == 0)) continue;
			//grid[xx][yy] = 2;
			if (grid[xx+1][yy] == 0) {
				grid[xx+1][yy] = 2;
				stack[ptr] = xx+1;
				ptr++;
				stack[ptr] = yy;
				ptr++;
			}if (grid[xx-1][yy] == 0) {
				grid[xx-1][yy] = 2;
				stack[ptr] = xx-1;
				ptr++;
				stack[ptr] = yy;
				ptr++;
			}if (grid[xx][yy+1] == 0) {
				grid[xx][yy+1] = 2;
				stack[ptr] = xx;
				ptr++;
				stack[ptr] = yy+1;
				ptr++;
			}if (grid[xx][yy-1] == 0) {
				grid[xx][yy-1] = 2;
				stack[ptr] = xx;
				ptr++;
				stack[ptr] = yy-1;
				ptr++;
			}
		}
	}
	
	//stack based flood fill, uses linked list, coords stored in integer array
	public void floodfill6(int x, int y) {
		Stack<Integer[]> stack = new Stack<Integer[]>();
		stack.push(new Integer[] {x, y, 0});
		while (stack.size() > 0) {
			Integer[] cur = stack.peek();
			int xx = cur[0];
			int yy = cur[1];
			if (grid[xx][yy] != 0 || cur[2] == 4) {
				stack.pop();
				continue;
			}
			//upd();
			grid[xx][yy] = 2;
			//System.out.println(stack.peek()[2]);
			cur[2] ++;
			//System.out.println(stack.peek()[2]);
			switch(cur[2]) {
			case 1:
				stack.add(new Integer[] {xx+1,yy,0});
			case 2:
				stack.add(new Integer[] {xx-1,yy,0});
			case 3:
				stack.add(new Integer[] {xx,yy+1,0});
			case 4:
				stack.add(new Integer[] {xx,yy-1,0});
			}

		}
	}
	
	public void upd() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {}
		canvas.repaint();
	}
	
	public void test1() {
		long total = 0;
		for (int i = 0; i < 100; i++) {
			long start = System.nanoTime();
			floodfill1(32, 32, 32, 32);
			total += System.nanoTime() - start;
			resetImg();
		}
		System.out.println("Run 1 " + total/100);
	}

	public void test2() {
		long total = 0;
		for (int i = 0; i < 100; i++) {
			long start = System.nanoTime();
			floodfill2(32, 32);
			total += System.nanoTime() - start;
			resetImg();
		}
		System.out.println("Run 2 " + total/100);
	}
	public void test3() {
		long total = 0;
		for (int i = 0; i < 100; i++) {
			long start = System.nanoTime();
			floodfill3(32, 32);
			total += System.nanoTime() - start;
			resetImg();
		}
		System.out.println("Run 3 " + total/100);
	}
	public void test4() {
		long total = 0;
		for (int i = 0; i < 100; i++) {
			long start = System.nanoTime();
			floodfill4(32, 32);
			total += System.nanoTime() - start;
			resetImg();
		}
		System.out.println("Run 4 " + total/100);
	}
	public void test5() {
		long total = 0;
		for (int i = 0; i < 100; i++) {
			long start = System.nanoTime();
			floodfill5(32, 32);
			total += System.nanoTime() - start;
			resetImg();
		}
		System.out.println("Run 5 " + total/100);
	}
	public void run(){
		
		test1();
		test2();
		//test3();
		//test4();
		test5();
		
		/*while (true)
		{
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {}
			canvas.repaint();
		}*/
	}
}

class Display extends JPanel
{
	Image display;
	FloodFill parent;
	public Display(FloodFill k)
	{
		parent = k;
		display = k.createImage(FloodFill.XW*4+1, FloodFill.YW*4+1);
	}
	@Override
	public void paintComponent(Graphics realgphx)
	{
		Graphics g = display.getGraphics();
		for (int x = 0; x < FloodFill.XW; x++)
		{
			for (int y = 0; y < FloodFill.YW; y++)
			{
				if (parent.grid[x][y] <= 0)
					g.setColor(Color.BLUE);
				else if (parent.grid[x][y] == 1)
					g.setColor(Color.RED);
				else if (parent.grid[x][y] == 2)
					g.setColor(Color.GREEN);
				g.fillRect(x*4, y*4, 4, 4);
				g.setColor(Color.BLACK);
				g.drawRect(x*4, y*4, 4, 4);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString(parent.head + "  " + parent.tail, 10, 10);
		realgphx.drawImage(display, 0, 0, parent);
	}
}