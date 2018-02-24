package AST;
import analizadores.*;

public class nodeFatorFunc extends nodeFator{
public Token id;
public nodeExpressao lista;

public void visit (Visitor v){
	 v.visitFatorFunc(this);
	 }

}
