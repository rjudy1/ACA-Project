package tomasulogui;

public class BranchUnit
        extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;
    int destTag;
    int destVal;

    public BranchUnit(PipelineSimulator sim) {
        super(sim);
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

    public int calculateResult(int station) {
    	if (canWriteback) {
            requestWriteback = false;
       }
       if (!requestWriteback) {
	       	int operand1 = stations[station].getData1();
	       	int operand2 = stations[station].getData2();
	       	boolean branchTaken;
	       	// ALU 
	       	switch(stations[station].getFunction()) {	
	       	case BEQ:
	       		branchTaken = operand1 == operand2;
	       		break;
	       	case BNE:
	       		branchTaken = operand1 != operand2;
	       		break;
	       	case BLEZ:
	      		branchTaken = operand1 < 0;
	       		break;
	       	case BLTZ:
	       		branchTaken = operand1 <= 0;
	       		break;
	       	case BGEZ:
	       		branchTaken = operand1 >= 0;
	       		break;
	       	case BGTZ:
	       		branchTaken = operand1 > 0;
	       		break;       		
	//       	case J: // jumps just get to to reorder buffer
	//       	case JAL:
	//       	case JALR:
	//       	case Instruction.INST_JR:
	//       		result = operand1; // register value
	//       		break;
	   		default:
	   			break;
	       	}    
	       	requestWriteback = true;
	       	destTag = stations[station].getDestTag();
	       	destVal = simulator.getPC() + operand2; // set target
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
