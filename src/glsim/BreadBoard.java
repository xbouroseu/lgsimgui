package glsim;

import glsim.Gate;
import glsim.Gate.GateType;

class BreadBoard {
	private Pin[][] pins;
	private Gate[][] gates;

	public int rows, columns;
	
	public BreadBoard(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		this.pins = new Pin[rows][columns];
		this.gates = new Gate[rows][columns-1];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				this.pins[i][j] = new Pin();
			}
		}
	}

	public void resetState() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				this.pins[i][j].resetVal();
			}
		}
	}
	
	public int[] getDimensions() {
		int dims[] = {rows,columns};
		return dims;
	}
	
	public Pin getPin(int row, int column) {
		return pins[row][column];
	}
	
	public Gate getGate(int row, int column) {
		return this.gates[row][column];
	}

	public void addGate(String type, int row, int column, int pin1Row, int pin2Row, int exitRow) {
		gates[row][column] = new Gate(type, row, column, pin1Row, pin2Row, exitRow);
	}

    public void editGate() {}
    
	public void removeGate(int row, int column) {
		gates[row][column] = null;
	}
	
	public void printState() {
		int i,j;
		
		System.out.println("Board pin state: \n---------------\n");
		
		for(i = 0;i < rows; i++) {
			String fram = "[ ";
			for(j = 0; j < columns; j++) {
				if(pins[i][j].getVal() >= 0 ) {
					fram += " " + pins[i][j].getVal() + " ";
				}
				else {
					fram += " - ";
				}
			}
			fram += "]";
			System.out.println(fram);
		}
	}
	
	static class Pin {
		private int value;
		
		public Pin() {
			this.value = -1;
		}
		
		public Pin(int value) {
			this.value = value;
		}
		
		public void setVal(boolean value) {
			this.value = value ? 1 : 0;
		}

        public void setVal(int value) {
            this.value = value;
        }
		
		public void resetVal() {
			this.value = -1;
		}
		
		public int getVal() {
			return this.value;
		}
	}
}
