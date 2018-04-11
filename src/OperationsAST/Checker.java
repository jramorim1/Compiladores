package OperationsAST;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import AST.*;
import analizadores.Token;
import analizadores.View;

public class Checker implements Visitor {
	
	IdentificationTable idTable = new IdentificationTable();
	View view;
	int flag;
	
	public Checker(View v){
		view = v;
		flag = 0;
	}
	
	public void check(nodePrograma p) {
		if((p != null && view.Ef ==0))
			p.visit(this);
				
	}

	public void visitListaDeclaracao(listaDeclaracao ld) {
			//if(ld.exp != null) ld.exp.visit(this);
			if((ld.exp != null && view.Ef ==0)) ld.exp.visit(this);
			if((ld.next != null && view.Ef ==0)) {ld.next.visit(this);
		}
	}

	@Override
	public void visitListaExpressao(listaExpressao le) {
		if(le != null) {
			if((le.exp != null && view.Ef ==0)) le.exp.visit(this);
			if((le.next != null && view.Ef ==0)) {le.next.visit(this);}
		}
	}
	
	private byte compareAtrib(byte t1, byte t2) {
		
			
		if( t1 == Token.REAL &&  t2 == Token.REAL) 
			return Token.REAL;
		
			else if((t1 == Token.REAL && t2 == Token.INTEGER))
				return Token.REAL;
		
			else if(t1 == Token.INTEGER && t2 == Token.INTEGER)
				return Token.INTEGER;
		
			else if(t1 == Token.BOOLEAN && t2 == Token.BOOLEAN)
				return Token.BOOLEAN;
			else {
				if(view.Ef == 0) {
				view.tC.append("Context Error: Incorrect Types!");
					view.Ef = 1;
				}
			}
		return 0;
	}

	@Override
	public void visitAtribComando(nodeAtribComando a) {
		if((a != null && view.Ef ==0)) {
			if((a.var.id != null && view.Ef ==0)) {
				//nodeDeclaracao d = this.idTable.retrieve(a.var.id.spelling);
			}
			if((a.var.exp != null && view.Ef ==0)) {
				a.var.exp.visit(this);
			}

			if((a.expressao != null && view.Ef ==0)) {
				a.expressao.visit(this);
			}
			
			byte varTipo = this.idTable.retrieve(a.var.id.spelling);
			byte expTipo = a.expressao.tipo;
			
			compareAtrib(varTipo, expTipo);
		}

	}

	@Override
	public void visitComando(nodeComando c) {
		if((c != null && view.Ef ==0)) {

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
		if((c != null && view.Ef ==0)) {
			if((c.lista != null && view.Ef ==0)) {
				this.idTable.openScope();
				c.lista.visit(this);
				this.idTable.closeScope();
			}
		}
	}

	@Override
	public void visitCorpo(nodeCorpo c) {
		if((c != null && view.Ef ==0)) {
			//this.idTable.openScope();
			if((c.declarations != null && view.Ef ==0)) {
				c.declarations.visit(this);
			}
			if((c.comandos != null && view.Ef ==0)) 
				c.comandos.visit(this);
			//this.idTable.closeScope();
		}
	}

	@Override
	public void visitDecFuncao(nodeDecFuncao df) {
		if((df != null && view.Ef ==0)) {
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
		if((d != null && view.Ef ==0)) {
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
		if((dp != null & view.Ef ==0)) {
			idTable.enter(dp.id.spelling, dp);
			
			if((dp.id != null && view.Ef ==0)) dp.id.visit(this);
			this.idTable.openScope();
			if((dp.lista != null && view.Ef ==0)) dp.lista.visit(this);
			if((dp.corpo != null && view.Ef ==0)) dp.corpo.visit(this);
			this.idTable.closeScope();
		}


	}

	@Override
	public void visitDecVariavel(nodeDecVariavel dv) {
		if((dv != null && view.Ef ==0)) {
			//idTable.enter(dv.id.spelling, dp);
			//if(dv.lista != null) dv.id.visit(this);
			if((dv.id != null && view.Ef ==0)) idTable.enter(dv.id.spelling, dv);
			if((dv.tipo != null && view.Ef ==0)) dv.tipo.visit(this);
			if((dv.next != null && view.Ef ==0)) dv.next.visit(this);
		}

	}

	@Override
	public void visitEn(nodeEn e) {
		if((e != null && view.Ef ==0)) 
			if((e.name != null && view.Ef ==0)) {//verificar se nao é literal
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
		if((e != null && view.Ef ==0)) {
		if((e.op != null && view.Ef ==0)) e.op.visit(this);
		if((e.left != null && view.Ef ==0)) e.left.visit(this);
		if((e.right != null && view.Ef ==0)) e.right.visit(this);
		
		byte tipo1 = e.left.tipo;
		byte tipo2 = e.right.tipo;
		byte op = e.op.op.code;
		String opAux = e.op.op.spelling;
		
		e.tipo = compareTypes(tipo1, tipo2, op, opAux);
		
			}
		}
	
	private byte compareTypes(byte t1, byte t2, byte op, String opAux) {
		if(op == Token.OPAD || op == Token.OPMUL) {
			
		if( t1 == Token.REAL &&  t2 == Token.REAL) 
			return Token.REAL;
		
			else if((t1 == Token.REAL && t2 == Token.INTEGER) || (t2 == Token.REAL && t1 == Token.INTEGER))
				return Token.REAL;
		
			else if(t1 == Token.INTEGER && t2 == Token.INTEGER)
				return Token.INTEGER;
		
			/*else if(t1 == Token.BOOLEAN && t2 == Token.BOOLEAN)
				return Token.BOOLEAN;*/
			else {
				if(view.Ef == 0) {
					view.tC.append("Context Error: Incorrect Types!");
						view.Ef = 1;
					}
			}
		}else {
			if( t1 == Token.REAL &&  t2 == Token.REAL) 
				return Token.BOOLEAN;
			
				else if((t1 == Token.REAL && t2 == Token.INTEGER) || (t2 == Token.REAL && t1 == Token.INTEGER))
					return Token.BOOLEAN;
			
				else if(t1 == Token.INTEGER && t2 == Token.INTEGER)
					return Token.BOOLEAN;
			
				else if(t1 == Token.BOOLEAN && t2 == Token.BOOLEAN && (opAux.compareTo("=") == 0 || opAux.compareTo("<>") == 0))
					return Token.BOOLEAN;
				else {
					if(view.Ef == 0) {
						view.tC.append("Context Error: Incorrect Types!");
							view.Ef = 1;
						}
				}
		}
		return 0;
	}

	@Override
	public void visitExpParenteses(nodeExpParenteses e) {
		if((e != null && view.Ef ==0)) {
			e.expressao.visit(this);
		}

	}

	@Override
	public void visitExpressao(nodeExpressao e) {
		if((e != null && view.Ef ==0)) {
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
		if((e != null && view.Ef ==0)) {
			if((e.op != null && view.Ef ==0)) e.op.visit(this);
			if((e.termo != null && view.Ef ==0)) e.termo.visit(this);
			if((e.next != null && view.Ef ==0)) e.next.visit(this);
		}
	}

	@Override
	public void visitFator(nodeFator f) {
		if((f != null && view.Ef ==0)) {
			if(f instanceof nodeFatorFunc)
				((nodeFatorFunc)f).visit(this);
			else if(f instanceof nodeFatorVar)
				((nodeFatorVar)f).visit(this);
		}
	}

	@Override
	public void visitFatorFunc(nodeFatorFunc f) { //VERIFICAR QUANTIDADE DE PARAMETROS
		if((f != null && view.Ef ==0)) {
			nodeDecFuncao decl;
			if((f.id != null && view.Ef ==0)) {
				f.tipo = this.idTable.retrieve(f.id.spelling);
			}
			if((f.lista != null && view.Ef ==0)) {
				f.lista.visit(this);
			}
			
				decl = (nodeDecFuncao)this.idTable.retrieveDeclaration(f.id.spelling);
				
				nodeDecVariavel declArgs = null;
				
				if((decl.lista != null && view.Ef ==0)) {
					declArgs = decl.lista.lista;
				}
				sequencialExpressao chamadaArgs = (sequencialExpressao)f.lista;
				
				if(chamadaArgs == null && declArgs != null && view.Ef ==0) {
					if(view.Ef == 0) {
						view.tC.append("Missing Arguments in Declaration on row " + decl.id.linha + " .");
							view.Ef = 1;
						}
				
				}else if(chamadaArgs != null && declArgs == null && view.Ef ==0) {
					if(view.Ef == 0) {
						view.tC.append("Exeded Arguments in Declaration on row " + decl.id.linha + " .");
							view.Ef = 1;
						}
			
				}
				
				while(declArgs != null || chamadaArgs != null) {
					
					if(chamadaArgs == null && declArgs != null && view.Ef ==0) {
						if(view.Ef == 0) {
							view.tC.append("Missing Arguments in Declaration on row " + decl.id.linha + " .");
								view.Ef = 1;
							}
					}
					else if(chamadaArgs != null && declArgs == null && view.Ef ==0) {
						if(view.Ef == 0) {
							view.tC.append("Exceeded Arguments in Declaration on row " + decl.id.linha + " .");
								view.Ef = 1;
							}
						
					}else
						if(chamadaArgs.exp.tipo != ((nodeTipoSimples)declArgs.tipo).tipo){
							if(view.Ef == 0) {
								view.tC.append("Type of Incorrect Arguments in Declaration on row " + decl.id.linha + " .");
									view.Ef = 1;
								}
						}
					else {
					
					chamadaArgs = (sequencialExpressao)chamadaArgs.next;
					declArgs = (nodeDecVariavel)declArgs.next;
					}
					
				}
			}

	}

	@Override
	public void visitFatorVar(nodeFatorVar f) {
		if(f != null && view.Ef ==0) {
			if(f.var != null && view.Ef ==0) {
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
		if(c != null && view.Ef ==0) {
			if(c.comandoIf != null && view.Ef ==0) { 
				c.expressao.visit(this);
				byte tipo = c.expressao.tipo;
				if(tipo != Token.BOOLEAN) {
					if(view.Ef == 0) {
						view.tC.append("Incompatible Types on If Expression ");
							view.Ef = 1;
						}
				}
				c.comandoIf.visit(this);
				if(c.comandoElse != null && view.Ef ==0) {

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
		if(p != null && view.Ef ==0) {
		if(p.lista!=null && view.Ef ==0) p.lista.visit(this);
		}

	}

	@Override
	public void visitPComando(nodePComando p) {
		if(p != null && view.Ef ==0) {
			if(p.id != null && view.Ef ==0) p.id.visit(this);
			if(p.lista != null && view.Ef ==0) p.lista.visit(this);
		}
	}

	@Override
	public void visitPrograma(nodePrograma p) {
		if(p != null && view.Ef ==0) {
			if(p.corpo != null && view.Ef ==0) {
				this.idTable.openScope();
				p.corpo.visit(this);
				this.idTable.closeScope();
			}
		}
	}

	@Override
	public void visitTermo(nodeTermo t) {
		if(t != null && view.Ef ==0) {


			t.op.visit(this);
			if(t.fator != null && view.Ef ==0) t.fator.visit(this);
			if(t.next != null && view.Ef ==0) t.next.visit(this);

		}
	}

	@Override
	public void visitTipo(nodeTipo t) {
		if(t != null && view.Ef ==0) {
			if(t instanceof nodeTipoSimples)
				((nodeTipoSimples)t).visit(this);
			else 
				((nodeTipoAgregado)t).visit(this);
		}
	}

	@Override
	public void visitTipoAgregado(nodeTipoAgregado t) {
		if(t != null && view.Ef ==0) {
			//System.out.println(t.intLeft.spelling);
			//System.out.println(t.intRight.spelling);
			
			if(t.intLeft.code != Token.INTLITERAL || t.intRight.code != Token.INTLITERAL) {
				if(view.Ef == 0) {
					view.tC.append("Literal operands in Array Declaration must be Integers");
						view.Ef = 1;
					}
			}
			
			int leftInt = Integer.parseInt(t.intLeft.spelling);
			int rightInt = Integer.parseInt(t.intRight.spelling);
			if(leftInt > rightInt) {
				if(view.Ef == 0) {
					view.tC.append("The First Operand is Bigger than Second in Array Declaration");
						view.Ef = 1;
					}
			}
			t.tipo.visit(this);
		}
	}

	@Override
	public void visitTipoSimples(nodeTipoSimples t) {
		if(t != null && view.Ef ==0) {
			//System.out.println(Token.SPELLINGS[t.tipo]);
		}

	}

	@Override
	public void visitVariavel(nodeVariavel v) {
		if(v != null && view.Ef ==0) {
			//System.out.println(v.id.spelling);
			if(v.exp != null && view.Ef ==0) v.exp.visit(this);
		}

	}

	@Override
	public void visitWhileComando(nodeWhileComando w) {
		if(w != null && view.Ef ==0) {
		if(w.expressao != null && view.Ef ==0) {
			w.expressao.visit(this);
			byte tipo = w.expressao.tipo;
			if(tipo != Token.BOOLEAN) {
				if(view.Ef == 0) {
					view.tC.append("Incompatible Types in While Expression");
						view.Ef = 1;
					}
			}
		}
		if(w.comando != null && view.Ef ==0) {
			w.comando.visit(this);
			}
		}
	}

	@Override
	public void visitSequencialComando(sequencialComando sc) {
		if(sc != null && view.Ef ==0) {
			if(sc.C1 != null && view.Ef ==0) {sc.C1.visit(this);}
			if(sc.C2 != null && view.Ef ==0) {
				sc.C2.visit(this);
			}
		}

	}

	@Override
	public void visitSequencialDeclaration(sequencialDeclaration sd) {
		if(sd != null && view.Ef ==0) {
			if(sd.D1 != null && view.Ef ==0) sd.D1.visit(this);
			if(sd.D2 != null && view.Ef ==0) {
				sd.D2.visit(this);
			}
		}

	}

	@Override
	public void visitSequencialExpressao(sequencialExpressao se) {
		if(se != null && view.Ef ==0) {
			if(se.exp != null && view.Ef ==0) se.exp.visit(this);
			if(se.next != null && view.Ef ==0) {
				se.next.visit(this);
			}
		}
	}

	@Override
	public void visitSequencialIdentificador(sequencialIdentificador si) {
		if(si != null && view.Ef ==0) {

			if(si.I1 != null && view.Ef ==0) si.I1.visit(this);			
			if(si.I2 != null && view.Ef ==0) {
				si.I2.visit(this);
			}
		}
	}
	@Override
	public void visitSequencialParametro(sequencialParametro sp) {
		if(sp != null && view.Ef ==0) {
			if(sp.P1 != null && view.Ef ==0) sp.P1.visit(this);
			if(sp.P2 != null && view.Ef ==0) {
				sp.P2.visit(this);
			}
		}
	}

	@Override
	public void visitSeletor(nodeSeletor s) {
		if(s != null && view.Ef ==0) {
			if(s.lista != null && view.Ef ==0) s.lista.visit(this);
			sequencialExpressao e = (sequencialExpressao)s.lista;
			while(e != null && view.Ef ==0) {
				byte tipo = e.exp.tipo;
				if(tipo != Token.INTEGER) {
					if(view.Ef == 0) {
						view.tC.append("Array Operands Must Be Integers.");
							view.Ef = 1;
						}
				}
				e = (sequencialExpressao)e.next;
			}
		}
	}
}

