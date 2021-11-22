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
	       		       	// ALU 
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
	       	
	       	simulator.btb.setBranchResult(stations[station].pc, stations[station].isTaken);
//	       	simulator.getROB().buff[stations[station].destTag].setBranchTaken(stations[station].isTaken);
	       	
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
