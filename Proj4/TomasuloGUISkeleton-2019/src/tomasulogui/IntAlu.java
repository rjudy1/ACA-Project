/*
 * File: 	IntAlu.java
 * Authors: Aaron Johnston and Rachael Judy
 * Project: ACA Project 4 - Tomasulo
 * Notes:   who gets to say calculateResult?
 * 
 * 
 */

package tomasulogui;

public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;
  
  public IntAlu(PipelineSimulator sim) {
    super(sim);
  }


  public int calculateResult(int station) {
     // just placeholder code
	int operand1 = FunctionalUnit.stations[station].getData1();
	int operand2 = FunctionalUnit.stations[station].getData2();
	int result = 0;
	// ALU -- Where do we get OPCODE from..?
	switch(opcode) {
	case Instruction.INST_ADD:
	case Instruction.INST_ADDI:
	case Instruction.INST_SW:
	case Instruction.INST_LW:
		result = operand1 + operand2;
		break;
	case Instruction.INST_SUB:
	case Instruction.INST_ANDI:
		result = operand1 - operand2; 
		break;
//	case Instruction.INST_MUL:
//		aluIntData = operand1 * operand2;
//		break;
//	case Instruction.INST_DIV:
//		aluIntData = operand1 / operand2;
//		break;
	case Instruction.INST_AND:
		result = operand1 & operand2;
		break;
	case Instruction.INST_OR:
	case Instruction.INST_ORI:
		result = operand1 | operand2;
		break;
	case Instruction.INST_XOR:
	case Instruction.INST_XORI:
		result = operand1 ^ operand2;    		
		break;
	case Instruction.INST_SLL:
		result = operand1 << operand2;
		break;
	case Instruction.INST_SRL:
		result = operand1 >>> operand2;
		break;
	case Instruction.INST_SRA:
		result = operand1 >> operand2;
		break;    		
	// Are these needed?
	case Instruction.INST_BEQ:
	case Instruction.INST_BNE:
	case Instruction.INST_BLEZ:
	case Instruction.INST_BLTZ:
	case Instruction.INST_BGEZ:
	case Instruction.INST_BGTZ:
	case Instruction.INST_J:
	case Instruction.INST_JAL:
		result = operand1 + operand2; // pc + imm
		break;
	case Instruction.INST_JALR:
	case Instruction.INST_JR:
		result = operand1; // register value
		break;
	}    
	
	return result;
  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }
}
