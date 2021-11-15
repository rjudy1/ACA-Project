/*
 * authors: Aaron Johnston and Rachael Judy
 * file: ROBEntry.java
 * purpose: fill in the tag fields and modify instruction
 * changes:
 *  	
 *  	
 */

package tomasulogui;

public class ROBEntry {
  ReorderBuffer rob;

  // TODO - add many more fields into entry
  // I deleted most, and only kept those necessary to compile GUI
  boolean complete = false;
  boolean predictTaken = false;
  boolean mispredicted = false;
  int instPC = -1;
  int writeReg = -1;
  int writeValue = -1;  
  boolean resultValid = false;
  

  // seems like the fields are just the instruction fields of register ready, etc.?
  IssuedInst instr;
  
  IssuedInst.INST_TYPE opcode;

  public ROBEntry(ReorderBuffer buffer) {
    rob = buffer;
  }

  public boolean isComplete() {
    return complete;
  }

  public boolean branchMispredicted() {
    return mispredicted;
  }

  public boolean getPredictTaken() {
    return predictTaken;
  }

  public int getInstPC() {
    return instPC;
  }

  public IssuedInst.INST_TYPE getOpcode () {
    return opcode;
  }


  public boolean isHaltOpcode() {
    return (opcode == IssuedInst.INST_TYPE.HALT);
  }

  public void setBranchTaken(boolean result) {
  // TODO - maybe more than simple set
	  // if mispredicted, in reorder buffer, 
	  // will reset pc to appropriate value and flush all
	  if (result != predictTaken) {
		  mispredicted = true;
	  }
  }

  public int getWriteReg() {
    return writeReg;
  }

  public int getWriteValue() {
    return writeValue;
  }

  public void setWriteValue(int value) {
    writeValue = value;
  }

  public void copyInstData(IssuedInst inst, int frontQ) {
	// G's code
    instPC = inst.getPC();
    inst.setRegDestTag(frontQ); // just gets new entry for the destination
    
    // TODO - This is a long and complicated method, probably the most complex
    // of the project.  It does 2 things:
    // 1. update the instruction, as shown in 2nd line of code above
    // 2. update the fields of the ROBEntry, as shown in the 1st line of code above
    
    // look through all active instructions in reorder buffer to see if dest is used
    instr = inst;
    opcode = instr.opcode;
    
    boolean foundTag1 = false;
    boolean foundTag2 = false;
    int pcUsed = instr.pc;
    for (int addr = 0; addr < ReorderBuffer.size; addr++) {
    	if (rob.buff[addr] != null) {
	    	if (rob.buff[addr].instr.regDest == instr.regSrc1 && rob.buff[addr].instr.pc < pcUsed) {
	    		instr.regSrc1Tag = rob.buff[addr].instr.regDestTag; // set the tag
	    		pcUsed = rob.buff[addr].instr.pc;
	    		foundTag1 = true;
	    		instr.regSrc1Valid = false;
//	    		instr.regSrc1Used = false;
	    	}
	    	if (rob.buff[addr].instr.regDest == instr.regSrc2  && pcUsed < rob.buff[addr].instr.pc) {
	    		instr.regSrc2Tag = rob.buff[addr].instr.regDestTag; // set the tag
	    		pcUsed = rob.buff[addr].instr.pc;
	    		foundTag2 = true;
	    		instr.regSrc2Valid = false;

	    	}
    	}
    }
    
    instr.regSrc1Used = !foundTag1;
    instr.regSrc2Used = !foundTag2;
    
    writeReg = inst.regDest;
    
    // ROB checks the tags and updates if available
    
  }

}
