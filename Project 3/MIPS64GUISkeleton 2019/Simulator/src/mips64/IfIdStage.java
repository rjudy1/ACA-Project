package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode = 62;
  
  int op1=0;
  int op2=0;
  int destReg=32;
  int immediate;
  int shamt;
  boolean shouldWriteback = false;

  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;
  }

  public void update() {
	  // keep everything in place if interlocked
	  if (!simulator.interlock) {
		  instPC = simulator.pc.getPC();
	  }		  

	  Instruction instr = simulator.memory.getInstAtAddr(instPC);
	  opcode = instr.getOpcode();
	  // based on instruction type, pass through the correct register values
	  if (instr instanceof RTypeInst) {
		  op1 = ((RTypeInst) instr).rs;
		  op2 = ((RTypeInst) instr).rt;
		  destReg = ((RTypeInst) instr).rd;
		  immediate =((RTypeInst) instr).shamt;;
		  shamt = ((RTypeInst) instr).shamt;
	  }
	  else if (instr instanceof ITypeInst) {
		  op1 = ((ITypeInst) instr).rs;
		  op2 = ((ITypeInst) instr).rt;
		  immediate = ((ITypeInst) instr).immed;
		  destReg = ((ITypeInst) instr).rt;
	  }
	  else if (instr instanceof JTypeInst) {
		  immediate = ((JTypeInst) instr).offset;
		  destReg = 31;
	  }
	  
	  // writes back if not one of the instructions that gets written back + two ahead not a taken branch handled up there
	  shouldWriteback = !simulator.exMem.branchTaken;
  }
}
