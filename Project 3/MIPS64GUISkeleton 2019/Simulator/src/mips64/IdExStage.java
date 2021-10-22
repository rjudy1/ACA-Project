package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    
    int destReg;

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
    }

    int getIntRegister(int regNum) {
        // todo - add supporting code
    	return simulator.memory.getIntDataAtAddr(regNum);
//        return 0;
    }

    public void update() {
    	// fetch the operands
    	// check for stalling
    	// figure out type of instruction and put into pipeline
    	if (opcode instanceof ITypeInst) {
    		
    	} else if (instanceof(JTypeInst)) {
    		
    	} else if (instanceof(RTypeInst)) {
    		
    	}
    	
    	// check branch
    	if (opcode == Instruction.INST_BEQZ ..) {
    		
    	}    	
    }
}
