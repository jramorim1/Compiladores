package AST;

public class nodeEo extends nodeExpressao{
	public nodeExpressao left, right;
	nodeOperator op;
	
	public nodeEo(nodeExpressao l, nodeOperator op, nodeExpressao r) {
		this.left = l;
		this.right = r;
		this.op = op;
	}

}
