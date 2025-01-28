package glsim;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;

public class LGSimGui extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2057394266896945387L;

	final static String Current_Dir = System.getProperty("user.dir");
	
	public LGSimGui frame;
	
	private JPanel contentPane;
	private BoardPanel mainPanel;
	private CircuitSchema extCircuit;
	
	private int[] dimensions = {6,4};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		(new CircuitSchema()).getGui();
	}

	/**
	 * Create the application.
	 */
	public LGSimGui(CircuitSchema circ) {
		initialize(circ);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(CircuitSchema circ) {
		frame = this;
		this.extCircuit = circ;
		
		frame.setTitle("Logical Circuit Simulator");
		
		// Minimum 4x2 grid - Optional will be resized later
		frame.setBounds(200, 200, 50 + dimensions[1]*150 + 70,60 + dimensions[0]*70 + 30);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		contentPane.setLayout(new BorderLayout(25,15));
		frame.setContentPane(contentPane);
		
		/**
		 * Create the Gate Panel
		 */
		JPanel gatePanel = new JPanel();
		gatePanel.setSize(200, 100);
		gatePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(gatePanel,BorderLayout.NORTH);
		
		JButton btnAndGate = new TopMenuButton("Gate","AND");
		btnAndGate.addActionListener(new AddGate(this,"AND"));
		gatePanel.add(btnAndGate);
		
		JButton btnOrGate = new TopMenuButton("Gate","OR");
		btnOrGate.addActionListener(new AddGate(this,"OR"));
		gatePanel.add(btnOrGate);
		
		JButton btnNandGate = new TopMenuButton("Gate","NAND");
		btnNandGate.addActionListener(new AddGate(this,"NAND"));
		gatePanel.add(btnNandGate);
		
		JButton btnNorGate = new TopMenuButton("Gate","NOR");
		btnNorGate.addActionListener(new AddGate(this,"NOR"));
		gatePanel.add(btnNorGate);
		
		JButton btnNotGate = new TopMenuButton("Gate","NOT");
		btnNotGate.addActionListener(new AddGate(this,"NOT"));
		gatePanel.add(btnNotGate);
		
		JButton btnXorGate = new TopMenuButton("Gate","XOR");
		btnXorGate.addActionListener(new AddGate(this,"XOR"));
		gatePanel.add(btnXorGate);
		
		JButton btnXnorGate = new TopMenuButton("Gate","XNOR");
		btnXnorGate.addActionListener(new AddGate(this,"XNOR"));
		gatePanel.add(btnXnorGate);

		JButton btnSwitch = new TopMenuButton("Switch","");
		btnSwitch.addActionListener(new AddSwitch(this));
		gatePanel.add(btnSwitch);
		
		JButton btnLed = new TopMenuButton("LED","Off");
		btnLed.addActionListener(new AddLed(this));
		gatePanel.add(btnLed);
		
		JButton btnProbe = new TopMenuButton("Probe","");
		btnProbe.addActionListener(new AddProbe(this));
		gatePanel.add(btnProbe);

		
		/**
		 * 
		*/
		
		/**
		 * Create the Options Panel
		 */
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
		contentPane.add(actionsPanel,BorderLayout.WEST);
		
		JButton btnNewBoard = new JButton("New Board");
		btnNewBoard.addActionListener(new NewBoard(this));
		actionsPanel.add(btnNewBoard);
		
		JButton btnResetBoard = new JButton("Reset Board");
		btnResetBoard.addActionListener(new ResetBoard(this));
		actionsPanel.add(btnResetBoard);
		
		JButton btnLoadCircuit = new JButton("Load Circuit");
		btnLoadCircuit.addActionListener(new LoadCircuit(this));
		actionsPanel.add(btnLoadCircuit);
		
		JButton btnSaveCircuit = new JButton("Save Circuit");
		btnSaveCircuit.addActionListener(new SaveCircuit(this));
		actionsPanel.add(btnSaveCircuit);
		
		JButton btnRunCircuit = new JButton("Run Circuit");
		btnRunCircuit.setForeground(Color.red);
		btnRunCircuit.addActionListener(new RunCircuit(this));
		actionsPanel.add(btnRunCircuit);
		
		JButton btnEditGate = new JButton("Edit Gate");
		btnEditGate.addActionListener(new EditGate(this));
		actionsPanel.add(btnEditGate);
		
		JButton btnDeleteGate = new JButton("Delete Gate");
		btnDeleteGate.addActionListener(new DeleteGate(this));
		actionsPanel.add(btnDeleteGate);
		
		JButton btnDeleteSwitch = new JButton("Delete Switch");
		btnDeleteSwitch.addActionListener(new DeleteSwitch(this));
		actionsPanel.add(btnDeleteSwitch);
		
		JButton btnDeleteLed = new JButton("Delete LED");
		btnDeleteLed.addActionListener(new DeleteLed(this));
		actionsPanel.add(btnDeleteLed);
		
		JButton btnDeleteProbe = new JButton("Delete Probe");
		btnDeleteProbe.addActionListener(new DeleteProbe(this));
		actionsPanel.add(btnDeleteProbe);
		/**
		 * 
		 */
		
		// Finally set the Board
		this.setBoard(6,4,true);
	}
	
	public void setExternalCircuit(CircuitSchema circ) {
		this.extCircuit = circ;
	}
	
	public CircuitSchema getExternalCircuit() {
		return this.extCircuit;
	}
	
	public void setBoard(int rows,int cols, boolean setExternal) {
		if (this.mainPanel != null) {
			this.remove(mainPanel);
		}
		
		this.dimensions[0] = rows;
		this.dimensions[1] = cols;
		
		this.mainPanel = new BoardPanel(rows,cols);
		this.contentPane.add(mainPanel);
		
		this.mainPanel.setBackground(Color.white);
		this.mainPanel.setLayout(null);
		

		this.mainPanel.fitToParent(this);
		
		if(setExternal) {
			this.setExternalBoard();
		}
	}
	
	public void setExternalBoard() {
		this.extCircuit.setBoard(dimensions[0],dimensions[1]);
	}
	
	public BoardPanel getBoard() {
		return mainPanel;
	}
	

    // TopMenuButton
	private static class TopMenuButton extends JButton {
		public TopMenuButton(String type, String extra) {
			super();
			
			String tooltip = "Add new";
			String imagePath = LGSimGui.Current_Dir + "/images/";
			String tooltipExtra = " ";
			String bP = "", aP = "";
			
			if( type.equalsIgnoreCase("gate") ) {
				tooltipExtra += extra + " " + type;
				bP += extra;
			}
			else if(type.equalsIgnoreCase("led")) {
				aP += extra;
			}
			
			tooltip += tooltipExtra + type;
			imagePath += bP + type + aP + "32.gif";
            
            System.out.println("Imagepath: " + imagePath);

			ImageIcon btnIcon = new ImageIcon(imagePath);
			this.setToolTipText(tooltip);
			this.setIcon(btnIcon);
		}
	}
	

	/**
	 * The Top-Menu Items, Listeners 
	 */
	
	private static class AddGate implements ActionListener {
		private String type;
		private LGSimGui myGui;
		
		public AddGate(LGSimGui myGui,String type) {
			this.myGui = myGui;
			this.type = type;
		}
		
		public void actionPerformed(ActionEvent pushed) {		
			/*int values[], dims[];
			JButton button = (JButton)pushed.getSource();
			LGSimGui frame = (LGSimGui)SwingUtilities.getWindowAncestor(button);
			
			dims = frame.getBoard().getDimensions();
			GateDialog dialog = new GateDialog (frame,type,true, dims[0], dims[1]);
			
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(frame);
	
			dialog.setVisible(true);
			
			if(dialog.OK_BUTTON_PRESSED) {
				values = dialog.getValues();
				
				frame.getCircuit().board.addGate(type, values[1], values[0], values[2], values[3], values[4]);
				
				frame.getBoard().addGate(type, values);
			}*/

			BoardPanel board = myGui.getBoard();
			int[] dimensions = board.getDimensions();
			int rows = dimensions[0], columns = dimensions[1];
			
            int numOptions = 5;
			List<JComboBox<Integer>> optionPanes = new ArrayList<>();

            for(int i=0; i < numOptions; i++) {
                JComboBox<Integer> box = new JComboBox<Integer>();
                
                if(i==1) {
                    for(int j=0; j < columns; j++) {
                        box.addItem(j);
                    }
                }
                else {
                    for(int j=0; j < rows; j++) {
                        box.addItem(j);
                    }
                }


                optionPanes.add(box);
            }

            JTextField gateType = new JTextField(type.toUpperCase());
			gateType.setEditable(false);
			
			/**
			 * Add the Row/Column Option - Selections
			 */
			
			
			int i;
			
			Object[] fields = { "Gate Type", type,
					"Gate Row", optionPanes.get(0),
					"Gate Column", optionPanes.get(1),
					"Input Pin1 Row", optionPanes.get(2),
					"Input Pin2 Row", optionPanes.get(3),
					"Output Pin Row", optionPanes.get(4)
	
			};
			
			int result = GateProperties.showGateDialog(myGui, "Add New Gate - Properties", fields);
			
			if(result == GateProperties.OK_OPTION) {
				int[] gateValues = new int[optionPanes.size()];
				
				for(i = 0;i < optionPanes.size(); i++) {
					gateValues[i] = Integer.parseInt(optionPanes.get(i).getSelectedItem().toString());
				}
				
				myGui.getExternalCircuit().getBoard().addGate(type, gateValues[0], gateValues[1], gateValues[2], gateValues[3], gateValues[4]);
				board.addGate(type, gateValues);
			}
		}
	}
	
	private static class EditGate implements ActionListener {	
		private LGSimGui myGui;
		
		public EditGate(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent pushed) {
			BoardPanel board = myGui.getBoard();
			int[] dimensions = board.getDimensions();
			int rows = dimensions[0], columns = dimensions[1];
			
			JComboBox<Integer>[] optionPanes = (JComboBox<Integer>[])new JComboBox[]{new JComboBox<Integer>(),new JComboBox<Integer>(),new JComboBox<Integer>(),new JComboBox<Integer>(),new JComboBox<Integer>()};
			JComboBox<Integer>[] gatePos = (JComboBox<Integer>[])new JComboBox[]{new JComboBox<Integer>(), new JComboBox<Integer>()};
	
			
			/**
			 * Add the Row/Column Option - Selections
			 */
			
			
			int i;
			
			for(i = 0; i < rows; i++) {
				optionPanes[0].addItem(i);
				gatePos[0].addItem(i);
				
				if (i < columns-1) {
					optionPanes[1].addItem(i);
					gatePos[1].addItem(i);
				}
				optionPanes[2].addItem(i);
				optionPanes[3].addItem(i);
				optionPanes[4].addItem(i);
			}
			
			Object[] gatePosFields = {"Gate - Row", gatePos[0], "Gate - Column", gatePos[1]};
			
			int gateResult = JOptionPane.showConfirmDialog(myGui, gatePosFields,"Please gate x,y position", JOptionPane.OK_CANCEL_OPTION);
			
			if(gateResult == JOptionPane.OK_OPTION) {
				int row = Integer.parseInt(gatePos[0].getSelectedItem().toString()), column = Integer.parseInt(gatePos[1].getSelectedItem().toString());
				BoardPanel.BoardGate gate = board.getGate(row, column);
				
				if(gate!= null) {
					optionPanes[0].setSelectedIndex(row);
					optionPanes[1].setSelectedIndex(column);
					optionPanes[2].setSelectedIndex(gate.pin1Row);
					optionPanes[3].setSelectedIndex(gate.pin2Row);
					optionPanes[4].setSelectedIndex(gate.pinOutRow);
					
					Object[] fields = { "Gate Type", gate.type,
							"Row-Order", optionPanes[0],
							"Column-Order", optionPanes[1],
							"Pin1-Row Order", optionPanes[2],
							"Pin2-Row Order", optionPanes[3],
							"Output Pin-Order", optionPanes[4]
					};
					
					int result = GateProperties.showGateDialog(myGui, "Edit Gate Properties", fields);
					
					if(result == GateProperties.OK_OPTION) {
						int[] gateValues = new int[optionPanes.length];
						
						for(i = 0;i < optionPanes.length; i++) {
							gateValues[i] = Integer.parseInt(optionPanes[i].getSelectedItem().toString());
						}
						
						myGui.getExternalCircuit().getBoard().removeGate(row, column);
						myGui.getExternalCircuit().getBoard().addGate(gate.type, gateValues[1], gateValues[0], gateValues[2], gateValues[3], gateValues[4]);
						
						board.removeGate(row, column);
						board.addGate(gate.type, gateValues);
					}
				}
			}
		}
	}
	
	private static class DeleteGate implements ActionListener {
		public LGSimGui myGui;
		
		public DeleteGate(LGSimGui myGui) {
			this.myGui = myGui;
		}
	
		@Override
		public void actionPerformed(ActionEvent pushed) {
			int[] dims = myGui.getBoard().getDimensions();
			JComboBox<Integer> rowField = new JComboBox<Integer>();
			JComboBox<Integer> colField = new JComboBox<Integer>();
			
			for(int i = 0; i < dims[0]; i++) {
				rowField.addItem(i);
			}
			
			for(int j = 0; j < dims[1] - 1; j++) {
				colField.addItem(j);
			}
			
			Object[] options = {"Gate - Row: ", rowField, "Gate - Column:", colField};
			
			int result = JOptionPane.showConfirmDialog(myGui, options, "Please give gate x,y position", JOptionPane.OK_CANCEL_OPTION);
			
			if(result == JOptionPane.OK_OPTION) {
				int x = Integer.parseInt(rowField.getSelectedItem().toString()), y = Integer.parseInt(colField.getSelectedItem().toString());
				BoardPanel.BoardGate gate = myGui.getBoard().getGate(x, y);
				if(gate != null) {
					myGui.getBoard().removeGate(x,y);
					myGui.getExternalCircuit().getBoard().removeGate(x, y);
				}
			}
		}
	}
	
	private static class AddProbe implements ActionListener {
		private LGSimGui myGui;
		
		public AddProbe(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] dims = myGui.getBoard().getDimensions();
			JComboBox<Integer> rowField = new JComboBox<Integer>();
			JComboBox<Integer> colField = new JComboBox<Integer>();
			
			for(int i = 0; i < dims[0]; i++) {
				rowField.addItem(i);
			}
			
			for(int j = 0; j < dims[1]; j++) {
				colField.addItem(j);
			}
			
			Object[] options = {"Probe - Row: ", rowField, "Probe - Column:", colField};
			
			int result = JOptionPane.showConfirmDialog(myGui, options, "Please give probe x,y position", JOptionPane.OK_CANCEL_OPTION);
			
			if(result == JOptionPane.OK_OPTION) {
				int x = Integer.parseInt(rowField.getSelectedItem().toString()), y = Integer.parseInt(colField.getSelectedItem().toString());
				myGui.getBoard().addProbe(x, y);
			}
		}
	}
	
	private static class DeleteProbe implements ActionListener {
		private LGSimGui myGui;
		
		public DeleteProbe(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] dims = myGui.getBoard().getDimensions();
			JComboBox<Integer> rowField = new JComboBox<Integer>();
			JComboBox<Integer> colField = new JComboBox<Integer>();
			
			for(int i = 0; i < dims[0]; i++) {
				rowField.addItem(i);
			}
			
			for(int j = 0; j < dims[1] - 1; j++) {
				colField.addItem(j);
			}
			
			Object[] options = {"Probe - Row: ", rowField, "Probe - Column:", colField};
			
			int result = JOptionPane.showConfirmDialog(myGui, options, "Delete probe x,y position", JOptionPane.OK_CANCEL_OPTION);
			
			if(result == JOptionPane.OK_OPTION) {
				int x = Integer.parseInt(rowField.getSelectedItem().toString()), y = Integer.parseInt(colField.getSelectedItem().toString());
				myGui.getBoard().deleteProbe(x, y);
			}
		}
	}
	
	private static class AddSwitch implements ActionListener {
		private LGSimGui myGui;
		
		public AddSwitch(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent pushed) {		
			Integer[] itemsRow = new Integer[myGui.getBoard().getDimensions()[0]];
			
			for(int i=0; i < itemsRow.length; i++) {
				itemsRow[i] = i;
			}
			
			JComboBox<Integer> rowOptions = new JComboBox<Integer>(itemsRow);
	
			Object[] fields = {
		    		"Switch Row-Order: ",rowOptions
		    };
	
		    int result = JOptionPane.showConfirmDialog(myGui, fields, "Please give board switch - position", JOptionPane.OK_CANCEL_OPTION);
		    int selc;
		    
		    if (result == JOptionPane.OK_OPTION) {
		    	selc = Integer.parseInt(rowOptions.getSelectedItem().toString());
		    	myGui.getBoard().addSwitch(selc);
		    }
		}
	}
	
	private static class DeleteSwitch implements ActionListener {
		private LGSimGui myGui;
		
		public DeleteSwitch(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent pushed) {
			Integer[] itemsRow = new Integer[myGui.getBoard().getDimensions()[0]];
			
			for(int i=0; i < itemsRow.length; i++) {
				itemsRow[i] = i;
			}
			
			JComboBox<Integer> rowOptions = new JComboBox<Integer>(itemsRow);
	
			Object[] fields = {
		    		"Switch Row-Order: ",rowOptions
		    };
	
		    int result = JOptionPane.showConfirmDialog(myGui, fields, "Remove Switch", JOptionPane.OK_CANCEL_OPTION);
		    int selc;
		    
		    if (result == JOptionPane.OK_OPTION) {
		    	selc = Integer.parseInt(rowOptions.getSelectedItem().toString());
		    	myGui.getBoard().removeSwitch(selc);
		    }
		}
	}
	
	private static class AddLed implements ActionListener {
		private LGSimGui myGui;
		
		public AddLed(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent pushed) {
			Integer[] itemsRow = new Integer[myGui.getBoard().getDimensions()[0]];
			
			for(int i=0; i < itemsRow.length; i++) {
				itemsRow[i] = i;
			}
			
			JComboBox<Integer> rowOptions = new JComboBox<Integer>(itemsRow);
	
			Object[] fields = {
		    		"Led Row-Order: ",rowOptions
		    };
	
		    int result = JOptionPane.showConfirmDialog(myGui, fields, "Please give Led Position", JOptionPane.OK_CANCEL_OPTION);
		    int selc;
		    
		    if (result == JOptionPane.OK_OPTION) {
		    	selc = Integer.parseInt(rowOptions.getSelectedItem().toString());
		    	myGui.getBoard().addLed(selc);
		    }
		}
	}
	
	private static class DeleteLed implements ActionListener {
		private LGSimGui myGui;
		
		public DeleteLed(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent pushed) {		
			Integer[] itemsRow = new Integer[myGui.getBoard().getDimensions()[0]];
			
			for(int i=0; i < itemsRow.length; i++) {
				itemsRow[i] = i;
			}
			
			JComboBox<Integer> rowOptions = new JComboBox<Integer>(itemsRow);
	
			Object[] fields = {
		    		"Led Row-Order: ",rowOptions
		    };
	
		    int result = JOptionPane.showConfirmDialog(myGui, fields, "Remove Led", JOptionPane.OK_CANCEL_OPTION);
		    int selc;
		    
		    if (result == JOptionPane.OK_OPTION) {
		    	selc = Integer.parseInt(rowOptions.getSelectedItem().toString());
		    	myGui.getBoard().removeLed(selc);
		    }
		}
	}
	
	/**
	 * Side - Panel Listeners
	 */
	
	private static class NewBoard implements ActionListener {
		private LGSimGui myGui;
		
		public NewBoard(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		public void actionPerformed(ActionEvent circLoad) {
			Integer[] itemsRow = {4,5,6,7,8,9,10,11,12};
			Integer[] itemsCol = {2,3,4,5,6,7,8};
	
			JComboBox<Integer> xField = new JComboBox<Integer>(itemsRow);
		    JComboBox<Integer> yField = new JComboBox<Integer>(itemsCol);
		    
		    Object[] fields = {
		    		"Number of Columns: ", yField,
		    		"Number of Pins per Column: ", xField
		    };
	
		    int result = JOptionPane.showConfirmDialog(this.myGui.frame, fields, "Please give board dimensions", JOptionPane.OK_CANCEL_OPTION);
		    int rows = Integer.parseInt(xField.getSelectedItem().toString());
		    int cols = Integer.parseInt(yField.getSelectedItem().toString());
	
		    if (result == JOptionPane.OK_OPTION) {	    	
		    	myGui.setBoard(rows, cols,true);
		    }
		}
	}
	
	private static class ResetBoard implements ActionListener {
		public LGSimGui myGui;
		
		public ResetBoard(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent circLoad) {
			myGui.getBoard().resetBoard();
			myGui.revalidate();
			myGui.repaint();
		}
	}
	
	private static class LoadCircuit implements ActionListener {
		public LGSimGui myGui;
		
		public LoadCircuit(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		public void actionPerformed(ActionEvent circLoad) {
			final JFileChooser myChoose = new JFileChooser();
			int returnVal = myChoose.showOpenDialog(myGui);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				myGui.getExternalCircuit().loadSchema(myChoose.getSelectedFile(),true);
			}
		}
	}
	
	private static class SaveCircuit implements ActionListener {
		public LGSimGui myGui;
		
		public SaveCircuit(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			final JFileChooser myChoose = new JFileChooser();
			int returnVal = myChoose.showSaveDialog(myGui);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				myGui.getExternalCircuit().saveSchema(myChoose.getSelectedFile().getAbsolutePath());
			}
		}
	}
	
	private static class RunCircuit implements ActionListener {
		public LGSimGui myGui;
		
		public RunCircuit(LGSimGui myGui) {
			this.myGui = myGui;
		}
		
		@Override
		public void actionPerformed(ActionEvent circLoad) {
			BoardPanel myBoard = myGui.getBoard();
			CircuitSchema extCircuit = myGui.getExternalCircuit();
			BreadBoard extBoard = extCircuit.getBoard();
			
			int[] states = myBoard.getInitialStates();
			int[] dimensions =  myBoard.getDimensions();
			extCircuit.run(states);
			
			for(int i = 0; i < dimensions[0]; i++ ) {
				myBoard.setLed(i, extBoard.getPin(i, dimensions[1]-1).getVal());
				for(int j = 0; j < dimensions[1]; j++) {
					// Update the Gui Pin values for Probing
					myBoard.getPin(i, j).setValue(extBoard.getPin(i, j).getVal() == 1 ? 1 : 0);
				}
			}
			
			myBoard.revalidate();
			myBoard.repaint();
		}
	}

}


 /*
 * Helper class. Creates a colored icon of specified size.
 */

class ColorIcon implements Icon {  
    private static int HEIGHT = 17;  
    private static int WIDTH = 17;  
  
    private Color color;  
  
    public ColorIcon(Color color) {  
        this.color = color;  
    }  
  
    public int getIconHeight() {  
        return HEIGHT;  
    }  
  
    public int getIconWidth() {  
        return WIDTH;  
    }  
  
    public void paintIcon(Component c, Graphics g, int x, int y) {  
        g.setColor(color);  
        g.fillRect(x, y, WIDTH - 1, HEIGHT - 1);  
  
        g.setColor(Color.white);  
        g.drawRect(x, y, WIDTH - 1, HEIGHT - 1);  
    }  
}