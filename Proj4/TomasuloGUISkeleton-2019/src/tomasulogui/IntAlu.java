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
  
  public int destVal;
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
  
  public int getDestTag() {
      return destTag;
  }
  
  public void setDestTag(int dTag) {
  	destTag = dTag;
  }
  
  public int getDestValue() {
      return destVal;
  }
  
  public void setDestValue(int dVal) {
  	destVal = dVal;
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
    		destVal = operand1 + operand2;
    		break;
    	case SUB:
    	case ANDI:
    		destVal = operand1 - operand2; 
    		break;
    	case AND:
    		destVal = operand1 & operand2;
    		break;
    	case OR:
    	case ORI:
    		destVal = operand1 | operand2;
    		break;
    	case XOR:
    	case XORI:
    		destVal = operand1 ^ operand2;    		
    		break;
    	case SLL:
    		destVal = operand1 << operand2;
    		break;
    	case SRL:
    		destVal = operand1 >>> operand2;
    		break;
    	case SRA:
    		destVal = operand1 >> operand2;
    		break;    		
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
	return destVal;

  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
