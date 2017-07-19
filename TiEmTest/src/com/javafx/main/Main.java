package com.javafx.main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class Main {
	public static void main(String[] args) {

		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		List<String> arguments = runtimeMxBean.getInputArguments();

		String s = "EXEC ARGS: ";
		for (String i: args) {
			s += i;
			s += ", ";
		}
		s += "JVM ARGS: ";
		for (String i: arguments) {
			s += i;
			s += ", ";
		}
			StringSelection stringSelection = new StringSelection(s);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}
}
