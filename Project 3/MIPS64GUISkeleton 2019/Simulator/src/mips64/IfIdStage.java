package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode;


  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;
  }

  public void update() {
	  // if not hazarded ----
	  // getinstrataddr()
	  instPC = simulator.pc.getPC()+4;
	  Instruction instr = simulator.memory.getInstAtAddr(instPC);
	  opcode = instr.getOpcode();
  }
}
