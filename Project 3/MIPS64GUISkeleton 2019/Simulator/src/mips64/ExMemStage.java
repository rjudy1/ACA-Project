package mips64;

public class ExMemStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC=-1;
    int opcode=62;
    int aluIntData=-0;
    int storeIntData=-1;

    int destReg=32;
    boolean branchTaken = false;
    
    public ExMemStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public void update() {
    	// do whatever operand if its arithmetic
    	// pass through operands to memory if load store
    	if (!simulator.interlock) {
	    	instPC = simulator.idEx.instPC;
	    	destReg = simulator.idEx.destReg;
	    	storeIntData = simulator.idEx.regBData;
	    	opcode = simulator.idEx.opcode;
    	}
    	
    	
    	// assume comparator to zero available for branches
    	switch(opcode) {
    	case Instruction.INST_BEQ:	
    		branchTaken = simulator.idEx.regAData == simulator.idEx.regBData;
    		break;
    	// mux choosing between 0 and regB based on whether its a branch or branch equal
    	case Instruction.INST_BNE:
    		branchTaken = simulator.idEx.regAData != simulator.idEx.regBData;
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
    	
    	// branch is taken if branch instruction and previous says to write back
    	branchTaken &= simulator.idEx.shouldWriteback; 
    	// this instruction should write back if it wasn't invalidated previously 
    	// and is an instruction that does write back as established in IfId
    	// bring forward writeback of previous
      	shouldWriteback = !(
					opcode == Instruction.INST_BEQ || opcode == Instruction.INST_BNE
				|| opcode == Instruction.INST_BLEZ || opcode == Instruction.INST_BLTZ
				|| opcode == Instruction.INST_BLTZ || opcode == Instruction.INST_BLEZ
				|| opcode == Instruction.INST_BGEZ || opcode == Instruction.INST_BGTZ
				|| opcode == Instruction.INST_NOP  || opcode == Instruction.INST_J
				|| opcode == Instruction.INST_JR)
      			&& simulator.idEx.shouldWriteback && !simulator.interlock;
    	
      	
    	int operand1 = 0;
    	int operand2 = 0;
    	
    	Instruction currInst = Instruction.getInstructionFromName(
    					Instruction.getNameFromOpcode(opcode));
    	// only case where we need to stall and not move the previous instructions
    	simulator.interlock = (simulator.ifId.op1 == destReg || simulator.ifId.op2 == destReg) 
    			&& opcode == Instruction.INST_LW && !simulator.interlock 
    			&& simulator.ifId.opcode != Instruction.INST_NOP;
    	
    	if (opcode == Instruction.INST_BEQ || opcode == Instruction.INST_BNE
    			|| opcode == Instruction.INST_BLEZ || opcode == Instruction.INST_BLTZ
    			|| opcode == Instruction.INST_BLTZ || opcode == Instruction.INST_BLEZ
    			|| opcode == Instruction.INST_BGEZ || opcode == Instruction.INST_BGTZ
    			|| opcode == Instruction.INST_J    || opcode == Instruction.INST_JAL) {
    		operand1 = simulator.ifId.instPC;
    	}
    	else {
    		// issue with jumps and loads 
    		// resolve forwarding
    		if (simulator.idEx.regA == simulator.memWb.destReg && simulator.memWb.opcode != Instruction.INST_LW
    				&& simulator.memWb.opcode != Instruction.INST_NOP) {
   				operand1 = simulator.memWb.aluIntData;
    		} else if (simulator.idEx.regA == simulator.memWb.destReg1 && simulator.memWb.opcode1 != Instruction.INST_NOP) {
        		if (simulator.memWb.opcode1 == Instruction.INST_LW) {
        			operand1 = simulator.memWb.loadIntData1; // grabbing the just loaded data
        		} else {
        			operand1 = simulator.memWb.aluIntData1; 
        		}
        	} else {    		
        		operand1 = simulator.idEx.regAData;
        	}
    	}
    	
    	// choose operand 2
    	if (!(currInst instanceof RTypeInst))
    		operand2 = simulator.idEx.immediate;
    	else if (opcode == Instruction.INST_SLL || opcode == Instruction.INST_SRA 
    			|| opcode == Instruction.INST_SRL) {
    		operand2 = simulator.idEx.shamt;
    	}
    	else {
    		// resolve forwarding
    		if (simulator.idEx.regB == simulator.memWb.destReg && simulator.memWb.opcode != Instruction.INST_LW
    				&& simulator.memWb.opcode != Instruction.INST_NOP) {
    			operand2 = simulator.memWb.aluIntData;
    		} else if (simulator.idEx.regB == simulator.memWb.destReg1 && simulator.memWb.opcode1 != Instruction.INST_NOP) {
        		if (simulator.memWb.opcode1 == Instruction.INST_LW) {
        			operand2 = simulator.memWb.loadIntData1;
        		} else {
        			operand2 = simulator.memWb.aluIntData1;
        		}
        	} else {    		
        		operand2 = simulator.idEx.regBData;
        	}
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
