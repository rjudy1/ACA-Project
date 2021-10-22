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
    	case Instruction.INST_BNE:
    		branchTaken = simulator.idEx.regAData == 0;
    	case Instruction.INST_BLEZ:
    	case Instruction.INST_BLTZ:
    	case Instruction.INST_BGEZ:
    	case Instruction.INST_BGTZ:
    	}
    	
    	
    	
    	// ALU
    	switch(opcode) {
    	case Instruction.INST_ADD:
    	case Instruction.INST_ADDI:
    	case Instruction.INST_SW:
    	case Instruction.INST_LW:
    		aluIntData = operand1 + operand2;
    		break;
    	case Instruction.INST_SUB:
    	case Instruction.INST_ANDI:
    		aluIntData = operand1 - operand2; 
    		break;
    	case Instruction.INST_MUL:
    		aluIntData = operand1 * operand2;
    		break;
    	case Instruction.INST_DIV:
    		aluIntData = operand1 / operand2;
    		break;
    	case Instruction.INST_AND:
    		aluIntData = operand1 & operand2;
    		break;
    	case Instruction.INST_OR:
    	case Instruction.INST_ORI:
    		aluIntData = operand1 | operand2;
    		break;
    	case Instruction.INST_XOR:
    	case Instruction.INST_XORI:
    		aluIntData = operand1 ^ operand2;    		
    		break;
    	case Instruction.INST_SLL:
    		aluIntData = operand1 << operand2;
    		break;
    	case Instruction.INST_SRL:
    		aluIntData = operand1 >>> operand2;
    		break;
    	case Instruction.INST_SRA:
    		aluIntData = operand1 >> operand2;
    		break;    		
    	case Instruction.INST_BEQ:
    	case Instruction.INST_BNE:
    	case Instruction.INST_BLEZ:
    	case Instruction.INST_BLTZ:
    	case Instruction.INST_BGEZ:
    	case Instruction.INST_BGTZ:
    	case Instruction.INST_J:
    		aluIntData = operand1 + operand2;
    		break;
    	case Instruction.INST_JAL:
    	case Instruction.INST_JALR:
    		aluIntData = operand1;
    		break;
    	case Instruction.INST_JR:
    		aluIntData = operand1;
    		break;
    	
    	}
    	
    }
}
