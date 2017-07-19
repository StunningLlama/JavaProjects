import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class Main {
	public static void main(String[] args) throws UnsupportedFlavorException, IOException {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		for (DataFlavor f: c.getAvailableDataFlavors()) {
			System.out.println(f.getHumanPresentableName());
			System.out.println(c.getData(f).toString());
		}
	}
}
