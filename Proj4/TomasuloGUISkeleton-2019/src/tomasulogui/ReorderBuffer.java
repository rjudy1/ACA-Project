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

    if (retiree.isHaltOpcode()) {
      halted = true;
      return true;
    }
    boolean shouldAdvance = true;

    // TODO - this is where you look at the type of instruction and
    // figure out how to retire it properly
    // case statement
    switch ((IssuedInst.INST_TYPE)buff[0].opcode) {
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
    		simulator.setPC(retiree.getWriteValue()); 
    		shouldAdvance = false; // Is this right?
    	}
    	break;
    case STORE:
    	// put in memory if not cleared/voided
    	simulator.memory.setIntDataAtAddr(retiree.getWriteReg(), retiree.getWriteValue());
    	break;
    case HALT:
    	halted = true;
    	break;
   	default:
    		
    }

      // if mispredict branch, won't do normal advance
      if (shouldAdvance) {
        numRetirees++;
        buff[frontQ] = null;
        frontQ = (frontQ + 1) % size;
      }

    return false;
  }

  public void readCDB(CDB cdb) {
    // check entire CDB for someone waiting on this data
    // could be destination reg
    // could be store address source
	  // iterate through the reorder buffer entries looking for the tag being broadcast, sort of like snoop
	  // check CDB destination register done or address source done
	  
    // TODO body of method
	  
	  
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

}
