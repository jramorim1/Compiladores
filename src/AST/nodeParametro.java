package AST;
import analizadores.*;

public class nodeParametro {
public nodeIdentificador lista;
public nodeTipo tipo;

public void visit (Visitor v){
	 v.visitParametro(this);
	 }

}
