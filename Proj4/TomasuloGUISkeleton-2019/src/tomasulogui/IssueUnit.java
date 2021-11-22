/*
 * File: 	IssueUnit.java
 * Authors: Aaron Johnston and Rachael Judy
 * purpose: This file issues an instruction. The first step is to ensure that a reservation station is available
 * 			from here, we issue the instruction and send it to the ROB, who checks and creates tags as necessary
 * 			After this, it is sent to its specific FU.
 * 
 */

package tomasulogui;

public class IssueUnit {
  private enum EXEC_TYPE {
    NONE, LOAD, ALU, MULT, DIV, BRANCH} ;

    PipelineSimulator simulator;
    IssuedInst issuee;
    boolean issued;
    Object fu;

    public IssueUnit(PipelineSimulator sim) {
      simulator = sim;
    }

    public void execCycle() {
    	// an execution cycle involves:
    	// 1. checking if ROB and Reservation Station avail
    	// 2. issuing to reservation station, if no structural hazard
    	// put issued instruction in reservation station and tag with number and registers as necessary

    	// to issue, we make an IssuedInst, filling in what we know
    	Instruction inst = simulator.memory.getInstAtAddr(simulator.getPC());
    	issuee = IssuedInst.createIssuedInst(inst);
    	issuee.pc = simulator.getPC();
    	
    	// set flag to indicate is a branch
    	if (issuee.determineIfBranch()) {
    		issuee.setBranch();
    	}
    	issued = false;
    	if (!simulator.reorder.isFull()) {
	    	switch (issuee.getOpcode()) {
	    	case ADD:
	    	case ADDI:
	    	case SUB:
	    	case AND:
	    	case ANDI:
	    	case OR:
	    	case ORI:
	    	case XOR:
	    	case XORI:
	       	case SLL:
	    	case SRL:
	    	case SRA:
	    		issued = simulator.alu.isReservationStationAvailable();
	     		break;
	
	    	case MUL:
	    		issued = simulator.multiplier.isReservationStationAvailable();
	     		break;
	    		
	    	case DIV:
	    		issued = simulator.divider.isReservationStationAvailable();
	     		break;
	    		
	    	case LOAD:
	    		issued = simulator.loader.isReservationStationAvail();
	    		break;
	    	case STORE:
	    	case HALT:    		
	    	case NOP:
	    	case J:
	    	case JR:
	    		issued = true;
	    		break;
	    	case JAL:
	    	case JALR:
	    		// straight to reorder buffer
	    		// We used 31 so that it is able to catch during forwarding
	    		issuee.regDest = 31; 
	    		issued = true;
	    		break;
	    	case BEQ:
	    	case BNE:
	    	case BLTZ:
	    	case BLEZ:
	    	case BGEZ:
	    	case BGTZ:
	    		issued = simulator.branchUnit.isReservationStationAvailable();
	     		break;
	    	}
    	}
    	
    	if (issued) {
    		// Instruction Decode pretagging
    	 	if (issuee.regSrc1Used) {
	    		issuee.regSrc1Value = simulator.regs.getReg(issuee.regSrc1);
	    	}
	    	if (issuee.regSrc2Used) {
	    		issuee.regSrc2Value = simulator.regs.getReg(issuee.regSrc2);
	    	}
	    	// true until we check the tags
			issuee.regSrc1Valid = true;
			issuee.regSrc2Valid = true;
	    	
	    	// We check the BTB, and put prediction if branch, updating PC
	    	// puts result from predictBranch
	    	// if pred taken, incr PC otherwise
	    	if (issuee.isBranch()) {
	    		simulator.btb.predictBranch(issuee);
	    	}
	    	
	        // We then send this to the ROB, which fills in the data fields
	      	// reorder buffer fills in the data fields and tags
	    	// only create new instruction if it hasn't gotten stuck
    		simulator.reorder.updateInstForIssue(issuee);
	
	        // We then check the CDB, and see if it is broadcasting data we need,
	        //    so that we can forward during issue - done during update now
	
	        // We then send this to the FU, who stores in reservation station
	      	// functional unit has to choose the reservation station
	    	switch (issuee.getOpcode()) {
	    	case ADD:
	    	case ADDI:
	    	case SUB:
	    	case AND:
	    	case ANDI:
	    	case OR:
	    	case ORI:
	    	case XOR:
	    	case XORI:
	       	case SLL:
	    	case SRL:
	    	case SRA:
	    		simulator.alu.acceptIssue(issuee);
	     		break;
	    	case MUL:
	    		simulator.multiplier.acceptIssue(issuee);
	     		break;		
	    	case DIV:
	    		simulator.divider.acceptIssue(issuee);
	     		break;
	    	case LOAD:
	    		simulator.loader.acceptIssue(issuee);
	    		break;
	    	case STORE:
	    		// straight to reorder
	    		break;
	    	case BEQ:
	    	case BNE:
	    	case BLTZ:
	    	case BLEZ:
	    	case BGEZ:
	    	case BGTZ:
	    		simulator.branchUnit.acceptIssue(issuee);
	    		break;
	     		// branches fall through to jump because immediate is the same
	    	case HALT:    		
	    	case NOP:
	    	case J:
	    	case JAL:
	    		// straight to reorder buffer
	    		break;
	    	case JR:
	    	case JALR:
	    		break;
	    	}
	    	if (!issuee.isBranch())
	    		simulator.setPC(simulator.getPC() + 4);
    	}
    }

  }