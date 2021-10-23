package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC=-1;
    int opcode=62;
    int regAData=-1;
    int regBData=-1;
    int immediate=-1;
    int shamt = 0;
    int destReg;

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
    }

    int getIntRegister(int regNum) {
        // todo - add supporting code
    	return simulator.regFile[regNum];
//        return 0;
    }

    public void update() {
    	// fetch the operands
    	// check for stalling
    	// figure out type of instruction and put into pipeline
    	
    	opcode = simulator.ifId.opcode;
    	instPC = simulator.ifId.instPC;
    	destReg = simulator.ifId.destReg;
    	shamt = simulator.ifId.shamt;
    	immediate = simulator.ifId.immediate;

   		regAData = simulator.regFile[simulator.ifId.op1];
       	if (opcode == Instruction.INST_JR || opcode == Instruction.INST_JALR) {
       		regBData = simulator.regFile[destReg];
       	} else {
       		regBData = simulator.regFile[simulator.ifId.op2];
        }
    }
}
