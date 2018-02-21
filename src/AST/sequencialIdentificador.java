package AST;

public class sequencialIdentificador extends nodeIdentificador{
nodeIdentificador I1,I2;
public sequencialIdentificador(nodeIdentificador i1, nodeIdentificador i2) {
	this.I1 = i1;
	this.I2 = i2;
}
}
