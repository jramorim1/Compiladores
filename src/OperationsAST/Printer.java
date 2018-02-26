package OperationsAST;
import AST.*;
import analizadores.Token;


/* FALTA IMPLEMENTAR A CHAMADA DE FUNÇÃO
 * NO VISITOR E NA AST
 * 
 * 
 * 
 * 
 * */
public class Printer implements Visitor{
	
	private int coluna = 0;
	
	public void print (nodePrograma p) {
		 System.out.println ("*********[ Starting the AST Printing Process ]**********");
		 p.visit (this);
		 System.out.println("[End of AST]");
		 }
	
	private void indent() {
	      for (int j=0; j<coluna; j++) 
	         System.out.print ("|");
	   }


	@Override
	public void visitListaDeclaracao(listaDeclaracao ld) {
		if(ld != null) {
			ld.exp.visit(this);
			if(ld.next != null) {
				coluna++;
				indent();
				ld.next.visit(this);
				coluna--;
			}
		}
	}

	@Override
	public void visitListaExpressao(listaExpressao le) {
		if(le != null) {
			le.exp.visit(this);
			if(le.next != null) {
				coluna++;
				indent();
				le.next.visit(this);
				coluna--;
			}
		}
	}

	@Override
	public void visitAtribComando(nodeAtribComando a) {
		if(a != null) {
			//indent();
			System.out.println(a.var.id.spelling);
			if(a.var.exp != null) {
				a.var.exp.visit(this);
			}
			
			if(a.expressao != null) {
				a.expressao.visit(this);
			}
		}
		
	}

	@Override
	public void visitComando(nodeComando c) {
		if(c != null) {
			
			if(c instanceof nodeIfComando)
				((nodeIfComando)c).visit(this);
			else if(c instanceof nodeWhileComando)
				((nodeWhileComando)c).visit(this);
			else if(c instanceof nodeAtribComando)
				((nodeAtribComando)c).visit(this);
			else if(c instanceof nodeComposto)
				((nodeComposto)c).visit(this);
			else if(c instanceof nodePComando)
				((nodePComando)c).visit(this);
			else
				((sequencialComando)c).visit(this);
		}
			
		
	}

	@Override
	public void visitComposto(nodeComposto c) { // metodo inutilizavel aparentemente
		if(c != null) {
			if(c.lista instanceof sequencialComando) {
				coluna++;
				indent();
				((sequencialComando)c.lista).C1.visit(this);
				((sequencialComando)c.lista).C2.visit(this);
				coluna--;
			}
		}
	}

	@Override
	public void visitCorpo(nodeCorpo c) {
		if(c != null) {
			if(c.declarations != null) 
					c.declarations.visit(this);
			
			if(c.comandos != null) 
				c.comandos.visit(this);
			
		}
	}

	@Override
	public void visitDecFuncao(nodeDecFuncao df) {
		if(df != null) {
			if(df.id != null) df.id.visit(this);
			if(df.lista != null) df.lista.visit(this);
			if(df.tipo != null) df.tipo.visit(this);
			if(df.corpo != null) df.corpo.visit(this);
		}
		 
		
	}

	@Override
	public void visitDeclaracao(nodeDeclaracao d) {
		if(d != null) {
			if(d instanceof nodeDecVariavel)
				((nodeDecVariavel)d).visit(this);
			else if( d instanceof nodeDecProcedimento)
				((nodeDecProcedimento)d).visit(this);
			else if(d instanceof nodeDecFuncao)
				((nodeDecFuncao)d).visit(this);
			else if(d instanceof sequencialDeclaration)
				((sequencialDeclaration)d).visit(this);
		}	
	}

	@Override
	public void visitDecProcedimento(nodeDecProcedimento dp) {
		if(dp != null) {
			if(dp.id != null) dp.id.visit(this);
			if(dp.lista != null) dp.lista.visit(this);
			if(dp.corpo != null) dp.corpo.visit(this);
		}
		 
		
	}

	@Override
	public void visitDecVariavel(nodeDecVariavel dv) {
		 if(dv != null) {
			 if(dv.lista != null) dv.lista.visit(this);
			 if(dv.tipo != null) dv.tipo.visit(this);
		 }
		
	}

	@Override
	public void visitEn(nodeEn e) {
		 if(e != null) {
			 coluna++;
			 indent();
			 System.out.println(e.name.spelling);
			 coluna--;
		 }
		
	}

	@Override
	public void visitEo(nodeEo e) {
		 coluna++;
		 indent();
		 if(e.op != null) e.op.visit(this);
		 if(e.left != null) e.left.visit(this);
		 if(e.right != null) e.right.visit(this);
		 coluna--;
		
	}

	@Override
	public void visitExpParenteses(nodeExpParenteses e) {
		 if(e != null) {
			 //talvez implementar classe com o token parentenses
			 e.expressao.visit(this);
		 }
		
	}

	@Override
	public void visitExpressao(nodeExpressao e) {
		 if(e != null) {
			 if (e instanceof nodeExpressaoSimples)
				 ((nodeExpressaoSimples)e).visit(this);
			 else if(e instanceof nodeExpParenteses)
				 ((nodeExpParenteses)e).visit(this);
			 else if( e instanceof sequencialExpressao)
				 ((sequencialExpressao)e).visit(this);
			 else if(e instanceof nodeEn)
				 ((nodeEn)e).visit(this);
			 else if(e instanceof nodeEo)
				 ((nodeEo)e).visit(this);
			 else if (e instanceof nodeTermo)
				 ((nodeTermo)e).visit(this);
			 else if(e instanceof nodeFator)
				 ((nodeFator)e).visit(this);
		 }
		
	}

	@Override
	public void visitExpressaoSimples(nodeExpressaoSimples e) {
		if(e != null) {
			//coluna++;
			//indent();
			if(e.op != null) e.op.visit(this);
			if(e.termo != null) e.termo.visit(this);
			if(e.next != null) e.next.visit(this);
			//coluna--;
		}
		 
		
	}

	@Override
	public void visitFator(nodeFator f) {
		 if(f != null) {
			 if(f instanceof nodeFatorFunc)
				 ((nodeFatorFunc)f).visit(this);
			 else if(f instanceof nodeFatorVar)
				 ((nodeFatorVar)f).visit(this);
		 }
	}

	@Override
	public void visitFatorFunc(nodeFatorFunc f) {
		 if(f != null) {
			 System.out.println(f.id.spelling);
			 if(f.lista != null) f.lista.visit(this);
		 }
		
	}

	@Override
	public void visitFatorVar(nodeFatorVar f) {
		if(f != null) {
			f.var.visit(this);
		}
	}

	@Override
	public void visitIdentificador(nodeIdentificador i) {
		if(i instanceof sequencialIdentificador)
		 ((sequencialIdentificador)i).visit(this);
		else {
			//coluna++;
			//indent();
			System.out.println(i.spelling);
			//coluna--;
		}
	}

	@Override
	public void visitIfComando(nodeIfComando c) {
		if(c != null) {

			System.out.println("if");
			if(c.comandoIf != null) {
				coluna++;
				//indent();
				c.expressao.visit(this);
				indent();
				System.out.println("then");
				indent();
				c.comandoIf.visit(this);
				if(c.comandoElse != null) {
					indent();
					System.out.println("else");
					c.comandoElse.visit(this);
				}
				coluna--;
			}
		} 

	}

	@Override
	public void visitLiteral(nodeLiteral l) {
		//coluna++;
		//indent();
		System.out.println(l.spelling);
		//coluna--;
		
	}

	@Override
	public void visitOperator(nodeOperator o) {
		System.out.println(o.op.spelling);
	}

	@Override
	public void visitParametro(nodeParametro p) {
		 if(p != null) {
			 coluna++;
			 indent();
			 p.lista.visit(this);
			 p.tipo.visit(this);
			 coluna--;
		 }
		
	}

	@Override
	public void visitPComando(nodePComando p) {
		if(p != null) {
			if(p.id != null) p.id.visit(this);
			if(p.lista != null) p.lista.visit(this);
		}
	}

	@Override
	public void visitPrograma(nodePrograma p) {
		if(p != null) {
			System.out.println(p.id.spelling);
			if(p.corpo != null)
				p.corpo.visit(this);
		}
	}

	@Override
	public void visitTermo(nodeTermo t) {
		 if(t != null) {
			 coluna++;
			 indent();
			 t.op.visit(this);
			 if(t.fator != null) t.fator.visit(this);
			 if(t.next != null) t.next.visit(this);
			 coluna--;
		 }
	}

	@Override
	public void visitTipo(nodeTipo t) {
		 if(t != null) {
			 if(t instanceof nodeTipoSimples)
				 ((nodeTipoSimples)t).visit(this);
			 else 
				 ((nodeTipoAgregado)t).visit(this);
		 }
		
	}

	@Override
	public void visitTipoAgregado(nodeTipoAgregado t) {
		 if(t != null) {
			 coluna++;
			 indent();
			 System.out.println("array");
			 System.out.println(t.intLeft.spelling);
			 System.out.println(t.intRight.spelling);
			 t.tipo.visit(this);
		 }
	}

	@Override
	public void visitTipoSimples(nodeTipoSimples t) {
		 if(t != null) {
			 coluna++;
			 indent();
			 System.out.println(Token.SPELLINGS[t.tipo]);
			 coluna--;
		 }
		
	}

	@Override
	public void visitVariavel(nodeVariavel v) {
		 if(v != null) {
			 coluna++;
			 indent();
			 System.out.println(v.id.spelling);
			 coluna--;
			 if(v.exp != null) v.exp.visit(this);
		 }
		
	}

	@Override
	public void visitWhileComando(nodeWhileComando w) {

		 System.out.println("while");
		 w.expressao.visit(this);
		 indent();
		 System.out.println("do");
		 indent();
		 w.comando.visit(this);
	}

	@Override
	public void visitSequencialComando(sequencialComando sc) {
		 if(sc != null) {
			 if(sc.C1 != null) sc.C1.visit(this);
			 if(sc.C2 != null) {
				 coluna++;
				 indent();
				 sc.C2.visit(this);
				 coluna--;
			 }
		 }
		
	}

	@Override
	public void visitSequencialDeclaration(sequencialDeclaration sd) {
		if(sd != null) {
			 if(sd.D1 != null) sd.D1.visit(this);
			 if(sd.D2 != null) {
				 coluna++;
				 indent();
				 sd.D2.visit(this);
				 coluna--;
			 }
		 }
		
	}

	@Override
	public void visitSequencialExpressao(sequencialExpressao se) {
		if(se != null) {
			 se.exp.visit(this);
			 if(se.next != null) {
				 coluna++;
				 indent();
				 se.next.visit(this);
				 coluna--;
			 }
		 }
	}

	@Override
	public void visitSequencialIdentificador(sequencialIdentificador si) {
		if(si != null) {
			
			if(si.I1 != null) si.I1.visit(this);
			coluna++;
			indent();
			 if(si.I2 != null) si.I2.visit(this);
			 coluna--;
		 }
		
	}

	@Override
	public void visitSequencialParametro(sequencialParametro sp) {
		if(sp != null) {
			 sp.P1.visit(this);
			 if(sp.P2 != null) {
				 coluna++;
				 indent();
				 sp.P2.visit(this);
				 coluna--;
			 }
		 }
	}
}
