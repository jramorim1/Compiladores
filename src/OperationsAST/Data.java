package OperationsAST;
import AST.nodeDecVariavel;
import AST.nodeDeclaracao;
import AST.nodeTipoAgregado;
import AST.nodeTipoSimples;
import AST.*;

public class Data {
	public String id;
	public nodeDeclaracao decl;
	public int local;
	public byte type;
	
	public Data(int local, String i, nodeDeclaracao decl) {
		this.id = i;
		this.local = local;
		this.decl = decl;
		type = returnTipo(decl);
		
	}
	
	private  byte returnTipo(nodeDeclaracao decl) {
		if(decl instanceof nodeDecVariavel) {
			nodeTipo t0 = ((nodeDecVariavel)decl).tipo;
			
			if(t0 instanceof nodeTipoSimples) {
				return ((nodeTipoSimples)t0).tipo;
			}
			else if(t0 instanceof nodeTipoAgregado) {
				nodeTipo t1 = ((nodeTipoAgregado)t0).tipo;
				if(t1 instanceof nodeTipoSimples) {
					return ((nodeTipoSimples)t1).tipo;
				}
				else if(t1 instanceof nodeTipoAgregado) {
					nodeTipo t2 = ((nodeTipoAgregado)t1).tipo;
					if(t2 instanceof nodeTipoSimples) {
						return ((nodeTipoSimples)t2).tipo;
					}
					else {
						System.out.println("Exceded Array Dimension!");
						System.exit(0);
					}
				}
			}
		}else if(decl instanceof nodeDecFuncao) {
			nodeTipo t0 = ((nodeDecFuncao)decl).tipo;
			
			if(t0 instanceof nodeTipoSimples) {
				return ((nodeTipoSimples)t0).tipo;
			}
			else if(t0 instanceof nodeTipoAgregado) {
				nodeTipo t1 = ((nodeTipoAgregado)t0).tipo;
				if(t1 instanceof nodeTipoSimples) {
					return ((nodeTipoSimples)t1).tipo;
				}
				else if(t1 instanceof nodeTipoAgregado) {
					nodeTipo t2 = ((nodeTipoAgregado)t1).tipo;
					if(t2 instanceof nodeTipoSimples) {
						return ((nodeTipoSimples)t2).tipo;
					}
					else {
						System.out.println("Exceded Array Dimension!");
						System.exit(0);
					}
				}
			}
		}
		return 0;
	}
	

}
