package AST;

public class nodeDecProcedimento extends nodeDeclaracao {
	public nodeIdentificador id;
	public nodeParametro lista;
	//public nodeTipo tipo;
	public nodeCorpo corpo;
	public void visit (Visitor v){
		 v.visitDecProcedimento(this);
		 }


}
