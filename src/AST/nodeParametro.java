package AST;
import analizadores.*;

public class nodeParametro {
public nodeDecVariavel lista;

public void visit (Visitor v){
	 v.visitParametro(this);
	 }

}
