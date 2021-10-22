package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode;
  
  int op1;
  int op2;
  int destReg;
  int immediate;


  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;
  }

  public void update() {
	  // if not hazarded ----
	  // getinstrataddr()
	  instPC = simulator.pc.getPC()+4;
	  Instruction instr = simulator.memory.getInstAtAddr(instPC);
	  opcode = instr.getOpcode();
	  
	  if (instr instanceof RTypeInst) {
		  op1 = ((RTypeInst) instr).rs;
		  op2 = ((RTypeInst) instr).rt;
		  destReg = ((RTypeInst) instr).rd;
	  }
	  else if (instr instanceof ITypeInst) {
		  op1 = ((RTypeInst) instr).rs;
		  destReg = ((RTypeInst) instr).rt;
		  immediate = ((ITypeInst) instr).immed;
	  }
	  else if (instr instanceof JTypeInst) {
		  immediate = ((JTypeInst) instr).offset;
	  }
	  
	  
	  
  }
}
