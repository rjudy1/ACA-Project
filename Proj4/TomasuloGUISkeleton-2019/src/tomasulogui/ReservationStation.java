/*
 * File: ReservationStation.java
 * Authors: Aaron Johnston and Rachael Judy
 * Project: ACA Project 4 - Tomasulo
 * Notes:   added snoop functionality, worked on populating from IssuedInst
 * 
 * 
 */

package tomasulogui;

public class ReservationStation {
  PipelineSimulator simulator;

  int tag1;
  int tag2;
  int data1;
  int data2;
  boolean data1Valid = false;
  boolean data2Valid = false;
  // destTag doubles as branch tag
  int destTag;
  int destReg; //??
  IssuedInst.INST_TYPE function = IssuedInst.INST_TYPE.NOP;

  // following just for branches
  int addressTag;
  boolean addressValid = false;
  int address;
  boolean predictedTaken = false;
  
  boolean occupied = false;

  public ReservationStation(PipelineSimulator sim) {
    simulator = sim;
  }

  public int getDestTag() {
    return destTag;
  }
  
  public int getData1() {
    return data1;
  }

  public int getData2() {
    return data2;
  }

  public boolean isPredictedTaken() {
    return predictedTaken;
  }

  public IssuedInst.INST_TYPE getFunction() {
    return function;
  }

  public void snoop(CDB cdb) {
    // TODO - add code to snoop on CDB each cycle
	  if ((simulator.cdb.getDataTag() == tag1) 
			  && simulator.cdb.getDataValid()) {
		  data1 = simulator.cdb.getDataValue();
		  data1Valid = true;
	  } else if ((simulator.cdb.getDataTag() == tag2) 
			  && simulator.cdb.getDataValid()) {
		  data2 = simulator.cdb.getDataValue();
		  data2Valid = true;
	  } else if (simulator.cdb.getDataTag() == addressTag
			  && simulator.cdb.getDataValid()) {
		  address = simulator.cdb.getDataValue();
		  addressValid = true;
	  } // else if dest tag and branch?
	  // etc, might need to check if branch
	  
  }

  public boolean isReady() {
    return data1Valid && data2Valid;
  }

  public void loadInst(IssuedInst inst) {
    // TODO add code to insert inst into reservation station
	  destTag = inst.regDestTag;
	  destReg = inst.regDest; //?
	  
	  tag1 = inst.regSrc1Tag;
	  data1 = inst.regSrc1Value;
	  data1Valid = inst.regSrc1Valid && inst.regSrc1Used;
	  	  
	  tag2 = inst.regSrc2Tag;
	  data2 = inst.regSrc2Value;
	  data2Valid = inst.regSrc2Valid && inst.regSrc2Used;
	  function = inst.getOpcode();
	  // branch setup??
	  if (inst.branch) {
		  predictedTaken = inst.branchPrediction;
		  address = inst.branchTgt;
	  }
	  
  }
}
