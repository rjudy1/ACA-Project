/*
 * authors: Aaron Johnston and Rachael Judy
 * file: IntAlu.java
 * purpose: handle the various alu operations like adds, shifts, etc
 *  	
 *  	
 */
package tomasulogui;


public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;
  
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
    if (!requestWriteback) {
    	// fetch operands from the stations
    	int operand1 = stations[station].getData1();
    	int operand2 = stations[station].getData2();
    	int imm = stations[station].immediate;

    	// ALU 
    	// calculate result and tell CDB we're ready
    	switch(stations[station].getFunction()) {
    	case ADD:
    		destVal = operand1 + operand2;
    		break;
    	case ADDI:
    		destVal = operand1 + imm;
    		break;
    	case SUB:
    		destVal = operand1 - operand2; 
    		break;
    	case ANDI:
    		destVal = operand1 & imm;
    		break;
    	case AND:
    		destVal = operand1 & operand2;
    		break;
    	case OR:
    		destVal = operand1 | operand2;
    		break;
    	case ORI:
    		destVal = operand1 | imm;
    		break;
    	case XOR:
    		destVal = operand1 ^ operand2;    		
    		break;
    	case XORI:
    		destVal = operand1 ^ imm;    		
    		break;
    	case SLL:
    		destVal = operand1 << imm;
    		break;
    	case SRL:
    		destVal = operand1 >>> imm;
    		break;
    	case SRA:
    		destVal = operand1 >> imm;
    		break;    		
		default:
			break;
    	}    
    	requestWriteback = true;
    	destTag = stations[station].getDestTag();
    	stationDone = station;
    }

	return destVal;

  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
