/*
 * File: 	IntAlu.java
 * Authors: Aaron Johnston and Rachael Judy
 * Project: ACA Project 4 - Tomasulo
 * Notes:   who gets to say calculateResult?
 * 
 * 
 */

package tomasulogui;


public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;
  
  public int result;
  public int destTag;
  boolean requestWriteback = false;
  boolean canWriteback = false;
  
  public void setRequestWriteback(boolean reqWB) {
  	requestWriteback = reqWB;
  }

  public boolean getRequestWriteBack() {
      return requestWriteback;
  }
  
  public void setCanWriteback(boolean canWB) {
  	canWriteback = canWB;
  }
  
  public boolean getCanWriteback() {
      return canWriteback;
  }
  
  public void squashAll() {
  	for (int i = 0; i < 2; i++) {
  		stations[i] = null;
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
    	switch(stations[station].getFunction()) {
    	case ADD:
    	case ADDI:
    	case STORE:
    	case LOAD:
    		result = operand1 + operand2;
    		break;
    	case SUB:
    	case ANDI:
    		result = operand1 - operand2; 
    		break;
    	case AND:
    		result = operand1 & operand2;
    		break;
    	case OR:
    	case ORI:
    		result = operand1 | operand2;
    		break;
    	case XOR:
    	case XORI:
    		result = operand1 ^ operand2;    		
    		break;
    	case SLL:
    		result = operand1 << operand2;
    		break;
    	case SRL:
    		result = operand1 >>> operand2;
    		break;
    	case SRA:
    		result = operand1 >> operand2;
    		break;    		
//    	case Instruction.INST_BEQ:
//    	case Instruction.INST_BNE:
//    	case Instruction.INST_BLEZ:
//    	case Instruction.INST_BLTZ:
//    	case Instruction.INST_BGEZ:
//    	case Instruction.INST_BGTZ:
//    	case Instruction.INST_J:
//    	case Instruction.INST_JAL:
//    		result = operand1 + operand2; // pc + imm
//    		break;
//    	case Instruction.INST_JALR:
//    	case Instruction.INST_JR:
//    		result = operand1; // register value
//    		break;
		default:
			break;
    	}    
    	requestWriteback = true;
    	destTag = stations[station].getDestTag();
    }
	// check reservationStations for cdb data
    if (simulator.cdb.getDataValid()) {
        for (int i = 0; i < 2; i++) {
          if (stations[i] != null) {
            stations[i].snoop(simulator.cdb);
          }
        }
    }
	return result;

  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
