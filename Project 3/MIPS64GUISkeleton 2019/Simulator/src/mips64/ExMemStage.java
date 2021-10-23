package mips64;

public class ExMemStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC=-1;
    int opcode=62;
    int aluIntData=-1;
    int storeIntData=-1;

    int destReg;
    boolean branchTaken = false;
    
    public ExMemStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public void update() {
    	// do whatever operand if its arithmetic
    	// pass through operands to memory if load store
    	instPC = simulator.idEx.instPC;
    	destReg = simulator.idEx.destReg;
    	storeIntData = simulator.idEx.regBData;
    	opcode = simulator.idEx.opcode;
    	
    	int operand1;
    	int operand2;
    	
    	Instruction currInst = Instruction.getInstructionFromName(
    					Instruction.getNameFromOpcode(opcode));

    	if (opcode == Instruction.INST_BEQ || opcode == Instruction.INST_BNE
    			|| opcode == Instruction.INST_BLEZ || opcode == Instruction.INST_BLTZ
    			|| opcode == Instruction.INST_BLTZ || opcode == Instruction.INST_BLEZ
    			|| opcode == Instruction.INST_BGEZ || opcode == Instruction.INST_BGTZ
    			|| opcode == Instruction.INST_J    || opcode == Instruction.INST_JAL) {
    		operand1 = simulator.ifId.instPC;
    	}
    	else {
    		operand1 = simulator.idEx.regAData;
    	}
    	if (!(currInst instanceof RTypeInst))
    		operand2 = simulator.idEx.immediate;
    	else if (opcode == Instruction.INST_SLL || opcode == Instruction.INST_SRA 
    			|| opcode == Instruction.INST_SRL) {
    		operand2 = simulator.idEx.shamt;
    	}
    	else {
    		operand2 = simulator.idEx.regBData;
    	}

    	
    	// assume comparator to zero available for branches
    	switch(opcode) {
    	case Instruction.INST_BEQ:	
    		branchTaken = simulator.idEx.regAData == simulator.idEx.regBData;
    		break;
    	// mux choosing between 0 and regB based on whether its a branch or branch equal
    	case Instruction.INST_BNE:
    		branchTaken = simulator.idEx.regAData == simulator.idEx.regBData;
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
    	case Instruction.INST_J:
    	case Instruction.INST_JAL:
    	case Instruction.INST_JALR:
    	case Instruction.INST_JR:
    		branchTaken = true;
    		break;
    	default:
    		branchTaken = false;
    	}
    	
    	// this instruction should write back if it was not invalidated previous run and is an op that does write back
    	simulator.idEx.shouldWriteback = !branchTaken;
    	
    	/// PROBLEM
    	shouldWriteback = !branchTaken && !( opcode == Instruction.INST_BEQ || opcode == Instruction.INST_BNE
    			|| opcode == Instruction.INST_BLEZ || opcode == Instruction.INST_BLTZ
    			|| opcode == Instruction.INST_BLTZ || opcode == Instruction.INST_BLEZ
    			|| opcode == Instruction.INST_BGEZ || opcode == Instruction.INST_BGTZ
    			|| opcode == Instruction.INST_NOP) || (opcode == Instruction.INST_JALR)
    			|| (opcode == Instruction.INST_JAL);
    	
    	
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
    	case Instruction.INST_JAL:
    		aluIntData = operand1 + operand2; // pc + imm
    		break;
    	case Instruction.INST_JALR:
    	case Instruction.INST_JR:
    		aluIntData = operand1;
    		break;
    	
    	}
    

    
    }
}
