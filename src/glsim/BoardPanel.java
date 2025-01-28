package glsim;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;

public class BoardPanel extends JPanel {
	
	private BoardPin[][] boardPins;
	private BoardGate[][] gateLabels;
	private BoardSwitch[] switches;
	private BoardLed[] leds;
	
	private int rows = 0,columns = 0, switchHeight, colHeight;
	private int[] initializers;
	
	public BoardPanel(int rows, int columns) {
		super();
		this.rows = rows;
		this.columns = columns;
		this.initialize();
	}
	
	public void resetBoard() {
		this.removeAll();
		this.initialize();
	}
	
	public void initialize() {		
		initializers = new int[rows];
				
		boardPins = new BoardPin[rows][columns];
		gateLabels = new BoardGate[rows][columns-1];
				
		switches = new BoardSwitch[rows];
		leds = new BoardLed[rows];
		
		this.colHeight = 60 + (this.rows-1)*70;
		this.switchHeight = this.colHeight/this.rows;
		
		/**
		 * Add the Switch Buttons
		 */
		this.switchHeight = colHeight/rows;
				
		for(int k = 0; k < rows; k++) {
			BoardSwitchButton s1 = new BoardSwitchButton(switchHeight, k);
			s1.addActionListener(new SwitchChangeState());
			s1.setBounds(2, k*switchHeight + 1, 20, switchHeight);
			this.add(s1);
			
			initializers[k] = -1;
		}
		
		
		/**
		 * Add the Column-Pins
		 */
		
		for(int j = 0;j < columns;j++) {
			for(int i = 0; i < rows; i++) {
				boardPins[i][j] = new BoardPin(i,j);
				
				boardPins[i][j].setBounds(120 + 6 + j *150, i*switchHeight + (switchHeight - 17)/2, 17, 17);
				
				this.add(boardPins[i][j]);
			}
		}
		
	}
	
	public int[] getDimensions() {
		int dims[] = {this.rows, this.columns};
		
		return dims;
	}
	
	public int[] getInitialStates() {
		for(int i = 0;i<rows; i++) {
			this.initializers[i] = switches[i] != null ? switches[i].state : -1;
		}	
		
		return this.initializers;
	}
	
	public void fitToParent(LGSimGui parent) {
		parent.setSize(120+ 50 + (this.columns)*150 + 70,60 + (this.rows)*70 + 30);
		parent.revalidate();
		parent.repaint();
	}
	
	public BoardGate getGate(int row, int column) {
		return gateLabels[row][column];
	}
	
	public void addGate(String type,int gValues[]) {
		int x = gValues[0], y = gValues[1];
		
		gateLabels[x][y] = new BoardGate(type, gValues);
		gateLabels[x][y].setBounds(120 + 150*y + 66, x*this.switchHeight + (this.switchHeight-56)/2 - 1, 56, 56);
		this.add(gateLabels[x][y]);
		
		this.revalidate();
		this.repaint();
	}
	
	public void removeGate(int x, int y) {
		this.remove(gateLabels[x][y]);
		gateLabels[x][y] = null;
		
		this.revalidate();
		this.repaint();
	}
	
	public void editGate(int x, int y, int[] newVals) {
		String type = gateLabels[x][y].type;
		
		this.removeGate(x, y);
		this.addGate(type, newVals);
	}
	
	public BoardSwitch getSwitch(int row) {
		return this.switches[row];
	}
	
	public void addSwitch(int row) {
		switches[row] = new BoardSwitch(row);
		switches[row].setBounds(20 + (100-64)/2, row*this.switchHeight + (this.switchHeight-64)/2 - 1, 64, 64);
		
		this.add(switches[row]);
				
		this.revalidate();
		this.repaint();
	}
	
	public void removeSwitch(int row) {
		this.remove(switches[row]);
		this.switches[row] = null;
		
		this.revalidate();
		this.repaint();
	}
	
	public void addLed(int row) {
		leds[row] = new BoardLed(row);
		leds[row].setBounds(120 + (this.columns-1)*150 + 30 + 2, row*this.switchHeight + (this.switchHeight-56)/2, 56, 56);
		
		leds[row].setValue(this.boardPins[row][this.columns-1].getValue());
		
		this.add(leds[row]);
		
		this.revalidate();
		this.repaint();
	}
	
	public void removeLed(int row) {
		this.remove(leds[row]);
		this.leds[row] = null;
		
		this.revalidate();
		this.repaint();
	}
	
	public void setLed(int row, int value) {
		if(this.leds[row] != null) {
			this.leds[row].setValue(value);
			this.revalidate();
			this.repaint();
		}
	}
	
	public BoardPin getPin(int row, int column) {
		return this.boardPins[row][column];
	}
	
	public void addProbe(int row, int column) {
		this.boardPins[row][column].setProbe(true);
		this.revalidate();
		this.repaint();
	}
	
	public void deleteProbe(int row, int column) {
		this.boardPins[row][column].setProbe(false);
		this.revalidate();
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Draw the Switch Buttons background
		g.setColor(Color.darkGray);
		g.fillRect(1,1,22,this.getSize().height - 3);
		
		int i = 0, j;

		// Draw the Breadboard columns
		g.setColor(new Color(200,200,200));
		for(i = 0; i < columns; i++) {
			g.fillRect(120+ i*150, 1, 30, this.getSize().height - 2);
		}
		
		// Draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, this.getSize().width-1, this.getSize().height-1);
		
		
		/**
		 * Iteration for lines
		 */
		
		Rectangle gateBounds,pin1Bounds,pin2Bounds,pinOutBounds;
		String type;
		
		g.setColor(Color.black);
		
		// Draw lines for the not null gates
		for(i = 0; i < this.rows; i++) {
			for(j = 0; j < this.columns-1; j++) {
				try {
					BoardGate gate = gateLabels[i][j];
					type = gate.type;

					gateBounds = gateLabels[i][j].getBounds();
					pin1Bounds = boardPins[gate.pin1Row][j].getBounds();
					pin2Bounds = boardPins[gate.pin2Row][j].getBounds();
					pinOutBounds = boardPins[gate.pinOutRow][j+1].getBounds();
										
					if(!type.equalsIgnoreCase("not")) {
						g.drawLine(gateBounds.x - 1,gateBounds.y+14 , pin1Bounds.x+25, pin1Bounds.y + 8);
						g.drawLine(gateBounds.x - 1,gateBounds.y+56 - 15 , pin2Bounds.x+25, pin2Bounds.y + 8);
						g.drawLine(gateBounds.x + 56 ,gateBounds.y+28 , pinOutBounds.x - 4, pinOutBounds.y + 8);
					}
					else {
						g.drawLine(gateBounds.x - 1,gateBounds.y+28 , pin1Bounds.x+25, pin1Bounds.y+8);
						g.drawLine(gateBounds.x + 48 ,gateBounds.y+28 , pinOutBounds.x - 4, pinOutBounds.y + 8);
					}
				} catch( Exception e) {}
				
			}
			
			// Draw for the switches
			if(this.switches[i] != null) {
				BoardSwitch switchI = this.switches[i];
				gateBounds = switchI.getBounds();
				pin1Bounds = boardPins[i][0].getBounds();
				
				g.drawLine(gateBounds.x + 64, gateBounds.y + 32, pin1Bounds.x, pin1Bounds.y + 8 );
			}
			
		}
	}
	
	
	/**
	 * 
	 * The Gui Elements SwitchButton / Switch / Gate / Pin / Led
	 *
	 */
	
	static class BoardPin extends JLabel {
		private int value = 0, row, column;
		private boolean probeEnabled = false;
		
		public BoardPin(int row, int column) {
			super();
			this.row = row;
			this.column = column;
			
			this.setSize(17,17);
		}
		
		public void setValue(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public void setProbe(boolean probe) {
			this.probeEnabled = probe;
		}
		
		public boolean isProbeEnabled() {
			return this.probeEnabled;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);		
	
	        g.setColor(Color.black);
	        g.fillRect(0, 0, 17, 17);
	        
	        
	        // Draw on screen the Pin Value if Probe is enabled
	        if(this.probeEnabled) {
	            g.setColor(new Color(208,255,157));
	    		if(this.value == 1) {
	    			g.drawLine(8, 1, 8, 14);
	    			g.drawLine(8, 1, 5, 5);
	    		}
	    		else {
	    			g.drawOval(4, 1, 8, 13);
	    		}
	        }
		}
	}
	
	static class BoardGate extends JLabel {
		public int x,y, pin1Row, pin2Row, pinOutRow;
		public String type;
		
		public BoardGate(String type, int[] points) {
			super("");
			this.type = type;
			this.x = points[0];
			this.y = points[1];
			this.pin1Row = points[2];
			this.pin2Row = points[3];
			this.pinOutRow = points[4];
			
			ImageIcon gateIcon = new ImageIcon(LGSimGui.Current_Dir + "/images/" + type.toUpperCase() + "Gate.gif");
			Image resImg = gateIcon.getImage().getScaledInstance(56, 56, java.awt.Image.SCALE_SMOOTH);
			gateIcon = new ImageIcon(resImg);
			
			this.setToolTipText("X = " + x + ", Y = " + y);
			
			this.setIcon(gateIcon);
		}
	}
	
	static class BoardSwitch extends JLabel {
		public int y;
		public int state = 0;
		
		public BoardSwitch(int y) {
			this.y = y;
			
			ImageIcon switchIcon = new ImageIcon(LGSimGui.Current_Dir + "/images/SwitchOff.gif");
			
			this.setIcon(switchIcon);
		}
		
		public void toggleState() {
			String[] toggleIconString = {"On","Off"};
			int[] toggleState = {1,0};
			
			ImageIcon switchIcon = new ImageIcon(LGSimGui.Current_Dir + "/images/Switch" + toggleIconString[state] + ".gif");
			
			this.state = toggleState[state];
			
			this.setIcon(switchIcon);
		}	
	}
	
	static class BoardSwitchButton extends JButton {
		public int row;
		
		public BoardSwitchButton(int switchH, int row) {
			this.row = row;
			ImageIcon switchIcon = new ImageIcon(LGSimGui.Current_Dir + "/images/SwitchButton.gif");
			Image resImg = switchIcon.getImage().getScaledInstance(20, switchH, java.awt.Image.SCALE_SMOOTH);
			switchIcon = new ImageIcon(resImg);
			this.setToolTipText("Change switch-" + row + " state");
			this.setIcon(switchIcon);
		}
	}
	
	private static class SwitchChangeState implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent pushed) {
			BoardSwitchButton button = (BoardSwitchButton)pushed.getSource();
			LGSimGui frame = (LGSimGui)SwingUtilities.getWindowAncestor(button);
			BoardSwitch corSwitch = frame.getBoard().getSwitch(button.row);
			
			if(corSwitch!= null) {
				corSwitch.toggleState();
			}
		}
	}
	
	static class BoardLed extends JLabel {
		private int value, y;
		
		public BoardLed(int y) {
			this.y = y;
			this.setValue(0);
		}
		
		public void reset() {
			this.value = 0;
		}
		
		public void setValue(int val) {
			this.value = val;
			
			String state = val == 1 ? "On" : "Off";
			
			ImageIcon ledIcon = new ImageIcon(LGSimGui.Current_Dir + "/images/LED" + state + ".gif");
			Image resImg = ledIcon.getImage().getScaledInstance(56, 56, java.awt.Image.SCALE_SMOOTH);
			ledIcon = new ImageIcon(resImg);
			this.setIcon(ledIcon);
		}
	}
}