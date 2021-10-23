package mips64;

public class MemWbStage {

    PipelineSimulator simulator;
    boolean halted;
    boolean shouldWriteback = false;
    int instPC=-1;
    int opcode=1;
    int aluIntData=0;
    int loadIntData=-1;
    int destReg=0;
    int storeIntData=-1;
    
    // Duplicate variables
    boolean shouldWriteback1 = false;
    int destReg1;
    int aluIntData1;
    int loadIntData1;
    
    public MemWbStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public boolean isHalted() {
        return halted;
    }

    public void update() {
    	if (shouldWriteback) {
    		if ( opcode==Instruction.INST_HALT) {
    			halted = true;
    		}
   			else if(opcode==Instruction.INST_LW) {
    			loadIntData = simulator.memory.getIntDataAtAddr(aluIntData);
    			simulator.regFile[destReg] = loadIntData;
    		}
    		else if (opcode==Instruction.INST_SW) {	
    			simulator.memory.setIntDataAtAddr(aluIntData, simulator.exMem.storeIntData);    		
    		}
    		else if (opcode ==Instruction.INST_JALR || opcode == Instruction.INST_JAL)
    			simulator.regFile[destReg] = simulator.exMem.instPC;
    		
    		// Anything thats not a memory op, halt, or jump and link
    		else {
    			simulator.regFile[destReg] = aluIntData;
    		}
    	}
    	// Assign values
    	shouldWriteback1 = shouldWriteback;
    	destReg1 = destReg;
    	aluIntData1 = aluIntData;
    	loadIntData1 = loadIntData;
    
    	// Assign the new values
    	shouldWriteback = simulator.exMem.shouldWriteback;
    	instPC = simulator.exMem.instPC;
    	opcode = simulator.exMem.opcode;
    	aluIntData= simulator.exMem.aluIntData;
    	storeIntData = simulator.exMem.storeIntData;
    	destReg = simulator.exMem.destReg;
    }
}
