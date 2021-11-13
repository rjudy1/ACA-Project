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
    	    	
      // We check the BTB, and put prediction if branch, updating PC
    	// puts result from predictBranch
      //     if pred taken, incr PC otherwise
    	if (issuee.isBranch()) {
    		issuee.branch = true;
    		simulator.btb.predictBranch(issuee);
    	}
    	
    	if (issuee.branch) {
    		simulator.setPC(issuee.branchTgt);
    	} else {
    		simulator.setPC(simulator.getPC() + 4);
    	}
   
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
     		if (simulator.alu.stations[0] != null) {
     			simulator.alu.stations[0].loadInst(issuee);
     		} else if (simulator.alu.stations[1] != null) {
     			simulator.alu.stations[1].loadInst(issuee);
     		}
     		break;

    	case MUL:
     		if (simulator.multiplier.stations[0] != null) {
     			simulator.multiplier.stations[0].loadInst(issuee);
     		} else if (simulator.multiplier.stations[1] != null) {
     			simulator.multiplier.stations[1].loadInst(issuee);
     		}
     		break;
    		
    	case DIV:
     		if (simulator.divider.stations[0] != null) {
     			simulator.divider.stations[0].loadInst(issuee);
     		} else if (simulator.divider.stations[1] != null) {
     			simulator.divider.stations[1].loadInst(issuee);
     		}
     		break;
    		
    	case LOAD:
    	case STORE:
    		if (simulator.loader.isReservationStationAvail()) {
    			simulator.loader.acceptIssue(issuee);
    		}
    		break;
    		
    	case HALT:    		
    	case NOP:
    	case J:
    	case JAL:
    	case JR:
    	case JALR:
    		// straight to reorder buffer
    		break;
    	
    	case BEQ:
    	case BNE:
    	case BLTZ:
    	case BLEZ:
    	case BGEZ:
    	case BGTZ:
     		if (simulator.branchUnit.stations[0] != null) {
     			simulator.branchUnit.stations[0].loadInst(issuee);
     		} else if (simulator.branchUnit.stations[1] != null) {
     			simulator.branchUnit.stations[1].loadInst(issuee);
     		}
     		break;
    	}
    	
      // We then send this to the ROB, which fills in the data fields
    	// reorder buffer fills in the data fields and tags
    	simulator.reorder.updateInstForIssue(issuee);

      // We then check the CDB, and see if it is broadcasting data we need,
      //    so that we can forward during issue
    	// special case of checking due to sequential ordering and if ready
    	if (simulator.cdb.getDataTag() == issuee.regSrc1Tag) {
    		issuee.regSrc1Value = simulator.cdb.getDataValue();
    		issuee.regSrc1Valid = true;
    	}
    	if (simulator.cdb.getDataTag() == issuee.regSrc2Tag) {
    		issuee.regSrc2Value = simulator.cdb.getDataValue();
    		issuee.regSrc2Valid = true;
    	}

      // We then send this to the FU, who stores in reservation station
    	// functional unit has to choose the reservation station (done above)
    }

  }