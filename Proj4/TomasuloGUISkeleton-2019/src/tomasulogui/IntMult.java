package tomasulogui;

public class IntMult extends FunctionalUnit {

    public static final int EXEC_CYCLES = 4;
    public int doneFlag = 0;
    public IntMult(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
    	
    	// Do tags get resolved here or earlier?
    	int operand1 = FunctionalUnit.stations[station].getData1();
    	int operand2 = FunctionalUnit.stations[station].getData2();
        int result = operand1*operand2;
        if (doneFlag == getExecCycles()) {
    		doneFlag = 0;
    		return result;
    	}
    	else {
    		doneFlag += 1;
    		return -1;
    	}
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
