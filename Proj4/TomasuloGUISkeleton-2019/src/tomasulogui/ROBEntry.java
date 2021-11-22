/*
 * authors: Aaron Johnston and Rachael Judy
 * file: ROBEntry.java
 * purpose: fill in the tag fields and modify instruction
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
  
  // needed in the branch retrieval and snooping
  int instPC = -1;
  int writeReg = -1;
  int writeValue = -1;
  
  int regDestTag = -1;

  // needed in branch unit
  int immediate = -1;

  boolean branch = false;
  int branchTgtTag = -1;
  int branchTgt = -1; // important for jumps
  
  // for stores
  int storeAddrReg=-1;
  int storeAddr = -1;
  int storeAddrTag = -1;
  int storeValueTag = -1;
  int storeValue = -1;
  boolean storeAddrValid = false;
  boolean storeValueValid = false;
  
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
//	  mispredicted = result != predictTaken;
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
    // division of responsibility for snooping to functional unit and rob
    // default storeValueValid as true like the registers
    // store the defaults
    storeAddr = inst.regSrc1Value; 
    storeAddrValid = true;
    storeValue = inst.regSrc2Value;
    storeValueValid = true;
    storeAddrReg = inst.regSrc1;
    branchTgt = inst.regSrc1Value;
 
    for (int addr = (frontQ + 1) % ReorderBuffer.size; addr != frontQ; addr = (addr + 1) % ReorderBuffer.size) {
    	if (rob.buff[addr] != null) {
    		// if writeRegister in buffer matches the source we want
	    	if (rob.buff[addr].writeReg == inst.regSrc1 && inst.regSrc1Used) {
	    		// if complete and in reorder buffer still
	    		if (rob.buff[addr].isComplete()) {
	    			inst.regSrc1Value = rob.buff[addr].writeValue;
	    			inst.regSrc1Valid = true;

	    			branchTgt = rob.buff[addr].writeValue;
	    			storeAddr = rob.buff[addr].writeValue;
	    			storeAddrValid = true;
	    			
	    		// if is in rob but isn't complete, must get tag
	    		} else {
	    			inst.regSrc1Tag = rob.buff[addr].regDestTag; // set the tag
	    			branchTgtTag = rob.buff[addr].regDestTag; // for JALR, JR
	    			inst.regSrc1Valid = false;

	    			storeAddrTag = rob.buff[addr].regDestTag;
	    			storeAddrValid = false;
	    			
	    			// case if on cdb right now but isn't marked complete
	    			if (rob.simulator.cdb.getDataTag() == inst.regSrc1Tag && rob.simulator.cdb.dataValid) {
			      		inst.regSrc1Value = rob.simulator.cdb.getDataValue();
			      		inst.regSrc1Valid = true;
			      		branchTgt = rob.simulator.cdb.getDataValue();
		    			storeAddr = rob.simulator.cdb.getDataValue();
		    			storeAddrValid = true;
	    			}
	    		}	 
	    	}
	    	// repeat of source 1
	    	if (rob.buff[addr].writeReg == inst.regSrc2 && inst.regSrc2Used) {
	    		if (rob.buff[addr].isComplete()) {
	    			inst.regSrc2Value = rob.buff[addr].writeValue;
	    			inst.regSrc2Valid = true;

	    			storeValue = rob.buff[addr].writeValue;
	    			storeValueValid = true;
	    		} else {
	    			inst.regSrc2Tag = rob.buff[addr].regDestTag; // set the tag
	    			inst.regSrc2Valid = (rob.buff[addr].opcode == IssuedInst.INST_TYPE.STORE); 
	    			// should be false unless its a store and then this field is bypassed in unit

	    			storeValueTag = rob.buff[addr].regDestTag;
	    			storeValueValid = false;
	    			if (rob.simulator.cdb.getDataTag() == inst.regSrc2Tag && rob.simulator.cdb.dataValid) {
			      		inst.regSrc2Value = rob.simulator.cdb.getDataValue();
			      		inst.regSrc2Valid = true;
		    			storeValue = rob.simulator.cdb.getDataValue();
		    			storeValueValid = true;
	    			}
	    		}

	    	}
    	}
    }
    
    // allows special exception for tagging store/jalr/jal outputs
    regDestTag = inst.regDestTag;
    writeReg = inst.regDest;
    writeValue = inst.pc+4;// defaults to this for jal/jalr

    branch = inst.branch;
    predictTaken = inst.branchPrediction;  

    opcode = inst.opcode;
    immediate = inst.immediate;
    complete = inst.getOpcode() == IssuedInst.INST_TYPE.J || 
 		   inst.getOpcode() == IssuedInst.INST_TYPE.JAL ||
 		   inst.getOpcode() == IssuedInst.INST_TYPE.NOP ||
 		   inst.getOpcode() == IssuedInst.INST_TYPE.JR && inst.regSrc1Valid ||
 		   inst.getOpcode() == IssuedInst.INST_TYPE.JALR && inst.regSrc1Valid || 
 		   inst.getOpcode() == IssuedInst.INST_TYPE.STORE && storeValueValid && storeAddrValid ||
 		   inst.getOpcode() == IssuedInst.INST_TYPE.HALT;

    // ROB checks the tags and updates if available
    
  }

}
