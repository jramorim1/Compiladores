package AST;
import analizadores.*;

public class nodeEn extends nodeExpressao {
public Token name;
public int level;
public int posicao;
//public byte tipo;

	public nodeEn(Token n) {
		this.name = n;
	}
	public void visit (Visitor v){
		 v.visitEn(this);
		 }

}
