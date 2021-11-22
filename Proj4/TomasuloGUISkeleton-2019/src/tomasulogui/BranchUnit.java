/*
 * authors: Aaron Johnston and Rachael Judy
 * file: BranchUnit.java
 * purpose: figure out if the branch is taken based on the operands
 * 			using backdoor to access the branch resolution target and isTaken
 *  	
 *  	
 */

package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
    	if (canWriteback) {
            requestWriteback = false;
    	}
    	if (!requestWriteback) {
	       	int operand1 = stations[station].getData1();
	       	int operand2 = stations[station].getData2();
	       	
     		// determine if branch taken
	       	switch(stations[station].getFunction()) {	
	       	case BEQ:
	       		stations[station].isTaken = operand1 == operand2;
	       		break;
	       	case BNE:
	       		stations[station].isTaken = operand1 != operand2;
	       		break;
	       	case BLEZ:
	       		stations[station].isTaken = operand1 <= 0;
	       		break;
	       	case BLTZ:
	       		stations[station].isTaken = operand1 < 0;
	       		break;
	       	case BGEZ:
	       		stations[station].isTaken = operand1 >= 0;
	       		break;
	       	case BGTZ:
	       		stations[station].isTaken = operand1 > 0;
	       		break;       		
	   		default:
	   			break;
	       	}
	       	
	       	// set in btb
	       	simulator.btb.setBranchResult(stations[station].pc, stations[station].isTaken);
	       	
	       	// if not taken, address will be pc+4, 
	       	// if taken, address will be address+4+imm
	       	// only gets used if mispredicted
       		stations[station].address = stations[station].pc + 4;
	       	if (stations[station].isTaken)
	       		stations[station].address += stations[station].immediate;

       		requestWriteback = true;
	       	destTag = -2;// use as a special flag to indicate a branch target just resolved
	       	destVal = station; // set target
	       	stationDone = station;
       }
    	
       
       return destVal;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
