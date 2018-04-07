package OperationsAST;

import java.io.FileWriter;
import OperationsAST.Stack;
import java.io.IOException;
import java.io.File;
import AST.*;
import analizadores.Token;

public class Coder implements Visitor{
	
	FileWriter arquivo;
	File codigo;
	Stack pilha;
	boolean running = false;
	
	public Coder() {
		this.pilha = new Stack();
		this.codigo = new File("C:\\Users\\oieus\\Documents\\arquivo.txt");
		try {
		arquivo = new FileWriter(this.codigo);
		}catch(IOException e) {
			e.getMessage();
		}
	}
	
	public void emit(byte op, byte r, int n, int d) {
		try {
		arquivo.write(Instruction.getInstruction(Instruction.LOAD, Instruction.SB, 0, 1));
		arquivo.close();
		}catch(IOException e) {
			e.getMessage();
		}
	}
	
	public void code (nodePrograma p) {
		if(p != null) {
		emit(Instruction.LOAD, Instruction.SB, 0, 1);
		 p.visit (this);
		 
		}
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
			c.lista.visit(this);
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
		
		if(df.id != null) df.id.visit(this);
		if(df.lista != null) df.lista.visit(this);
	
		if(df.tipo != null) df.tipo.visit(this);
		//this.pilha.pushFrame(); //insere os links e atualiza os ponteiros
		if(df.corpo != null) df.corpo.visit(this);
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
		
		if(dp.id != null) dp.id.visit(this);
		
		if(dp.lista != null) dp.lista.visit(this);
		if(dp.corpo != null) dp.corpo.visit(this);
		
	}


}

@Override
public void visitDecVariavel(nodeDecVariavel dv) {
	if(dv != null) {
		//idTable.enter(dv.id.spelling, dp);
		//if(dv.lista != null) dv.id.visit(this);
		//pilha.push(dv, dv.size);
		if(dv.id != null) dv.id.visit(this);
		if(dv.tipo != null) dv.tipo.visit(this);
		if(dv.next != null) dv.next.visit(this);
	}

}

@Override
public void visitEn(nodeEn e) {
	if(e != null) 
		if(e.name != null) {//verificar se nao é literal
		}
}

@Override
public void visitEo(nodeEo e) {//verificar possibilidade aqui
	if(e != null) {
	if(e.op != null) e.op.visit(this);
	if(e.left != null) e.left.visit(this);
	if(e.right != null) e.right.visit(this);
	
		}
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
		}
		else if(e instanceof nodeEo) {
			((nodeEo)e).visit(this);
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
public void visitFatorFunc(nodeFatorFunc f) { //VERIFICAR QUANTIDADE DE PARAMETROS
	if(f != null) {
		if(f.id != null) {
		}
		nodeDecVariavel declArgs = ((nodeDecFuncao)f.decl).lista.lista;
		
		while(declArgs != null) {
			this.pilha.push(declArgs, declArgs.size);
			declArgs = (nodeDecVariavel)declArgs.next;
		}
		
		if(f.lista != null) {
			f.lista.visit(this);
		}
		this.pilha.pushFrame();
		sequencialDeclaration declaracoes = ((sequencialDeclaration)((nodeDecFuncao)f.decl).corpo.declarations);
		
		while(declaracoes != null) {
			if(declaracoes.D1 instanceof nodeDecVariavel) {
				nodeDecVariavel var = ((nodeDecVariavel)declaracoes.D1);
				int size = var.size;
				this.pilha.push(var, size);
			}
			declaracoes = (sequencialDeclaration)declaracoes.D2;
		}
		
	}
}

@Override
public void visitFatorVar(nodeFatorVar f) {
	if(f != null) {
		if(f.var != null) {
			f.var.visit(this);
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
		if(c.expressao != null) c.expressao.visit(this);
		if(c.comandoIf != null) c.comandoIf.visit(this);
			if(c.comandoElse != null)c.comandoElse.visit(this);
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
			p.corpo.visit(this);
			
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
		sequencialExpressao e = (sequencialExpressao)s.lista;
		while(e != null) {
			e = (sequencialExpressao)e.next;
		}
	}
}
}

