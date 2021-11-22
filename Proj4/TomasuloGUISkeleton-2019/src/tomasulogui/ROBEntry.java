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
    storeAddr = inst.regSrc1Value;
    storeValue = inst.regSrc2Value;
    storeValueValid = true;
    storeAddrValid = true;
    storeAddrReg = inst.regSrc1;
    branchTgt = inst.regSrc1Value;
 
    int pcUsed1 = -1;
    int pcUsed2 = -1;
    for (int addr = 0; addr < ReorderBuffer.size; addr++) {
    	if (rob.buff[addr] != null) {
	    	if (rob.buff[addr].writeReg == inst.regSrc1 && rob.buff[addr].instPC > pcUsed1
	    			&& rob.buff[addr].instPC < inst.pc	&& inst.regSrc1Used) {
	    		if (rob.buff[addr].isComplete()) {
	    			inst.regSrc1Value = rob.buff[addr].writeValue;
	    			inst.regSrc1Valid = true;
		      		branchTgt = rob.buff[addr].writeValue;

	    			storeAddr = rob.buff[addr].writeValue;
	    			storeAddrValid = true;
	    		} else if (rob.simulator.cdb.getDataTag() == inst.regSrc1Tag && rob.simulator.cdb.dataValid) {
		      		inst.regSrc1Value = rob.simulator.cdb.getDataValue();
		      		inst.regSrc1Valid = true;
		      		branchTgt = rob.simulator.cdb.getDataValue();
	    			storeAddr = rob.simulator.cdb.getDataValue();
	    			storeAddrValid = true;
	    		} else {
	    			inst.regSrc1Tag = rob.buff[addr].regDestTag; // set the tag
	    			branchTgtTag = rob.buff[addr].regDestTag; // for JALR, JR
	    			inst.regSrc1Valid = false;
	    			storeAddrTag = rob.buff[addr].regDestTag;
	    			storeAddrValid = false;
	    		}	    			
    			pcUsed1 = rob.buff[addr].instPC;

	    	}
	    	if (rob.buff[addr].writeReg == inst.regSrc2 && rob.buff[addr].instPC > pcUsed2
	    			&& rob.buff[addr].instPC < inst.pc && inst.regSrc2Used) {
	    		if (rob.buff[addr].isComplete()) {
	    			inst.regSrc2Value = rob.buff[addr].writeValue;
	    			storeValue = rob.buff[addr].writeValue;
	    			inst.regSrc2Valid = true;
	    			storeValueValid = true;
	    		} else if (rob.simulator.cdb.getDataTag() == inst.regSrc2Tag && rob.simulator.cdb.dataValid) {
		      		inst.regSrc2Value = rob.simulator.cdb.getDataValue();
		      		inst.regSrc2Valid = true;
	    			storeValue = rob.simulator.cdb.getDataValue();
	    			storeValueValid = true;
	    		} else {
	    			inst.regSrc2Tag = rob.buff[addr].regDestTag; // set the tag
	    			storeValueTag = rob.buff[addr].regDestTag;
	    			inst.regSrc2Valid = (rob.buff[addr].opcode == IssuedInst.INST_TYPE.STORE); // should be false unless its a store and then this field is bypassed in unit
	    			storeValueValid = false;
	    		}
	    		pcUsed2 = rob.buff[addr].instPC;

	    	}
    	}
    }
    /*
     * 	      	if (simulator.cdb.getDataTag() == issuee.regSrc1Tag && simulator.cdb.dataValid) {
	      		issuee.regSrc1Value = simulator.cdb.getDataValue();
	      		issuee.regSrc1Valid = true;
	      	}
	      	if (simulator.cdb.getDataTag() == issuee.regSrc2Tag && simulator.cdb.dataValid) {
	      		issuee.regSrc2Value = simulator.cdb.getDataValue();
	      		issuee.regSrc2Valid = true;
	      	}
	
     */
    
    // allows special exception for tagging store/jalr/jal outputs
    regDestTag = inst.regDestTag;
    regDestUsed = inst.regDestUsed;

    
//    immediate = inst.immediate;

    branch = inst.branch;
    predictTaken = inst.branchPrediction;
    
    complete = inst.getOpcode() == IssuedInst.INST_TYPE.J || 
    		   inst.getOpcode() == IssuedInst.INST_TYPE.JAL ||
    		   inst.getOpcode() == IssuedInst.INST_TYPE.NOP ||
    		   inst.getOpcode() == IssuedInst.INST_TYPE.JR && inst.regSrc1Valid ||
    		   inst.getOpcode() == IssuedInst.INST_TYPE.JALR && inst.regSrc1Valid || 
    		   inst.getOpcode() == IssuedInst.INST_TYPE.STORE && storeValueValid && storeAddrValid ||
    		   inst.getOpcode() == IssuedInst.INST_TYPE.HALT;
    

    writeReg = inst.regDest;
    opcode = inst.opcode;
    immediate = inst.immediate;
    writeValue = inst.pc+4;// defaults to this for jal/jalr

    // ROB checks the tags and updates if available
    
  }

}
