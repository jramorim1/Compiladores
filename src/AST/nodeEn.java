package AST;
import analizadores.*;

public class nodeEn extends nodeExpressao {
public Token name;
	public nodeEn(Token n) {
		this.name = n;
	}
	public void visit (Visitor v){
		 v.visitEn(this);
		 }

}
