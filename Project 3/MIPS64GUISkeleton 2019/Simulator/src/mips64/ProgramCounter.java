/*
 *  Authors: Rachel Judy and Aaron Johnston
 *  Date: 11/2/21
 *  Pipeline Program Counter  
 *  
 *  Description: This is the program counter for the pipeline
 *  			 When not interlocked, the program counter increments. If a branch is taken, we set the PC to the right value.
 */
package mips64;

public class ProgramCounter {

  PipelineSimulator simulator;
  int pc;

  public ProgramCounter(PipelineSimulator sim) {
    pc = 0;
    simulator = sim;
  }

  public int getPC () {
    return pc;
  }

  public void setPC (int newPC) {
    pc = newPC;
  }

  public void incrPC () {
    pc += 4;
  }

  public void update() {
	  // if not a branch, increment, if it is a branch, fetch from ID stage
	  if (!simulator.interlock && simulator.exMem.branchTaken) { 
		  // pipeline was nullified by branch or jump
		  setPC(simulator.exMem.aluIntData);
	  } else if (!simulator.interlock && pc < simulator.memory.maxCodeAddress*4) {
		  incrPC();
	  }
  }
}
