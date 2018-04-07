package AST;

public abstract class nodeDeclaracao {
	public abstract void visit (Visitor v);
	public int level;
	public int posicao;
	public int size;

}