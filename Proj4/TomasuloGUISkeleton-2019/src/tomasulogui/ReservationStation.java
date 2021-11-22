/*
 * File: ReservationStation.java
 * Authors: Aaron Johnston and Rachael Judy
 * Notes:   We added the ability for the reservation station to snoop on the CDB. We also added
 * 			the loadInst function so that the Functional Units can load instructions into the reservation stations
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
  IssuedInst.INST_TYPE function = IssuedInst.INST_TYPE.NOP;

  // following just for branches
  int address;
  
  boolean isTaken;
  int pc;
  int immediate;
  
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



  public IssuedInst.INST_TYPE getFunction() {
    return function;
  }

  public void snoop(CDB cdb) {
	  if ((simulator.cdb.getDataTag() == tag1) 
			  && simulator.cdb.getDataValid()) {
		  data1 = simulator.cdb.getDataValue();
		  data1Valid = true;
	  } else if ((simulator.cdb.getDataTag() == tag2) 
			  && simulator.cdb.getDataValid()) {
		  data2 = simulator.cdb.getDataValue();
		  data2Valid = true;
	  } 

  }

  public boolean isReady() {
    return data1Valid && data2Valid;
  }

  public void loadInst(IssuedInst inst) {
	  destTag = inst.regDestTag;
	  
	  tag1 = inst.regSrc1Tag;
	  data1 = inst.regSrc1Value;
	  data1Valid = inst.regSrc1Valid;
	  	  
	  tag2 = inst.regSrc2Tag;
	  data2 = inst.regSrc2Value;
	  data2Valid = inst.regSrc2Valid;
	  function = inst.getOpcode();
	  
	  pc = inst.pc;
	  immediate = inst.immediate;
	  
	  if (inst.branch) {
 		  address = inst.branchTgt;
	  }
	  
  }
}
