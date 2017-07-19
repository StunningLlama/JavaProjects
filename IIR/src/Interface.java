import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import java.awt.Font;

public class Interface extends JFrame {

	private JPanel contentPane;
	private IIR parent;
	public JLabel l_input;
	public JLabel l_output;
	public JSpinner i_freq_low;
	public JSpinner i_freq_high;
	public JSpinner i_img_w;
	public JSpinner i_img_h;
	public JSpinner i_q;
	public JSpinner i_prescision;
	public JCheckBox chckbxLogarithmic;
	public JSpinner i_intensity;
	public JSpinner i_gamma;
	public JProgressBar progressBar;
	/**
	 * Create the frame.
	 */
	public Interface(IIR instance) {
		setResizable(false);
		final Interface settings = this;
		final FileFilter ffin = new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				String name = f.getName().toLowerCase();
				return (name.endsWith(".wav") || name.endsWith(".mp3") || name.endsWith(".flac") || name.endsWith(".ogg") || name.endsWith(".wma"));
			}

			@Override
			public String getDescription() {
				return "All Audio Files";
			}
			
		};
		final FileFilter ffout = new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				String name = f.getName().toLowerCase();
				return (name.endsWith(".png"));
			}

			@Override
			public String getDescription() {
				return "PNG";
			}
			
		};
		parent = instance;
		setTitle("Spectrogram Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 398);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Frequency Lower Bound");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setBounds(10, 26, 142, 17);
		contentPane.add(lblNewLabel);
		
		i_freq_low = new JSpinner(new SpinnerNumberModel(27.5, 0.0, 100000.0, 0.1));
		i_freq_low.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_freq_low.setBounds(167, 26, 98, 20);
		contentPane.add(i_freq_low);
		
		i_freq_high = new JSpinner(new SpinnerNumberModel(20000, 0.0, 100000.0, 0.1));
		i_freq_high.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_freq_high.setBounds(167, 53, 98, 20);
		contentPane.add(i_freq_high);
		
		JLabel lblNewLabel_1 = new JLabel("Frequency Upper Bound");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_1.setBounds(10, 52, 142, 19);
		contentPane.add(lblNewLabel_1);
		
		chckbxLogarithmic = new JCheckBox("Logarithmic");
		chckbxLogarithmic.setSelected(true);
		chckbxLogarithmic.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		chckbxLogarithmic.setBounds(381, 80, 97, 23);
		contentPane.add(chckbxLogarithmic);
		
		JLabel lblNewLabel_2 = new JLabel("Width of output image");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(26, 95, 126, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Height of output image");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(26, 117, 126, 17);
		contentPane.add(lblNewLabel_3);
		
		i_img_w = new JSpinner(new SpinnerNumberModel(2000, 0, 20000, 1));
		i_img_w.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_img_w.setBounds(167, 92, 98, 20);
		contentPane.add(i_img_w);
		
		i_img_h = new JSpinner(new SpinnerNumberModel(1000, 0, 20000, 1));
		i_img_h.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_img_h.setBounds(167, 117, 98, 20);
		contentPane.add(i_img_h);
		
		l_input = new JLabel("File:");
		l_input.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		l_input.setBounds(167, 204, 119, 14);
		contentPane.add(l_input);
		
		JButton b_select_in = new JButton("Select Input File");
		b_select_in.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		b_select_in.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(ffin);
				int returnVal = fc.showOpenDialog(settings);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            parent.INPUT_FILE = fc.getSelectedFile();
		            settings.l_input.setText("File: " + parent.INPUT_FILE.getName());
				}
			}
		});
		b_select_in.setBounds(26, 200, 132, 23);
		contentPane.add(b_select_in);
		
		JButton b_select_out = new JButton("Select Output File");
		b_select_out.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		b_select_out.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(ffout);
				int returnVal = fc.showOpenDialog(settings);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            parent.OUTPUT_FILE = fc.getSelectedFile();
		            settings.l_output.setText("File: " + parent.OUTPUT_FILE.getName());
				}
			}
		});
		b_select_out.setBounds(26, 234, 132, 23);
		contentPane.add(b_select_out);
		
		l_output = new JLabel("File:");
		l_output.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		l_output.setBounds(167, 238, 119, 14);
		contentPane.add(l_output);
		
		JButton b_generate_spec = new JButton("Start Generating Spectrogram");
		b_generate_spec.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		b_generate_spec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settings.parent.convertinput();
				settings.parent.commence();
			}
		});
		b_generate_spec.setBounds(310, 200, 168, 23);
		contentPane.add(b_generate_spec);
		
		JButton b_output_img = new JButton("Output Image to File");
		b_output_img.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		b_output_img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeImageToFile();
			}
		});
		b_output_img.setBounds(310, 234, 168, 23);
		contentPane.add(b_output_img);
		
		JLabel lblIntensity = new JLabel("Brightness");
		lblIntensity.setHorizontalAlignment(SwingConstants.TRAILING);
		lblIntensity.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblIntensity.setBounds(33, 287, 69, 19);
		contentPane.add(lblIntensity);
		
		i_intensity = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10000.0, 0.1));
		i_intensity.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_intensity.setBounds(112, 288, 74, 20);
		contentPane.add(i_intensity);
		
		JLabel lblGamma = new JLabel("Gamma");
		lblGamma.setHorizontalAlignment(SwingConstants.TRAILING);
		lblGamma.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblGamma.setBounds(56, 314, 46, 14);
		contentPane.add(lblGamma);
		
		i_gamma = new JSpinner(new SpinnerNumberModel(1.0, 0.5, 2.0, 0.01));
		i_gamma.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_gamma.setBounds(112, 313, 74, 20);
		contentPane.add(i_gamma);
		
		JLabel lblQValue = new JLabel("Q value");
		lblQValue.setHorizontalAlignment(SwingConstants.TRAILING);
		lblQValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblQValue.setBounds(325, 29, 46, 14);
		contentPane.add(lblQValue);
		
		i_q = new JSpinner(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.0001));
		i_q.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_q.setBounds(381, 26, 98, 20);
		contentPane.add(i_q);
		
		JLabel lblPrescision = new JLabel("Prescision");
		lblPrescision.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPrescision.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblPrescision.setBounds(309, 54, 62, 14);
		contentPane.add(lblPrescision);
		
		i_prescision = new JSpinner(new SpinnerNumberModel(1000.0, 0.0, 5000.0, 1.0));
		i_prescision.setFont(new Font("Consolas", Font.PLAIN, 11));
		i_prescision.setBounds(381, 53, 98, 20);
		contentPane.add(i_prescision);
		
		JButton b_update_img = new JButton("Update Output Image");
		b_update_img.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		b_update_img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeToImg();
			}
		});
		b_update_img.setBounds(309, 287, 157, 23);
		contentPane.add(b_update_img);
		
		progressBar = new JProgressBar(0, 10000);
		progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		progressBar.setStringPainted(true);
		progressBar.setBounds(26, 159, 452, 14);
		contentPane.add(progressBar);
	}
}
