package glsim;

public class Gate {    
    public static enum GateType {
        AND {
            @Override
            public boolean evaluate(boolean p1, boolean p2) {
                return p1 && p2;
            }
        },
    
        OR {
            @Override
            public boolean evaluate(boolean p1, boolean p2) {
                return p1 || p2;
            }
        },
        
        NOR {
            @Override
            public boolean evaluate(boolean p1, boolean p2) {
                return !(p1 || p2);
            }
        },
        
        NAND {
            @Override
            public boolean evaluate(boolean p1, boolean p2) {
                return !(p1 && p2);
            }  
        },
        
        XOR {
            @Override
            public boolean evaluate(boolean p1, boolean p2) {
                return (p1 ^ p2);
            }
        },
        
        NOT {
            @Override
            public boolean evaluate(boolean p1, boolean p2) {
                return !(p1);
            }
        };
    
    
        public boolean evaluate(boolean p1, boolean p2) {
            return true;
        }
    }

    static int guid = 0;

    private GateType _gtype;
    private int _id = ++guid;
    private String _type;
    private int _p1Row, _p2Row, _peRow;
    private int _row, _col;

    public Gate(String type, int row, int col, int pin1Row, int pin2Row, int pinExitRow) {
        _type = type;
        _gtype = GateType.AND;
        _row = row;
        _col = col;
        _p1Row = pin1Row;
        _p2Row = pin2Row;
        _peRow = pinExitRow;
    }

    public int getPin1() {
        return _p1Row;
    }

    public int getPin2() {
        return _p2Row;
    }

    public int getPinExit() {
        return _peRow;
    }

    public int[] getPos() {
        return new int []{_row, _col};
    }

    public int getRow() {
        return _row;
    }

    public int getCol() {
        return _col;
    }

    public void setPos(int row, int col) {
        _row = row;
        _col = col;
    }

    public String getType() {
        return _type;
    }

    public boolean evaluate(boolean p1Value, boolean p2Value) {
        return _gtype.evaluate(p1Value, p2Value);
    }
}

