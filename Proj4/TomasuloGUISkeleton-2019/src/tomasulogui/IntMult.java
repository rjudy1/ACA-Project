/*
 * authors: Aaron Johnston and Rachael Judy
 * file: IntMultiply.java
 * purpose: once operands ready, hold the unit with the in progress flag
 * 				and calculate result after appropriate delay
 *  	
 */

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
    	// wait for cycles to finish and then do the calculation on last
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
        
        return destVal;
    }
}
