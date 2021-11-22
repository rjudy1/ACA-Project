/*
 * File: 	ReorderBuffer.java
 * Authors: Aaron Johnston and Rachael Judy
 * purpose: Reorder Buffer has three main functions. The first is being able to retire a function. That writes back values
 * 			 and removes them from the ROB. We also wrote a complicated readCDB function. Much of the logic here has to do with branches
 * 			and stores. UpdateInstForIssue's main purpose is to copy the inst data into the ROB
 */

package tomasulogui;

public class ReorderBuffer {
  public static final int size = 30;
  int frontQ = 0;
  int rearQ = 0;
  ROBEntry[] buff = new ROBEntry[size];
  int numRetirees = 0;

  PipelineSimulator simulator;
  RegisterFile regs;
  boolean halted = false;

  public ReorderBuffer(PipelineSimulator sim, RegisterFile registers) {
    simulator = sim;
    regs = registers;
  }

  public ROBEntry getEntryByTag(int tag) {
    return buff[tag];
  }

  public int getInstPC(int tag) {
    return buff[tag].getInstPC();
  }

  public boolean isHalted() {
    return halted;
  }

  public boolean isFull() {
    return (frontQ == rearQ && buff[frontQ] != null);
  }

  public int getNumRetirees() {
    return numRetirees;
  }

  public boolean retireInst() {
    ROBEntry retiree = buff[frontQ];

    if (retiree == null) {
      return false;
    }

    if (retiree.isHaltOpcode() && retiree.isComplete()) {
      halted = true;
      return true;
    }
    boolean shouldAdvance = true;
    
    if (retiree.opcode != null && retiree.isComplete() || retiree.opcode == IssuedInst.INST_TYPE.NOP) {
	    switch ((IssuedInst.INST_TYPE)retiree.opcode) {
	    case ADD:
	    case ADDI:
	    case SUB:
	    case MUL:
	    case DIV:
	    case AND:
	    case ANDI:
	    case OR:
	    case ORI:
	    case XOR:
	    case XORI:
	    case SLL:
	    case SRL:
	    case SRA:
	    case LOAD:
	    	// assign to register file
	    	simulator.regs.setReg(retiree.getWriteReg(),retiree.getWriteValue());
	    	break;
	    case BEQ:    
	    case BNE:
	    case BLTZ:
	    case BLEZ:
	    case BGEZ:
	    case BGTZ:
	    	// check if was a mispredict, if so write the correct pc
	    	if (retiree.mispredicted) {
				simulator.setPC(retiree.branchTgt);
				simulator.squashAllInsts();
	    	}
	    	break;
	    case STORE:
	    	// Special exception because its never put on CDB
	    	if (retiree.storeAddrReg == 31)
	    		retiree.storeAddr = simulator.regs.getReg(31);
	    	simulator.memory.setIntDataAtAddr(retiree.storeAddr + retiree.immediate, retiree.storeValue);
	    	break;
	    case HALT:
	    	halted = true;
	    	break;
		case J:
			break;
		case JR:
			simulator.setPC(retiree.branchTgt);
			simulator.squashAllInsts();
			break;
		case JAL:
			simulator.regs.setReg(31, retiree.instPC+4);
			break;
		case JALR:
			simulator.regs.setReg(31, retiree.instPC+4);
			simulator.squashAllInsts();
			break;
	   	default:
	    }
	    
    	shouldAdvance = retiree.isComplete();
	   
    	// if mispredict branch, won't do normal advance
	    if (shouldAdvance) {
	       numRetirees++;
	       buff[frontQ] = null;
	       frontQ = (frontQ + 1) % size;
	    }
    }

    return false;
  }

  public void readCDB(CDB cdb) {
	  for (int slot = 0; slot < size; slot++) { 
		  if (buff[slot] != null && cdb.getDataValid()) { 			  
			  if (buff[slot].branchTgtTag == cdb.getDataTag() 
					  && (buff[slot].opcode == IssuedInst.INST_TYPE.JR
					  || buff[slot].opcode == IssuedInst.INST_TYPE.JALR)) {
				  buff[slot].branchTgt = cdb.getDataValue();
				  buff[slot].writeValue = cdb.getDataValue(); // for display purposes
				  buff[slot].complete = true;
				  buff[slot].resultValid = true;
			  } else if ((buff[slot].opcode == IssuedInst.INST_TYPE.BEQ || buff[slot].opcode == IssuedInst.INST_TYPE.BNE
					  || buff[slot].opcode == IssuedInst.INST_TYPE.BLTZ || buff[slot].opcode == IssuedInst.INST_TYPE.BLEZ
					  || buff[slot].opcode == IssuedInst.INST_TYPE.BGTZ || buff[slot].opcode == IssuedInst.INST_TYPE.BGEZ)
						  && cdb.getDataTag() == -2
						  && simulator.branchUnit.stations[cdb.getDataValue()].pc == buff[slot].instPC) {
				  // shorting from branch unit
				  buff[slot].branchTgt = simulator.branchUnit.stations[cdb.getDataValue()].address;
				  buff[slot].complete = true;
				  buff[slot].writeValue = buff[slot].branchTgt;
				  buff[slot].setBranchTaken(simulator.branchUnit.stations[cdb.getDataValue()].isTaken);
			  } else if (buff[slot].opcode == IssuedInst.INST_TYPE.STORE) {
				  if (buff[slot].storeAddrTag == cdb.getDataTag()) { 			  
					  buff[slot].storeAddr = cdb.getDataValue();
					  buff[slot].storeAddrValid = true;
				  } else if (buff[slot].storeValueTag == cdb.getDataTag()) {
					  buff[slot].storeValue = cdb.getDataValue();
					  buff[slot].storeValueValid = true;
				  }
				  buff[slot].complete = buff[slot].storeAddrValid && buff[slot].storeValueValid;
			  } else if (buff[slot].regDestTag == cdb.getDataTag()) {		  
				  buff[slot].writeValue = cdb.getDataValue();
				  buff[slot].resultValid = true;
				  buff[slot].complete = true;
			  }
		  }
	  }	  
  }

  public void updateInstForIssue(IssuedInst inst) {
    // the task is to simply annotate the register fields
    // the dest reg will be assigned a tag, which is just our slot#
    // all src regs will either be assigned a tag, read from reg, or forwarded from ROB

    // TODO - possibly nothing if you use my model
    // I use the call to copyInstData below to do 2 things:
    // 1. update the Issued Inst
    // 2. fill in the ROB entry

    // first get a ROB slot
    if (buff[rearQ] != null) {
      throw new MIPSException("updateInstForIssue: no ROB slot avail");
    }
    ROBEntry newEntry = new ROBEntry(this);
    buff[rearQ] = newEntry;
    newEntry.copyInstData(inst, rearQ);

    rearQ = (rearQ + 1) % size;
  }

  public int getTagForReg(int regNum) {
    return (regs.getSlotForReg(regNum));
  }

  public int getDataForReg(int regNum) {
    return (regs.getReg(regNum));
  }

  public void setTagForReg(int regNum, int tag) {
    regs.setSlotForReg(regNum, tag);
  }

  public void squashAll() {
	  for (int addr = 0; addr < ReorderBuffer.size; addr++) {
		  		buff[addr] = null;
	  }
	  frontQ = (rearQ - 1 + 30) % size;
  }
}
