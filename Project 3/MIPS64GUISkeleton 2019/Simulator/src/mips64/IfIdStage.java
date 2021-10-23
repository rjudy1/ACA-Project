package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode = 62;
  
  int op1=0;
  int op2=0;
  int destReg;
  int immediate;
  int shamt;

  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;
  }

  public void update() {
	  // if not hazarded ----
	  // getinstrataddr()
	  instPC = simulator.pc.getPC();
	  Instruction instr = simulator.memory.getInstAtAddr(instPC);
	  opcode = instr.getOpcode();
	  
	  if (instr instanceof RTypeInst) {
		  op1 = ((RTypeInst) instr).rs;
		  op2 = ((RTypeInst) instr).rt;
		  destReg = ((RTypeInst) instr).rd;
		  shamt = ((RTypeInst) instr).shamt;
	  }
	  else if (instr instanceof ITypeInst) {
		  op1 = ((ITypeInst) instr).rs;
		  immediate = ((ITypeInst) instr).immed;
		  destReg = ((ITypeInst) instr).rt;
	  }
	  else if (instr instanceof JTypeInst) {
		  immediate = ((JTypeInst) instr).offset;
		  destReg = 31;
	  }
  }
}
