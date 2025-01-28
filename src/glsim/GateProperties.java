package glsim;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GateProperties {
	
	static final int OK_OPTION = 1, CANCEL_OPTION = 0;
		
	public GateProperties() {
		
	}
	
	static int showGateDialog(JFrame frame, String title, Object[] fields) {
		JDialog dialog = new JDialog(frame, title, true);
		final JPanel contentPanel = new JPanel();
		
		contentPanel.setOpaque(true);
		dialog.setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		/**
		 * OK - Cancel ButtonPane
		 */
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		/**
		 * The Options Panel
		 */
		JPanel optionsPanel = new JPanel();
		
		optionsPanel.setLayout(new GridLayout(0,2));
		
		/**
		 * Get user specified fields in format of Label - Field
		 */
		int size = fields.length;
		
		/*buttonPane.setBounds(60, 220, 180, 40);
		optionsPanel.setBounds(37, 34, 226, 180);
		dialog.setBounds(100,100,300,280);*/
		
		// OptionPane.height + (OptionPane.y + 5) + ButtonPane.height + 20
		int dHeight = size/2 * 30 + 30 + 40 + 20;
		buttonPane.setBounds(60, dHeight - 60, 180, 40);
		optionsPanel.setBounds(37, 25, 226, size/2 * 30);
		dialog.setBounds(100,100,300, dHeight);

		JLabel labelGate = new JLabel((String)fields[0]);
		JTextField gateTyp = new JTextField((String)fields[1]);
		gateTyp.setEditable(false);
		
		optionsPanel.add(labelGate);
		optionsPanel.add(gateTyp);
		
		for(int i = 2; i < size; i+=2) {
			optionsPanel.add(new JLabel((String)fields[i]));
			optionsPanel.add((JComponent)fields[i+1]);
		}
		
		RetValue retVal = new RetValue(GateProperties.CANCEL_OPTION);
		
		okButton.addActionListener(new OkCloseDialog(retVal,dialog));
		cancelButton.addActionListener(new CancelCloseDialog(retVal,dialog));
						
		contentPanel.add(buttonPane);
		contentPanel.getRootPane().setDefaultButton(okButton);
		contentPanel.add(optionsPanel);
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(frame);

		dialog.setVisible(true);
		dialog.pack();

		return retVal.getVal();
	}
	
	private static class RetValue {
		private int value;
		
		public RetValue(int val) {
			this.value = val;
		}
		
		public void setVal(int val) {
			this.value = val;
		}
		
		public int getVal() {
			return this.value;
		}
	}

	private static class OkCloseDialog implements ActionListener {
		RetValue parent;
		JDialog dialog;
		
		public OkCloseDialog(RetValue parent,JDialog dialog) {
			this.parent = parent;
			this.dialog = dialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			parent.setVal(GateProperties.OK_OPTION);
			dialog.dispose();
		}
	}
	
	private static class CancelCloseDialog implements ActionListener {
		RetValue parent;
		JDialog dialog;

		public CancelCloseDialog(RetValue parent, JDialog dialog) {
			this.parent = parent;
			this.dialog = dialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			parent.setVal(GateProperties.CANCEL_OPTION);
			dialog.dispose();
		}
	}
}
