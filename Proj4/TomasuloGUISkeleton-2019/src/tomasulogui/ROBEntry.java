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
  
  int regDestTag = -1;
  boolean regDestUsed = false;

  // might need need
  int immediate = -1;

  boolean branch = false;
  boolean branchPrediction = false;
  int branchTgtTag = -1;
  int branchTgt = -1; // important for jumps
  
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
    
    boolean foundTag1 = false;
    boolean foundTag2 = false;
    int pcUsed1 = inst.pc;
    int pcUsed2 = inst.pc;
    for (int addr = 0; addr < ReorderBuffer.size; addr++) {
    	if (rob.buff[addr] != null) {
	    	if (rob.buff[addr].writeReg == inst.regSrc1 && rob.buff[addr].instPC < pcUsed1
	    			&& inst.regSrc1Used) {
	    		if (rob.buff[addr].isComplete()) {
	    			inst.regSrc1Value = rob.buff[addr].writeValue;
	    			inst.regSrc1Valid = true;
	    		} else {
	    			inst.regSrc1Tag = rob.buff[addr].regDestTag; // set the tag
	    			branchTgtTag = rob.buff[addr].regDestTag; // for JALR, JR
	    			inst.regSrc1Valid = false;
	    		}	    			
    			pcUsed1 = rob.buff[addr].instPC;

	    	}
	    	if (rob.buff[addr].writeReg == inst.regSrc2  && rob.buff[addr].instPC < pcUsed2
	    			&& inst.regSrc2Used) {
	    		if (rob.buff[addr].isComplete()) {
	    			inst.regSrc2Value = rob.buff[addr].writeValue;
	    			inst.regSrc2Valid = true;
	    		} else {
	    			inst.regSrc2Tag = rob.buff[addr].regDestTag; // set the tag
	    			inst.regSrc2Valid = false;
	    		}
	    		pcUsed2 = rob.buff[addr].instPC;

	    	}
    	}
    }
    
    regDestTag = inst.regDestTag;
    regDestUsed = inst.regDestUsed;

//    immediate = inst.immediate;

    branch = inst.branch;
    predictTaken = inst.branchPrediction;
    
    if (opcode == IssuedInst.INST_TYPE.JR || opcode == IssuedInst.INST_TYPE.JALR) {
   		branchTgt = inst.regSrc1Value;
    } else {
    	// all other branches use this and irrelevant for non branches
        branchTgt = inst.pc + 4 + inst.immediate;
    }
    
    writeReg = inst.regDest;
    opcode = inst.opcode;
    
    

    // ROB checks the tags and updates if available
    
  }

}
