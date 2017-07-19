import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollBar;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JSpinner;
import java.awt.Panel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerModel;

public class Options extends JFrame {

	protected JPanel contentPane;
	protected JTextField trigtext;
	protected JComboBox inputList;
	protected JComboBox inputList2;
	protected JComboBox vdivunits;
	protected JComboBox tdivunits;
	protected JComboBox oscmode;
	protected JCheckBox chckbxUseLrChannels;
	protected JScrollBar trigvalue;
	protected JSpinner tdivtext;
	protected JSpinner vdivtext;
	protected JSpinner decayRate;
	protected JSpinner hdivtext;
	protected JComboBox hdivunits;
	protected JSpinner phosphorStr;
	protected JCheckBox chkInv;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Options frame = new Options();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public Options(final Oscilloscope osc) {
		setVisible(true);
		setTitle("Options window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 494, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel hdivlabel = new JLabel("Time Division");
		hdivlabel.setHorizontalAlignment(SwingConstants.TRAILING);
		hdivlabel.setBounds(10, 11, 92, 22);
		contentPane.add(hdivlabel);
		
		tdivunits = new JComboBox();
		tdivunits.setBounds(206, 11, 47, 22);
		tdivunits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osc.updateOptions();
			}
		});
		tdivunits.setModel(new DefaultComboBoxModel(new String[] {"us", "ms", "s"}));
		tdivunits.setSelectedIndex(1);
		contentPane.add(tdivunits);
		
		JLabel lblNewLabel = new JLabel("Vertical Division");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setBounds(10, 44, 92, 22);
		contentPane.add(lblNewLabel);
		
		vdivunits = new JComboBox();
		vdivunits.setBounds(206, 44, 47, 22);
		vdivunits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osc.updateOptions();
			}
		});
		vdivunits.setModel(new DefaultComboBoxModel(new String[] {"mv", "v", "kv"}));
		vdivunits.setSelectedIndex(1);
		contentPane.add(vdivunits);
		
		JLabel lblNewLabel_1 = new JLabel("Trigger Voltage");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_1.setBounds(254, 11, 114, 22);
		contentPane.add(lblNewLabel_1);
		
		trigtext = new JTextField();
		trigtext.setBounds(383, 11, 85, 22);
		trigtext.setText("0");
		trigtext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trigvalue.setValue((int)(Double.valueOf(trigtext.getText()) * 100));
				osc.updateOptions();
			}
		});
		contentPane.add(trigtext);
		trigtext.setColumns(10);
		
		trigvalue = new JScrollBar();
		trigvalue.setBounds(289, 44, 179, 22);
		trigvalue.setMinimum(-100);
		trigvalue.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				trigtext.setText(String.valueOf(trigvalue.getValue()*0.01));
				osc.updateOptions();
			}
		});
		trigvalue.setOrientation(JScrollBar.HORIZONTAL);
		contentPane.add(trigvalue);
		
		JLabel lblSource = new JLabel("Source");
		lblSource.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSource.setBounds(10, 258, 67, 22);
		contentPane.add(lblSource);
		
		inputList = new JComboBox();
		inputList.setBounds(87, 258, 166, 22);
		contentPane.add(inputList);
		
		inputList2 = new JComboBox();
		inputList2.setBounds(263, 258, 166, 22);
		contentPane.add(inputList2);
		
		JLabel lblOscilloscopeMode = new JLabel("Oscilloscope Mode");
		lblOscilloscopeMode.setHorizontalAlignment(SwingConstants.TRAILING);
		lblOscilloscopeMode.setBounds(10, 225, 114, 22);
		contentPane.add(lblOscilloscopeMode);
		
		oscmode = new JComboBox();
		oscmode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				osc.updateOptions();
			}
		});
		oscmode.setBounds(134, 225, 119, 22);
		oscmode.setModel(new DefaultComboBoxModel(new String[] {"Entire Waveform", "Trigger", "X-Y"}));
		oscmode.setSelectedIndex(0);
		contentPane.add(oscmode);
		
		chckbxUseLrChannels = new JCheckBox("Use L/R Channels");
		chckbxUseLrChannels.setBounds(263, 225, 166, 22);
		contentPane.add(chckbxUseLrChannels);
		
		chkInv = new JCheckBox("Trigger Inverted");
		chkInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				osc.updateOptions();
			}
		});
		chkInv.setBounds(289, 78, 134, 23);
		contentPane.add(chkInv);
		
		JButton btnNewButton = new JButton("Select Background Color");
		btnNewButton.setBounds(30, 132, 159, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Select Light Color");
		btnNewButton_1.setBounds(30, 166, 159, 23);
		contentPane.add(btnNewButton_1);
		
		tdivtext = new JSpinner(new SpinnerNumberModel(25.0, 0.0, 1000.0, 0.1));
		tdivtext.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				osc.updateOptions();
			}
		});
		tdivtext.setBounds(112, 12, 86, 20);
		contentPane.add(tdivtext);
		
		vdivtext = new JSpinner(new SpinnerNumberModel(2.0, 0.0, 10.0, 0.1));
		vdivtext.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				osc.updateOptions();
			}
		});
		vdivtext.setBounds(112, 45, 86, 20);
		contentPane.add(vdivtext);
		
		JLabel lblDecayRate = new JLabel("Decay Rate (A)");
		lblDecayRate.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDecayRate.setBounds(254, 155, 92, 14);
		contentPane.add(lblDecayRate);
		
		decayRate = new JSpinner(new SpinnerNumberModel(0.9, 0.0, 5.0, 0.01));
		decayRate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				osc.updateOptions();
			}
		});
		decayRate.setBounds(360, 152, 108, 20);
		contentPane.add(decayRate);
		
		Panel panel = new Panel();
		panel.setBounds(195, 132, 23, 23);
		contentPane.add(panel);
		
		Panel panel_1 = new Panel();
		panel_1.setBounds(195, 166, 23, 23);
		contentPane.add(panel_1);
		
		JLabel lblHorizontalDivision = new JLabel("Horizontal Division");
		lblHorizontalDivision.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHorizontalDivision.setBounds(10, 77, 92, 22);
		contentPane.add(lblHorizontalDivision);
		
		hdivunits = new JComboBox();
		hdivunits.setModel(new DefaultComboBoxModel(new String[] {"mv", "v", "kv"}));
		hdivunits.setSelectedIndex(1);
		hdivunits.setBounds(206, 77, 47, 22);
		contentPane.add(hdivunits);
		
		hdivtext = new JSpinner(new SpinnerNumberModel(2.0, 0.0, 10.0, 0.1));
		hdivtext.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				osc.updateOptions();
			}
		});
		hdivtext.setBounds(112, 78, 86, 20);
		contentPane.add(hdivtext);
		
		phosphorStr = new JSpinner(new SpinnerNumberModel(0.2, 0.0, 5.0, 0.01));
		phosphorStr.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				osc.updateOptions();
			}
		});
		phosphorStr.setBounds(360, 181, 108, 20);
		contentPane.add(phosphorStr);
		
		JLabel lblNewLabel_2 = new JLabel("Phosphor Strength (B)");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(231, 184, 119, 14);
		contentPane.add(lblNewLabel_2);
		
		JCheckBox chckbxPhosphorEffect = new JCheckBox("Phosphor Effect");
		chckbxPhosphorEffect.setBounds(342, 122, 126, 23);
		contentPane.add(chckbxPhosphorEffect);
	}
}
