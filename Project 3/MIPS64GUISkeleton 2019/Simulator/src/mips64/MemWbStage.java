package mips64;

public class MemWbStage {

    PipelineSimulator simulator;
    boolean halted;
    boolean shouldWriteback = false;
    int instPC=-1;
    int opcode=1;
    int aluIntData=0;
    int loadIntData=-1;
    int destReg=32;
    int storeIntData=-1;
    
    // Duplicate variables
    boolean shouldWriteback1 = false;
    int destReg1 = 32;
    int aluIntData1;
    int loadIntData1;
    int opcode1;
    int storeIntData1;
    
    public MemWbStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public boolean isHalted() {
        return halted;
    }

    public void update() {
    	// save the previous values for the writeback/ doing double duty here
    	shouldWriteback1 = shouldWriteback;
    	destReg1 = destReg;
    	aluIntData1 = aluIntData;
    	loadIntData1 = loadIntData;
    	storeIntData1 = storeIntData;
    	opcode1 = opcode;
    
    	// Assign the new values to the register
    	shouldWriteback = simulator.exMem.shouldWriteback;
    	instPC = simulator.exMem.instPC;
    	opcode = simulator.exMem.opcode;
    	aluIntData= simulator.exMem.aluIntData;
    	storeIntData = simulator.exMem.storeIntData;
    	destReg = simulator.exMem.destReg;
    	if (shouldWriteback) {
	    	if (opcode == Instruction.INST_LW) {
	    		loadIntData = simulator.memory.getIntDataAtAddr(aluIntData);
	    	} else if (opcode == Instruction.INST_SW) {
	    		simulator.memory.setIntDataAtAddr(aluIntData, storeIntData);  
	    	}
    	}
    	
    	if (shouldWriteback1) {
    		if ( opcode1==Instruction.INST_HALT) {
    			halted = true;
    		}
   			else if(opcode1==Instruction.INST_LW) {
    			simulator.regFile[destReg1] = loadIntData1; // occurs after mem
    		}
   			else if(opcode1==Instruction.INST_SW) {
   				// nothing, but must exclude from the default else
   			}
    		else if (opcode1 ==Instruction.INST_JALR || opcode1 == Instruction.INST_JAL)
    			simulator.regFile[destReg1] = simulator.exMem.instPC;    		
    		else {	// Anything thats not a memory op, halt, or jump and link
    			simulator.regFile[destReg1] = aluIntData1;
    		}
	
    	}
//    	
//    	if (shouldWriteback) {
//    		if ( opcode==Instruction.INST_HALT) {
//    			halted = true;
//    		}
//   			else if(opcode==Instruction.INST_LW) {
//    			loadIntData = simulator.memory.getIntDataAtAddr(aluIntData);
//    			simulator.regFile[destReg] = loadIntData; // occurs after mem
//    		}
//    		else if (opcode==Instruction.INST_SW) {	
//    			simulator.memory.setIntDataAtAddr(aluIntData, storeIntData);    		
//    		}
//    		else if (opcode ==Instruction.INST_JALR || opcode == Instruction.INST_JAL)
//    			simulator.regFile[destReg] = simulator.idEx.instPC;
//    		
//    		// Anything thats not a memory op, halt, or jump and link
//    		else {
//    			simulator.regFile[destReg] = aluIntData;
//    		}
//    	}

    }
}
