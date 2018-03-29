package OperationsAST;
import AST.*;
import analizadores.Token;

public class Checker implements Visitor {
	
	IdentificationTable idTable = new IdentificationTable();
	
	public void check(nodePrograma p) {
		if(p != null)
			p.visit(this);
		
	}

	public void visitListaDeclaracao(listaDeclaracao ld) {
			//if(ld.exp != null) ld.exp.visit(this);
			if(ld.exp != null) ld.exp.visit(this);
			if(ld.next != null) {ld.next.visit(this);
		}
	}

	@Override
	public void visitListaExpressao(listaExpressao le) {
		if(le != null) {
			if(le.exp != null) le.exp.visit(this);
			if(le.next != null) {le.next.visit(this);}
		}
	}

	@Override
	public void visitAtribComando(nodeAtribComando a) {
		if(a != null) {
			if(a.var.id != null) {
				//nodeDeclaracao d = this.idTable.retrieve(a.var.id.spelling);
			}
			if(a.var.exp != null) {
				a.var.exp.visit(this);
			}

			if(a.expressao != null) {
				a.expressao.visit(this);
			}
			
			byte varTipo = this.idTable.retrieve(a.var.id.spelling);
			byte expTipo = a.expressao.tipo;
			
			if(varTipo != expTipo) {
				System.out.println("Incorrect Types");
				System.exit(0);
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
	public void visitComposto(nodeComposto c) {
		if(c != null) {
			if(c.lista != null) {
				this.idTable.openScope();
				c.lista.visit(this);
				this.idTable.closeScope();
			}
		}
	}

	@Override
	public void visitCorpo(nodeCorpo c) {
		if(c != null) {
			//this.idTable.openScope();
			if(c.declarations != null) {
				c.declarations.visit(this);
			}
			if(c.comandos != null) 
				c.comandos.visit(this);
			//this.idTable.closeScope();
		}
	}

	@Override
	public void visitDecFuncao(nodeDecFuncao df) {
		if(df != null) {
			idTable.enter(df.id.spelling, df);
			if(df.id != null) df.id.visit(this);
			this.idTable.openScope();
			if(df.lista != null) df.lista.visit(this);
			if(df.tipo != null) df.tipo.visit(this);
			if(df.corpo != null) df.corpo.visit(this);
			this.idTable.closeScope();
		}


	}

	@Override
	public void visitDeclaracao(nodeDeclaracao d) {
		if(d != null) {
			if(d instanceof nodeDecVariavel) {
				((nodeDecVariavel)d).visit(this);
			}
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
			idTable.enter(dp.id.spelling, dp);
			
			if(dp.id != null) dp.id.visit(this);
			this.idTable.openScope();
			if(dp.lista != null) dp.lista.visit(this);
			if(dp.corpo != null) dp.corpo.visit(this);
			this.idTable.closeScope();
		}


	}

	@Override
	public void visitDecVariavel(nodeDecVariavel dv) {
		if(dv != null) {
			//idTable.enter(dv.id.spelling, dp);
			//if(dv.lista != null) dv.id.visit(this);
			if(dv.id != null) idTable.enter(dv.id.spelling, dv);
			if(dv.tipo != null) dv.tipo.visit(this);
			if(dv.next != null) dv.next.visit(this);
		}

	}

	@Override
	public void visitEn(nodeEn e) {
		if(e != null) 
			if(e.name != null) {//verificar se nao é literal
				if(e.name.code == Token.INTLITERAL)
					e.tipo = Token.INTEGER;
				else 
					if(e.name.code == Token.FLOATLIT)
						e.tipo = Token.REAL;
				else
					if(e.name.code == Token.BOOLLIT)
						e.tipo = Token.BOOLEAN;
				else
				e.tipo = this.idTable.retrieve(e.name.spelling);
			}
	}

	@Override
	public void visitEo(nodeEo e) {//verificar possibilidade aqui
		if(e != null) {
		if(e.op != null) e.op.visit(this);
		if(e.left != null) e.left.visit(this);
		if(e.right != null) e.right.visit(this);
		
		byte tipo1 = e.left.tipo;
		byte tipo2 = e.right.tipo;
		byte op = e.op.op.code;
		
		e.tipo = compareTypes(tipo1, tipo2, op);
		
			}
		}
	
	private byte compareTypes(byte t1, byte t2, byte op) {
		if(op == Token.OPAD || op == Token.OPMUL) {
			
		if( t1 == Token.REAL &&  t2 == Token.REAL) 
			return Token.REAL;
		
			else if((t1 == Token.REAL && t2 == Token.INTEGER) || (t2 == Token.REAL && t1 == Token.INTEGER))
				return Token.REAL;
		
			else if(t1 == Token.INTEGER && t2 == Token.INTEGER)
				return Token.INTEGER;
		
			else if(t1 == Token.BOOLEAN && t2 == Token.BOOLEAN)
				return Token.BOOLEAN;
			else {
				System.out.println("Incorrect Types!");
				System.exit(0);
			}
		}else {
			if( t1 == Token.REAL &&  t2 == Token.REAL) 
				return Token.BOOLEAN;
			
				else if((t1 == Token.REAL && t2 == Token.INTEGER) || (t2 == Token.REAL && t1 == Token.INTEGER))
					return Token.BOOLEAN;
			
				else if(t1 == Token.INTEGER && t2 == Token.INTEGER)
					return Token.BOOLEAN;
			
				else if(t1 == Token.BOOLEAN && t2 == Token.BOOLEAN)
					return Token.BOOLEAN;
				else {
					System.out.println("Incorrect Types!");
					System.exit(0);
				}
		}
		return 0;
	}

	@Override
	public void visitExpParenteses(nodeExpParenteses e) {
		if(e != null) {
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
			else if(e instanceof nodeEn) {
				((nodeEn)e).visit(this);
				e.tipo = ((nodeEn)e).tipo;
			}
			else if(e instanceof nodeEo) {
				((nodeEo)e).visit(this);
				e.tipo = ((nodeEo)e).tipo;
			}
			else if (e instanceof nodeTermo)
				((nodeTermo)e).visit(this);
			else if(e instanceof nodeFator)
				((nodeFator)e).visit(this);
			else if(e instanceof nodeSeletor) {
				((nodeSeletor)e).visit(this);
			}
		}

	}

	@Override
	public void visitExpressaoSimples(nodeExpressaoSimples e) {
		if(e != null) {
			if(e.op != null) e.op.visit(this);
			if(e.termo != null) e.termo.visit(this);
			if(e.next != null) e.next.visit(this);
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
			if(f.id != null) {
				f.tipo = this.idTable.retrieve(f.id.spelling);
			}
			if(f.lista != null) f.lista.visit(this);
		}

	}

	@Override
	public void visitFatorVar(nodeFatorVar f) {
		if(f != null) {
			if(f.var != null) {
				f.var.visit(this);
				//verificar se nao é literal
					f.tipo = this.idTable.retrieve(f.var.id.spelling);
				
			}
		}
	}

	@Override
	public void visitIdentificador(nodeIdentificador i) {
		//
	}

	@Override
	public void visitIfComando(nodeIfComando c) {
		if(c != null) {
			if(c.comandoIf != null) { 
				c.expressao.visit(this);
				byte tipo = c.expressao.tipo;
				if(tipo != Token.BOOLEAN) {
					System.out.println("Incompatible Types in If Expression ");
					System.exit(0);
				}
				c.comandoIf.visit(this);
				if(c.comandoElse != null) {

					c.comandoElse.visit(this);
				}
			}
		} 
	}

	@Override
	public void visitLiteral(nodeLiteral l) {
		//
	}

	@Override
	public void visitOperator(nodeOperator o) {
		//
	}

	@Override
	public void visitParametro(nodeParametro p) {
		if(p != null) {
		if(p.lista!=null) p.lista.visit(this);
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
			if(p.corpo != null) {
				this.idTable.openScope();
				p.corpo.visit(this);
				this.idTable.closeScope();
			}
		}
	}

	@Override
	public void visitTermo(nodeTermo t) {
		if(t != null) {


			t.op.visit(this);
			if(t.fator != null) t.fator.visit(this);
			if(t.next != null) t.next.visit(this);

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
			//System.out.println(t.intLeft.spelling);
			//System.out.println(t.intRight.spelling);
			t.tipo.visit(this);
		}
	}

	@Override
	public void visitTipoSimples(nodeTipoSimples t) {
		if(t != null) {
			//System.out.println(Token.SPELLINGS[t.tipo]);
		}

	}

	@Override
	public void visitVariavel(nodeVariavel v) {
		if(v != null) {
			//System.out.println(v.id.spelling);
			if(v.exp != null) v.exp.visit(this);
		}

	}

	@Override
	public void visitWhileComando(nodeWhileComando w) {
		if(w != null) {
		if(w.expressao != null) {
			w.expressao.visit(this);
			byte tipo = w.expressao.tipo;
			if(tipo != Token.BOOLEAN) {
				System.out.println("Incompatible Types in While Expression");
				System.exit(0);
			}
		}
		if(w.comando != null) {
			w.comando.visit(this);
			}
		}
	}

	@Override
	public void visitSequencialComando(sequencialComando sc) {
		if(sc != null) {
			if(sc.C1 != null) {sc.C1.visit(this);}
			if(sc.C2 != null) {
				sc.C2.visit(this);
			}
		}

	}

	@Override
	public void visitSequencialDeclaration(sequencialDeclaration sd) {
		if(sd != null) {
			if(sd.D1 != null) sd.D1.visit(this);
			if(sd.D2 != null) {
				sd.D2.visit(this);
			}
		}

	}

	@Override
	public void visitSequencialExpressao(sequencialExpressao se) {
		if(se != null) {
			if(se.exp != null) se.exp.visit(this);
			if(se.next != null) {
				se.next.visit(this);
			}
		}
	}

	@Override
	public void visitSequencialIdentificador(sequencialIdentificador si) {
		if(si != null) {

			if(si.I1 != null) si.I1.visit(this);			
			if(si.I2 != null) {
				si.I2.visit(this);
			}
		}
	}
	@Override
	public void visitSequencialParametro(sequencialParametro sp) {
		if(sp != null) {
			if(sp.P1 != null) sp.P1.visit(this);
			if(sp.P2 != null) {
				sp.P2.visit(this);
			}
		}
	}

	@Override
	public void visitSeletor(nodeSeletor s) {
		if(s != null) {
			if(s.lista != null) s.lista.visit(this);
		}
	}
}

