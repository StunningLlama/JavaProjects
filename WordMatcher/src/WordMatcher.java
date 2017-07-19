import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;




public class WordMatcher extends Frame {
	TextField input;
	TextArea output;
	Button findbutton;
	List<String> dictionary = new ArrayList<String>();
	public static void main(String[] args) {
		new WordMatcher();
	}
	public WordMatcher() {
		this.setSize(200, 200);
		input = new TextField();
		output = new TextArea();
		output.setEditable(false);
		findbutton = new Button("Find Matches");
		Panel p = new Panel(new BorderLayout());
		p.add(input, BorderLayout.CENTER);
		p.add(findbutton, BorderLayout.EAST);
		this.add(output, BorderLayout.CENTER);
		this.add(p, BorderLayout.NORTH);
		this.setVisible(true);
		//Create a file chooser
		JFileChooser fc = new JFileChooser();
		//In response to a button click:
		findbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				output.setText("");
				StringBuilder matches = new StringBuilder();
				String pattern = input.getText();
				boolean[] isUsed = new boolean[26];
				for (int i = 0; i < 26; i++)
					isUsed[i] = false;
				for (char c: pattern.toCharArray())
					if (c != '_')
						isUsed[c-'a'] = true;
				for (String word: dictionary) {
					if (word.length() == pattern.length()) {
						boolean isMatch = true;
						for (int i = 0; i < pattern.length(); i++) {
							if ((pattern.charAt(i) != '_' && word.charAt(i) != pattern.charAt(i)) || (pattern.charAt(i) == '_' && isUsed[word.charAt(i)-'a'])) {
								isMatch = false;
								break;
							}
						}
						if (isMatch) {
							matches.append(word + "\n");
						}
					}
				}
				output.setText(matches.toString());
			}
			
		});
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				dictionary = Files.readAllLines(fc.getSelectedFile().toPath(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
