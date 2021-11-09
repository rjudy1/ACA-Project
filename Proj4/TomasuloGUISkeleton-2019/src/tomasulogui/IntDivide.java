package tomasulogui;

public class IntDivide extends FunctionalUnit {

    public static final int EXEC_CYCLES = 7;
    public int doneFlag = 1;
    public int destTag;
    public int destVal;
    boolean requestWriteback = false;
    boolean canWriteback = false;
    
    public void setRequestWriteback(boolean reqWB) {
    	requestWriteback = reqWB;
    }

    public boolean getRequestWriteBack() {
        return requestWriteback;
    }
    
    public void setCanWriteback(boolean canWB) {
    	canWriteback = canWB;
    }
    
    public boolean getCanWriteback() {
        return canWriteback;
    }
    
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
    	 if (canWriteback) {
             requestWriteback = false;
         }
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
         // check reservationStations for cdb data
         if (simulator.cdb.getDataValid()) {
             for (int i = 0; i < 2; i++) {
               if (stations[i] != null) {
                 stations[i].snoop(simulator.cdb);
               }
             }
         }
         return -1;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
