package AST;
import analizadores.*;
import AST.nodeDeclaracao;

public class nodeFatorFunc extends nodeFator{
public Token id;
public nodeExpressao lista;
public nodeDeclaracao decl;

public void visit (Visitor v){
	 v.visitFatorFunc(this);
	 }

}
