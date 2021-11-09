package tomasulogui;

public class IntMult extends FunctionalUnit {

    public static final int EXEC_CYCLES = 4;
    public int doneFlag = 1;
    
    boolean requestWriteback = false;
    boolean canWriteback = false;
    
    public void setRequestWriteback(boolean reqWB) {
    	requestWriteback = reqWB;
    }

    public int getRequestWriteBack() {
        return requestWriteback;
    }
    
    public void setCanWriteback(boolean canWB) {
    	canWriteback = canWB;
    }
    
    public int getCanWriteback() {
        return canWriteback;
    }
    
    public IntMult(PipelineSimulator sim) {
        super(sim);
    }

    public void squashAll() {
    	for (int i = 0; i < 2; i++) {
    		stations[i] = -1;
    	}
     	requestWriteback = false;
    }
    
    public int getExecCycles() {
        return EXEC_CYCLES;
    }

    public int calculateResult(int station) {        
        if (canWriteback) {
            requestWriteback = false;
        }
        if (!requestWriteback) {
	    	if (doneFlag == getExecCycles()) {
	        	int operand1 = stations[station].getData1();
	        	int operand2 = stations[station].getData2();
	        	int result = operand1*operand2;
	    		requestWriteback = True;
	    		doneFlag = 1;
	    	}
	    	else {
	    		doneFlag += 1;
	    		return -1;
	    	}
        }
        // check reservationStations for cdb data
        if (simulator.cdb.getDataValid()) {
            for (int i = 0; i < BUFFER_SIZE; i++) {
              if (stations[i] != null) {
                stations[i].snoop(cdb);
              }
            }
        }
        
        
        
    }

    
}
