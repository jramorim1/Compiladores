package OperationsAST;
import AST.nodeDeclaracao;
import AST.nodeDecVariavel;

public class Stack {
	public Object[] stack;
	public int SB,ST,LB,L1,L2,L3,LD,LE,ER;
	
	public Stack() {
		this.stack = new Object[2048];
		this.ST = -1;
		this.SB = 0;
		this.LB = this.SB;
		this.L1 = this.L2 = this.L3 = this.LB = 0;
	}
	
	public boolean isEmpty() {
		if(this.ST == -1)
			return true;
		return false;
	}
	
	public int size() {
		if(isEmpty())
			return 0;	
		return this.ST + 1;
	}

	public void push(Object ob, int size) {
		if(this.ST + size < this.stack.length - 1) {
			this.stack[++ST] = ((nodeDecVariavel)ob).id.spelling;
			while(size > 1) {
				this.stack[++ST] = "";
				size--;
		}
	}
}
	
	public Object pop(int n) {
		 if (isEmpty()) {
	            return null;
	        }
		 	this.ST -= n;
	        return this.stack[this.ST];
	}
	public void pushFrame(){
		if(this.ST + 3 < this.stack.length - 1) { //3 considerando 1 para cada link 
			if(isEmpty())
				this.ST = 0;
			
			this.LD = this.LB; // link dinamico recebe o lb do ultimo ativado
			this.LB = this.ST;
			this.stack[this.ST] = "LE";
			this.stack[this.ST + 1] = "LD";
			this.stack[this.ST + 2] = "ER";// lb aponta pro antigo ST
			this.ST += 2;//espaço dos links
			}
	}
}
