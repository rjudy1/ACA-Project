package tomasulogui;

public class IntDivide extends FunctionalUnit {

    public static final int EXEC_CYCLES = 7;

    public IntDivide(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        
    	int operand1 = FunctionalUnit.stations[station].data1
    	int operand2 = FunctionalUnit.stations[station].data2
        int result = operand1/operand2;
        return result;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
