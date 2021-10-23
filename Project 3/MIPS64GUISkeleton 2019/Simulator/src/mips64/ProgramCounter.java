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
	  // need to declare some more values and register for output to be set
	  // if hazard -----------
	  // look at book for image
	  if (!simulator.idEx.shouldWriteback ) { // pipeline was nullified by branch or jump
//		  && (simulator.exMem.opcode == Instruction.INST_BEQ ||
//				  simulator.exMem.opcode == Instruction.INST_BGEZ || simulator.exMem.opcode == Instruction.INST_BGTZ ||
//				  simulator.exMem.opcode == Instruction.INST_BLEZ || simulator.exMem.opcode == Instruction.INST_BLTZ ||
//				  simulator.exMem.opcode == Instruction.INST_BNE)
		  setPC(simulator.exMem.aluIntData);
	  } else {
		  incrPC();
	  }
  }
}
