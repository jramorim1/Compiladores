package OperationsAST;

public class Instruction {
	
 static String getInstruction(byte m, byte r, int n, int d) {
		String instrucao = "";
		switch(m) {
		case Instruction.LOAD:
			instrucao = "LOAD ";
			break;
		case Instruction.LOADL:
			instrucao = "LOADL ";
			break;
		case Instruction.LOADA:
			instrucao = "LOADA ";
			break;
		case Instruction.STORE:
			instrucao = "STORE ";
			break;
		case Instruction.CALL:
			instrucao = "CALL(" + n + ") ";
			break;
		case Instruction.RETURN:
			instrucao = "RETURN(" + n + ") ";
			break;
		case Instruction.PUSH:
			instrucao = "PUSH " + n;
			break;
		case Instruction.POP:
			instrucao = "POP " + n;
			break;
		case Instruction.JUMP:
			instrucao = "JUMP ";
			break;
		case Instruction.JUMPIF:
			instrucao = "JUMPIF(" + n + ") ";
			break;
		case Instruction.HALT:
			instrucao = "HALT";
			break;
		default:
			System.out.println("Instrucao nao existe");
			break;
		}
		
		switch(r) {
		case Instruction.SB:
			instrucao = instrucao + d + "[SB]";
			break;
		case Instruction.LB:
			instrucao = instrucao + d + "[LB]";
			break;
		case Instruction.ST:
			break;
		case Instruction.L1:
				instrucao = instrucao + d + "[L1]";
			break;
		case Instruction.L2:
			instrucao = instrucao + d + "[L2]";
			break;
		case Instruction.L3:
			instrucao = instrucao + d + "[L3]";
			break;
		default:
			System.out.println("Instrucao nao existe");
			break;
		}
		
		return instrucao;
	}
	
	public final static byte
	LOAD = 0,
	LOADL = 1,
	LOADA = 2,
	STORE = 3,
	CALL = 4,
	RETURN =5,
	PUSH = 6,
	POP = 7,
	JUMP = 8,
	JUMPIF = 9,
	HALT = 10;
	
	public final static byte 
	SB = 0,
	ST = 1,
	LB = 2,
	L1 = 3,
	L2 = 4,
	L3 = 5;
}
