package tomasulogui;

public class IntDivide extends FunctionalUnit {

    public static final int EXEC_CYCLES = 7;
    public int doneFlag = 1;
    public int destTag;
    public int destVal;
    
    public int getDestTag() {
        return destTag;
    }
    
    public void setDestTag(int dTag) {
    	destTag = dTag;
    }
    
    public int getDestValue() {
        return destVal;
    }
    
    public void setDestValue(int dVal) {
    	destVal = dVal;
    }
    
    public IntDivide(PipelineSimulator sim) {
        super(sim);
    }
    

    public int calculateResult(int station) {
//    	 if (canWriteback) {
//             requestWriteback = false;
//         }
         if (!requestWriteback) {
 	    	if (doneFlag == getExecCycles()) {
 	        	int operand1 = stations[station].getData1();
 	        	int operand2 = stations[station].getData2();
 	        	destVal = operand1/operand2;
	        	destTag = stations[station].getDestTag();
 	    		requestWriteback = true;
 	    		doneFlag = 1;
 	    		return destVal;
 	    	}
 	    	else {
 	    		doneFlag += 1;
 	    	}
         }

         return -1;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
