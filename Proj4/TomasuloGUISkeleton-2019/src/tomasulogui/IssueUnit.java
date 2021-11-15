/*
 * File: 	IssueUnit.java
 * Authors: Aaron Johnston and Rachael Judy
 * Project: ACA Project 4 - Tomasulo
 * Notes:   
 * 
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
    	// put issued instruction in reservation station and tag with number and registers as necssary

    	// to issue, we make an IssuedInst, filling in what we know
    	// fetch
    	Instruction inst = simulator.memory.getInstAtAddr(simulator.getPC());
    	issuee = IssuedInst.createIssuedInst(inst);
    	issuee.pc = simulator.getPC();

    	// ask G about cycle when to do this
    	if (issuee.regSrc1Used)
    		issuee.regSrc1Value = simulator.regs.getReg(issuee.regSrc1);
    		issuee.regSrc1Valid = true;
    	if (issuee.regSrc2Used) {
    		issuee.regSrc2Value = simulator.regs.getReg(issuee.regSrc2);
    		issuee.regSrc2Valid = true;
    	} else if (inst instanceof ITypeInst) {
    		issuee.regSrc2Value = issuee.immediate;
    		issuee.regSrc2Valid = true;
    	}
    	
    	
    	// We check the BTB, and put prediction if branch, updating PC
    	// puts result from predictBranch
    	//     if pred taken, incr PC otherwise
    	if (issuee.isBranch()) {
    		issuee.branch = true;
    		simulator.btb.predictBranch(issuee);
    	}

        // We then send this to the ROB, which fills in the data fields
      	// reorder buffer fills in the data fields and tags
      	simulator.reorder.updateInstForIssue(issuee);

        // We then check the CDB, and see if it is broadcasting data we need,
        //    so that we can forward during issue
      	// special case of checking due to sequential ordering and if ready
      	if (simulator.cdb.getDataTag() == issuee.regSrc1Tag && simulator.cdb.dataValid) {
      		issuee.regSrc1Value = simulator.cdb.getDataValue();
      		issuee.regSrc1Valid = true;
      	}
      	if (simulator.cdb.getDataTag() == issuee.regSrc2Tag && simulator.cdb.dataValid) {
      		issuee.regSrc2Value = simulator.cdb.getDataValue();
      		issuee.regSrc2Valid = true;
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
	    		for (int i = 0; i < 2; i++) {
	    			if (simulator.alu.stations[i] == null) {
	    				simulator.alu.acceptIssue(issuee);
	    				issued = true;
	    			} 
	     		}
	     		break;
	
	    	case MUL:
	    		for (int i = 0; i < 2; i++) {
	    			if (simulator.multiplier.stations[i] == null) {
	    				simulator.multiplier.acceptIssue(issuee);
	    				issued = true;
	    			}
	    		}
	     		break;
	    		
	    	case DIV:
	    		for (int i = 0; i < 2; i++) {
		     		if (simulator.divider.stations[i] == null) {
		     			simulator.divider.acceptIssue(issuee);
		     			issued = true;
		     		} 
	     		}
	     		break;
	    		
	    	case LOAD:
	    	case STORE:
	    		if (simulator.loader.isReservationStationAvail()) {
	    			simulator.loader.acceptIssue(issuee);
	    			issued = true;
	    		}
	    		break;
	    		
	    	case HALT:    		
	    	case NOP:
	    	case J:
	    	case JAL:
	    	case JR:
	    	case JALR:
	    		// straight to reorder buffer
	    		issued = true;
	    		break;
	    	
	    	case BEQ:
	    	case BNE:
	    	case BLTZ:
	    	case BLEZ:
	    	case BGEZ:
	    	case BGTZ:
	    		for (int i = 0; i < 2; i++) {
		     		if (simulator.branchUnit.stations[i] == null) {
		     			simulator.branchUnit.acceptIssue(issuee);
		     			issued = true;
		     		}
	    		}
	     		break;
	    	}
    	}
    	
    	if (issuee.branch) {
    		simulator.setPC(issuee.branchTgt);
    	} else if (issued) {
    		simulator.setPC(simulator.getPC() + 4);
    	}
      // We then send this to the FU, who stores in reservation station
    	// functional unit has to choose the reservation station (done above)
    }

  }