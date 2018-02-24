package AST;
import analizadores.*;

public class nodeLiteral extends Token{
	public void visit (Visitor v){
		 v.visitLiteral(this);
		 }

}
