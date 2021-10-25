package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC=0;
    int opcode=62;
    int regAData=-1;
    int regBData=-1;
    int immediate=-1;
    int destReg=32;
    int regA;
    int regB;

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
    }

    int getIntRegister(int regNum) {
    	return simulator.regFile[regNum];
    }

    public void update() {
    	// fetch the operands
    	// check for stalling
    	// figure out type of instruction and put into pipeline
    	if (!simulator.interlock) {
	    	opcode = simulator.ifId.opcode;
	    	instPC = simulator.ifId.instPC;
	    	destReg = simulator.ifId.destReg;
	    	immediate = simulator.ifId.immediate;
	    	shouldWriteback = simulator.ifId.shouldWriteback & !simulator.exMem.branchTaken;
    	}
    	
   		regAData = simulator.regFile[simulator.ifId.op1];
    	regA = simulator.ifId.op1;

       	if (opcode == Instruction.INST_JR || opcode == Instruction.INST_JALR || opcode == Instruction.INST_SW) {
       		regBData = simulator.regFile[destReg];
	    	regB = destReg;
       	} else {
       		regBData = simulator.regFile[simulator.ifId.op2];
	    	regB = simulator.ifId.op2;
	
    	}
    }
}
