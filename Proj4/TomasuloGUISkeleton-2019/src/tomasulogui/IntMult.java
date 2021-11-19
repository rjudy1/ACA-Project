package tomasulogui;

public class IntMult extends FunctionalUnit {

    public static final int EXEC_CYCLES = 4;
    public int doneFlag = 1;

    public IntMult(PipelineSimulator sim) {
        super(sim);
    }

    public void squashAll() {
    	for (int i = 0; i < 2; i++) {
    		stations[i] = null;
    	}
     	requestWriteback = false;
    }
    
    public int getExecCycles() {
        return EXEC_CYCLES;
    }

    public int calculateResult(int station) {        
//        if (canWriteback) {
//            requestWriteback = false;
//        }
        if (!requestWriteback) {
	    	if (doneFlag == getExecCycles()) {
	        	int operand1 = stations[station].getData1();
	        	int operand2 = stations[station].getData2();
	        	destTag = stations[station].getDestTag();
	        	destVal = operand1*operand2;
	    		requestWriteback = true;
	    		doneFlag = 1;
	    		inProgress = false;
	    		return destVal;
	    	}
	    	else {
	    		inProgress = true;
	    		doneFlag += 1;
	    	}
	    	stationDone = station;
        }
        
        return -1;
    }

    
}
