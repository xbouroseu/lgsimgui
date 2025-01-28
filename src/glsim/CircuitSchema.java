package glsim;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CircuitSchema {
	private BreadBoard board;
	private LGSimGui myGui;
	
	public static void main(String[] args) {
		CircuitSchema cd = new CircuitSchema();
		cd.getGui();
	}
	
	// Add some gates
	public CircuitSchema() {
		//GLGui myGui = new GLGui(this);
		//myGui.setCircuit(this);
		//myGui.frame.setVisible(true);
		/*board = new BreadBoard(10,4);
		board.addGate("AND",0, 1, 0, 1, 1);
		board.addGate("AND",0, 2, 2, 3, 2);
		board.addGate("NOR",0, 4, 4, 5, 3);
		board.addGate("NOR",1, 1, 1, 2, 1);
		board.addGate("XOR",1, 3, 2, 3, 3);
		board.addGate("XOR",2, 1, 1, 3, 3);
		board.addGate("OR",2, 3, 1, 3, 3);*/
		//loadCircuit("src/glsim/mycic.txt");
	}
	
	public void getGui() {
		EventQueue.invokeLater(new Runnable() {
			public CircuitSchema circuit;
			
			public Runnable setCircuit(CircuitSchema circ) {
				this.circuit = circ;
				return this;
				
			}
			public void run() {
				try {
					myGui = new LGSimGui(this.circuit);
					myGui.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.setCircuit(this));
	}
	
	public BreadBoard getBoard() {
		return this.board;
	}
	
	public void setBoard(int rows, int columns) {
		this.board = new BreadBoard(rows,columns);
	}
	
	public void saveSchema(String file_name) {
		int i = 0, j = 0;
		
		try {
			PrintWriter fileWrite;

			fileWrite = new PrintWriter(file_name, "UTF-8");
			
			fileWrite.write("BOARD " + board.rows + " " + board.columns);
			
			for(j = 0; j < board.columns-1; j++) {
				for(i = 0; i < board.rows; i++) {
					Gate gate = board.getGate(i, j);
					if( gate != null ) {
						fileWrite.write("\n" + gate.getType() + " " + gate.getCol() + " " + gate.getRow() + " " + gate.getPin1() + " " + gate.getPin2() + " " + gate.getPinExit());
					}
				}
			}
			
			fileWrite.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void loadSchema(File file, boolean GuiEnabled) {
		ArrayList<String> lines = new ArrayList<String>();

		try {
			BufferedReader reader;

			reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File does not exist!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String bString[] = lines.get(0).split(" ");
		
		int dims[] = {Integer.parseInt(bString[1]), Integer.parseInt(bString[2])};
		
		board = new BreadBoard(dims[0], dims[1]);
		
		if(GuiEnabled) {
			this.myGui.setBoard(dims[0], dims[1], false);
		}
		
		for(int i = 1; i < lines.size(); i++) {
			String parts[] = lines.get(i).split(" ");
            String type = parts[0];
            
            // row layout: type, col, row, pin1Row, pin2Row, pinExitRow
			int[] points = {Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])};
            
            // arg layout: type, row, col, pin1Row, pin2Row, pinExitRow
			board.addGate(type, points[0], points[1], points[2], points[3], points[4]);

			if(GuiEnabled) {
				this.myGui.getBoard().addGate(type, points);
			}
		}
				
		System.out.println("Circuit " + file.getName() + " Loaded");
	}

	public void loadSchema(String file_name, boolean GuiEnabled) {
		File file = new File(file_name);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File " + file_name +" does not exist!");
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		this.loadSchema(file, GuiEnabled);
	}
	
	public void run(int[] states) {
		int i = 0, j = 0;
		
		try {
			int n = board.columns;
		}
		catch ( NullPointerException e ) {
			System.out.println("-Board not Given-");
			return;
		}
		
		// Reset previous state
		this.board.resetState();

		//Initialize first pins
		for(i = 0; i < board.rows; i++) {
			board.getPin(i, 0).setVal(states[i]);
		}
				
		for(j = 0; j < board.columns-1; j++) {
			for(i = 0; i < board.rows; i++) {
				Gate gate = board.getGate(i, j);

				if(gate != null) {
					BreadBoard.Pin pin1, pin2;

					pin1 = board.getPin(gate.getPin1(), j);
					pin2 = board.getPin(gate.getPin2(), j);
					
                    int pin1Val = pin1.getVal(), pin2Val = pin2.getVal();

					if(pin1Val >= 0 && (gate.getType() != "not" || pin2Val >= 0) ) {
                        boolean res = gate.evaluate(pin1Val == 1, pin2Val == 1);
						board.getPin(gate.getPinExit(), j+1).setVal(res);
					}
				}
			}
		}
		
		board.printState();
	}
	
}