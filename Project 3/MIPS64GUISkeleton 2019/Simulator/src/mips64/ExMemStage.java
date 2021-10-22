package mips64;

public class ExMemStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int storeIntData;

    int destReg;
    boolean branchTaken = false;
    
    public ExMemStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public void update() {
    	// do whatever operand if its arithmetic
    	// pass through operands to memory if load store
    	int operand1;
    	int operand2;
    	
    	
    	
    	Instruction currInst = Instruction.getInstructionFromOper(opcode);
    	if (currInst instanceof ITypeInst)
    		operand2 = simulator.idEx.immediate;
    	else {
    		operand2 = simulator.idEx.regBData;
    	}
    	if (!(currInst instanceof RTypeInst)) {
    		operand1 = simulator.idEx.regAData;
    	}
    	else {
    		operand1 = simulator.idEx.regAData;
    	}

    	
    	// assume comparator to zero available for branches
    	switch(opcode) {
    	case Instruction.INST_BEQ:	
    		branchTaken = simulator.idEx.regAData == 0;
    		break;
    	case Instruction.INST_BNE:
    		branchTaken = simulator.idEx.regAData != 0;
    		break;
    	case Instruction.INST_BLEZ:
    		branchTaken = simulator.idEx.regAData < 0;
    		break;
    	case Instruction.INST_BLTZ:
    		branchTaken = simulator.idEx.regAData <= 0;
    		break;
    	case Instruction.INST_BGEZ:
    		branchTaken = simulator.idEx.regAData > 0;
    		break;
    	case Instruction.INST_BGTZ:
    		branchTaken = simulator.idEx.regAData >= 0;
    		break;
    	}
    	
    	simulator.idEx.shouldWriteback = !branchTaken;
    	
    	
    	
    	// ALU
    	switch(opcode) {
    	case Instruction.INST_ADD:
    		
    	case Instruction.INST_SUB:
    		
    	case Instruction.INST_MUL:
    		
    	case Instruction.INST_DIV:
    		
    	case Instruction.INST_AND:
    		
    	case Instruction.INST_OR:
    		
    	case Instruction.INST_XOR:
    		
    	case Instruction.INST_ADDI:
    		
    	case Instruction.INST_ANDI:
    		
    	case Instruction.INST_ORI:
    		
    	case Instruction.INST_XORI:
    		
    	case Instruction.INST_SLL:
    		
    	case Instruction.INST_SRL:
    		
    	case Instruction.INST_SRA:
    		
    	case Instruction.INST_LW:
    		
    	case Instruction.INST_SW:
    		aluIntData = simulator.idEx.regBData + simulator.idEx.immediate;
    		
    	case Instruction.INST_BEQ:
		
    	case Instruction.INST_BNE:
    	case Instruction.INST_BLEZ:
    	case Instruction.INST_BLTZ:
    	case Instruction.INST_BGEZ:
    	case Instruction.INST_BGTZ:
    	case Instruction.INST_J:
    	case Instruction.INST_JAL:
    	case Instruction.INST_JALR:
    	case Instruction.INST_JR:
    		
    	
    	}
    
    	instPC = simulator.idEx.instPC;
    	destReg = simulator.idEx.destReg;
    	storeIntData = simulator.idEx.regBData;
    	
    
    }
}
