/*
 * File: 	IntAlu.java
 * Authors: Aaron Johnston and Rachael Judy
 * Project: ACA Project 4 - Tomasulo
 * Notes:   who gets to say calculateResult?
 * 
 * 
 */

package tomasulogui;

import IssuedInst.INST_TYPE;

public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;
  
  public int result;
  public int destTag;
  boolean requestWriteback = false;
  boolean canWriteback = false;
  
  public void setRequestWriteback(boolean reqWB) {
  	requestWriteback = reqWB;
  }

  public int getRequestWriteBack() {
      return requestWriteback;
  }
  
  public void setCanWriteback(boolean canWB) {
  	canWriteback = canWB;
  }
  
  public int getCanWriteback() {
      return canWriteback;
  }
  
  public void squashAll() {
  	for (int i = 0; i < 2; i++) {
  		stations[i] = -1;
  	}
   	requestWriteback = false;
  }
  
  
  public IntAlu(PipelineSimulator sim) {
    super(sim);
  }


  public int calculateResult(int station) {
	if (canWriteback) {
         requestWriteback = false;
    }
    if (!requestWriteback) {
    	int operand1 = stations[station].getData1();
    	int operand2 = stations[station].getData2();
    	// ALU 
    	switch(opcode) {
    	case Instruction.INST_ADD:
    	case Instruction.INST_ADDI:
    	case Instruction.INST_SW:
    	case Instruction.INST_LW:
    		result = operand1 + operand2;
    		break;
    	case Instruction.INST_SUB:
    	case Instruction.INST_ANDI:
    		result = operand1 - operand2; 
    		break;
    	case Instruction.INST_AND:
    		result = operand1 & operand2;
    		break;
    	case Instruction.INST_OR:
    	case Instruction.INST_ORI:
    		result = operand1 | operand2;
    		break;
    	case Instruction.INST_XOR:
    	case Instruction.INST_XORI:
    		result = operand1 ^ operand2;    		
    		break;
    	case Instruction.INST_SLL:
    		result = operand1 << operand2;
    		break;
    	case Instruction.INST_SRL:
    		result = operand1 >>> operand2;
    		break;
    	case Instruction.INST_SRA:
    		result = operand1 >> operand2;
    		break;    		
    	// Are these needed?
    	case Instruction.INST_BEQ:
    	case Instruction.INST_BNE:
    	case Instruction.INST_BLEZ:
    	case Instruction.INST_BLTZ:
    	case Instruction.INST_BGEZ:
    	case Instruction.INST_BGTZ:
    	case Instruction.INST_J:
    	case Instruction.INST_JAL:
    		result = operand1 + operand2; // pc + imm
    		break;
    	case Instruction.INST_JALR:
    	case Instruction.INST_JR:
    		result = operand1; // register value
    		break;
    	}    
    	requestWriteback = true;
    	destTag = stations[station].getDestTag();
    }
	// check reservationStations for cdb data
    if (cdb.getDataValid()) {
        for (int i = 0; i < 2; i++) {
          if (stations[i] != null) {
            stations[i].snoop(cdb);
          }
        }
    }
	

  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
