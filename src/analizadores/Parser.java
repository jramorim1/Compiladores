package analizadores;

import java.io.IOException;
import AST.*;

public class Parser {
	
	private Token currentToken;
	private Scanner scanner;
	
	public Parser(String path) throws IOException{
		scanner = new Scanner(path);
	}
	
	private void acceptIt(){
		try {
		currentToken = this.scanner.scan();
		}catch(IOException e) {
			System.out.println(e.getMessage());
			}
	}
	
	private void accept(byte ExpectedKind){
		try {
		if(currentToken.code == ExpectedKind) {
			currentToken = this.scanner.scan();
		}else {
			SyntacticError1(currentToken);
		}
		
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public nodePrograma parse() {
		nodePrograma p = null;
		try {
		currentToken = scanner.scan();
		
		if(compare(Token.EOF)) {
				System.out.println("Erro: Nenhum texto foi encontrado!");
				return null;
			}
		
		p = parsePrograma();
		
		if(compare(Token.EOF)) {
			System.out.println("Compilado!");
		}
		return p;
		
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return p;
	}
	
	private void LexicError(Token t) {
		System.out.println("Erro line " + t.linha + " colum "+ t.coluna + "Token "+ t.spelling + " is not acceptable.");
	}
	
	private void SyntacticError1(Token t){
		System.out.println("Syntaxe Error: line "+ t.linha + " colum " + t.coluna + ". Unexpected token "+t.spelling+ ".");
	}
	
	private void SyntacticError2(Token t){
		System.out.println("Syntaxe Error: line "+ t.linha + " colum " + t.coluna + ". Missing token "+t.spelling+ ".");
		
	}
	
	private nodePrograma parsePrograma(){
			nodePrograma p = new nodePrograma();
			
			accept(Token.PROGRAM);
			p.id = parseIdentifier();
			accept(Token.SEMICOLON);
			p.corpo = parseCorpo();
			accept(Token.DOT);
			
			return p;
	}
	
	private nodeIdentificador parseIdentifier(){
		nodeIdentificador id = null;
		try {
			if(currentToken.code == Token.IDENTIFIER) {
				id = new nodeIdentificador();
				id.spelling = currentToken.spelling;
				currentToken = this.scanner.scan();
			}else {
				SyntacticError1(currentToken);
			}

		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	private nodeComando parseComando(){ //verificar para uma entrada só com id
		nodeComando comando = null;
		
		switch(currentToken.code){
		case Token.IF:	//para a regra de condição, first if
			comando = parseCondicional();
			break;
		
		case Token.WHILE:	// para a regra de iteraçao, first while
			comando = parseIterativo();
			break;
		
		case Token.BEGIN:	//para a regra de begin, first begin
			comando = parseComandoComposto();
			break;
		
		case Token.IDENTIFIER:
		{
			nodeIdentificador id = parseIdentifier();
			nodeVariavel var = new nodeVariavel();
			nodeAtribComando atribAST = new nodeAtribComando();
			var.id = id;
			
			switch(currentToken.code) {  
			
			case Token.RCOL:	//se achar um "[" de inicio, então pega "[expressao]*"

				nodeExpressao e = null;
				while(currentToken.code == Token.RCOL) {
					acceptIt();
					sequencialExpressao current = new sequencialExpressao(e, parseExpressao());
					e = current;
					accept(Token.RCOL);
				}
				//criando o ponteiro para a variavel
				var.exp = e;

			case Token.BECOMES:	//se nao foi o [ mas foi o := , entao pega := expressao
				atribAST.var = var;
				acceptIt();
				nodeExpressao eAST = parseExpressao();
				atribAST.expressao = eAST;
				comando = atribAST;
				return comando;
				//sai do laço interno
				
				
			case Token.LPAREN: // "(" (<lista-de-expressões> | <vazio>) ")"
				nodePComando callProced = new nodePComando();
				nodeExpressao listaE = null;
				
				acceptIt();
				if(compare(Token.BOOLLIT) || compare(Token.INTLITERAL) || compare(Token.FLOATLIT) || compare(Token.LPAREN) || compare(Token.IDENTIFIER)) {
				listaE = parseListaDeExpressoes();
				}
				accept(Token.RPAREN);
				callProced.id = id;
				callProced.lista = listaE;
				comando = callProced;
				break; // sai do laço interno
				
				default:
					SyntacticError1(currentToken);
					break;
			}
				break;
		}
			
		default: // default do comando
			SyntacticError1(currentToken);
			break;
		}
		return comando;
	}
	
	private boolean compare(byte code) {
		return (currentToken.code == code);
	}
	
	private nodeComando parseComandoComposto(){
		nodeComando c1;
		sequencialComando lista = null;
		
		accept(Token.BEGIN);
		while(compare(Token.IF) || compare(Token.BEGIN) || compare(Token.WHILE) || compare(Token.IDENTIFIER)) {
			sequencialComando current = new sequencialComando(lista, parseComando());
			lista = current;
			accept(Token.SEMICOLON);
		}
		accept(Token.END);
		c1 = lista;
		return c1;
	}
    
	private nodeComando parseCondicional(){
		nodeComando c1 = null;
		nodeIfComando condicional;
		nodeExpressao ifExp =null;
		nodeComando ifCom=null;
		nodeComando elseCom=null;
		
		accept(Token.IF);
		ifExp = parseExpressao();
		accept(Token.THEN);
		ifCom = parseComando();
		
		condicional = new nodeIfComando();
		condicional.expressao = ifExp;
		condicional.comandoIf = ifCom;
		condicional.comandoElse = null;
		
		if(currentToken.code == Token.ELSE) {
			acceptIt();
			elseCom = parseComando();
			condicional.comandoElse = elseCom;
		}
		c1 = condicional;
		return c1;
	}
	
    private nodeCorpo parseCorpo(){
    	nodeCorpo corpo = new nodeCorpo();
    	nodeDeclaracao lista = null;
    	while(compare(Token.VAR) || compare(Token.FUNCTION) || compare(Token.PROCEDURE)) {
    		sequencialDeclaration current = new sequencialDeclaration(lista, parseDeclaracao());
    		lista = current;
    		accept(Token.SEMICOLON);
    	}
    	nodeComando comAST = parseComandoComposto();
    	corpo.comandos = comAST;
    	corpo.declarations = lista;
    	return corpo;
	}
    
    private nodeDeclaracao parseDeclaracao(){
    	nodeDeclaracao decAST = null;
    	switch(currentToken.code){
    		
    	case Token.VAR:
    		decAST = parseDeclaracaoDeVariavel();
    		break;
    	
    	case Token.FUNCTION:
    		decAST = parseDeclaracaoDeFuncao();
    		break;
    	
    	case Token.PROCEDURE:
    		decAST = parseDeclaracaoDeProcedimento();
    		break;
    	
    	default:
    		SyntacticError1(currentToken);
    		break;
    	}
    	return decAST;
	}
    
    private nodeDeclaracao parseDeclaracaoDeFuncao(){
    	nodeDecFuncao dfuncAST = new nodeDecFuncao();
    	
    	accept(Token.FUNCTION);
    	nodeIdentificador id = parseIdentifier();
    	accept(Token.LPAREN);
    	
    	nodeParametro p = null;
    	if(compare(Token.VAR) || compare(Token.IDENTIFIER))
    		p = parseListaDeParametros();
    	
    	accept(Token.RPAREN);
    	accept(Token.COLON);
    	nodeTipo tipo = parseTipoSimples();
    	accept(Token.SEMICOLON);
    	nodeCorpo corpo = parseCorpo();
    	
    	dfuncAST.id = id;
    	dfuncAST.lista = p;
    	dfuncAST.tipo = tipo;
    	dfuncAST.corpo = corpo;
    	return dfuncAST;
    	
	}
    
    private nodeDeclaracao parseDeclaracaoDeProcedimento() {
    	nodeDecProcedimento procedAST = new nodeDecProcedimento();
    	
    	accept(Token.PROCEDURE);
    	nodeIdentificador id = parseIdentifier();
    	accept(Token.LPAREN);
    	
    	nodeParametro p = null;
    	if(compare(Token.VAR) || compare(Token.IDENTIFIER))
    		p = parseListaDeParametros();
    	
    	accept(Token.RPAREN);
    	accept(Token.SEMICOLON);
    	nodeCorpo corpo = parseCorpo();
    	
    	procedAST.id = id;
    	procedAST.lista = p;
    	procedAST.corpo = corpo;
    	
    	return procedAST;
    	
	}
    
    private nodeDeclaracao parseDeclaracaoDeVariavel(){
    	nodeDecVariavel dVarAST = new nodeDecVariavel();
    	
    	accept(Token.VAR);
    	dVarAST.lista = parseListaDeIDs();
    	accept(Token.COLON);
    	dVarAST.tipo = parseTipo();
    	
    	return dVarAST;
		
	}
    
    private nodeExpressao parseExpressao(){
    	nodeExpressao left = parseExpressaoSimples();
    	if(compare(Token.OPREL)) {
    		//accept(Token.OPREL);
    		nodeEo current = new nodeEo(left,parseOperator(),parseExpressaoSimples());
    		left = current;
    	}
    	return left;
	}   
    
    private nodeOperator parseOperator() {
    	nodeOperator opAST = null;
    	try {
    	if(compare(Token.OPAD) || compare(Token.OPMUL) || compare(Token.OPREL)) {
    		opAST = new nodeOperator(currentToken);
    		currentToken = this.scanner.scan();
    	}else {
    		SyntacticError1(currentToken);
    	}
    	}catch(IOException e) {
    		System.out.println(e.getMessage());
    	}
    	return opAST;
    }

	private nodeExpressao parseExpressaoSimples() {
		
		nodeExpressao last = parseTermo();
		while(compare(Token.OPAD)) {
			//accept(Token.OPAD);
			nodeEo current = new nodeEo(last,parseOperator(),parseTermo());
			last = current;
		}
		return last;
	}
	
	private nodeLiteral parseLiteral() {
		nodeLiteral litAST = null;
    	try {
    		litAST = new nodeLiteral();
    		litAST.spelling = currentToken.spelling;
    		currentToken = this.scanner.scan();
    		
    	}catch(IOException e) {
    		System.out.println(e.getMessage());
    	}
    	return litAST;
	}
	
	private nodeExpressao parseFator(){
		
		nodeExpressao ex = null;
		switch(currentToken.code) {
		case Token.BOOLLIT:case Token.INTLITERAL: case Token.FLOATLIT:
			//acceptIt();
			nodeEn e = new nodeEn(parseLiteral());
			ex = e;
			break;
			
		case Token.LPAREN:
			acceptIt();
			ex = parseExpressao();
			accept(Token.RPAREN);
			break;
			
		case Token.IDENTIFIER:
		{
			nodeIdentificador id = parseIdentifier();
			nodeVariavel var = new nodeVariavel();
			nodeFatorVar v = new nodeFatorVar();
			var.id = id;
			switch(currentToken.code) {
			case Token.LCOL: //espera um colchete para o seletor
				nodeExpressao first = null;
				while(compare(Token.LCOL)) {
					acceptIt();
					sequencialExpressao current = new sequencialExpressao(first, parseExpressao());
					first = current;
					accept(Token.RCOL);
				}
				//criar construtor para esta bagunça
				var.exp = first;
				v.var = var;
				ex = v;
				break;
				
			case Token.LPAREN:
				acceptIt();
				if(compare(Token.BOOLLIT) || compare(Token.INTLITERAL) || compare(Token.FLOATLIT) || compare(Token.LPAREN) || compare(Token.IDENTIFIER))
				ex = parseListaDeExpressoes();
				accept(Token.RPAREN);
				break;
				
			case Token.SEMICOLON:
				v.var = var;
				v.var.exp = null;
				break;
			}
			break;
		}
			default:
				SyntacticError1(currentToken);
				return null;
		}
		return ex;
	}
    
    private nodeComando parseIterativo(){
    	nodeWhileComando whileAST = new nodeWhileComando();
		accept(Token.WHILE);
		whileAST.expressao = parseExpressao();
		accept(Token.DO);
		whileAST.comando = parseComando();
		
		return whileAST;
	}
    
    
    private nodeExpressao parseListaDeExpressoes(){
    	nodeExpressao last;
		last = parseExpressao();
		while(compare(Token.POINT)) {
			acceptIt();
			sequencialExpressao current = new sequencialExpressao(last, parseExpressao());
			last = current;
		}
		return last;
	}
	
    private nodeIdentificador parseListaDeIDs(){
    	nodeIdentificador last;
		last = parseIdentifier();
		while(compare(Token.POINT)) {
			acceptIt();
			sequencialIdentificador current = new sequencialIdentificador(last, parseIdentifier());
			last = current;
		}
		return last;
	}
    
    private nodeParametro parseListaDeParametros(){
    	nodeParametro last;
    	last = parseParametros();
    	while(compare(Token.SEMICOLON)) {
    		acceptIt();
    		sequencialParametro current = new sequencialParametro(last,parseParametros());
    		last = current;
    	}
		return last;
	}
    
    private nodeParametro parseParametros(){
    	nodeParametro p = new nodeParametro();	
		if(compare(Token.VAR))
			acceptIt();
		p.lista = parseListaDeIDs();
		accept(Token.COLON);
		p.tipo = parseTipoSimples();
		return p;
	}
    
    private nodeExpressao parseTermo(){
    	nodeExpressao e = parseFator();
    	while(compare(Token.OPMUL)) {
    		//acceptIt();
    		nodeEo current = new nodeEo(e, parseOperator(),parseFator());
    		e = current;
    	}
    	return e;
	}
    
    private nodeTipo parseTipo(){
    	nodeTipo tipo = null;
		switch(currentToken.code){
		case Token.ARRAY:
			tipo = parseTipoAgregado();
			break;
		
		case Token.INTEGER:
		case Token.REAL:
		case Token.BOOLEAN:
			tipo = parseTipoSimples();
			break;
		
		default:
			SyntacticError1(currentToken);
			break;
		}
		return tipo;
	}
    
    private nodeTipo parseTipoAgregado(){
    	nodeTipoAgregado t = new nodeTipoAgregado();
		accept(Token.ARRAY);
		accept(Token.LCOL);
		t.intLeft = parseLiteral();
		accept(Token.DOUBLEDOT);
		t.intRight = parseLiteral();
		accept(Token.RCOL);
		accept(Token.OF);
		t.tipo = parseTipo();
		
		return t;
	}
    
    private nodeTipo parseTipoSimples(){
    	nodeTipoSimples t = new nodeTipoSimples();
		switch(currentToken.code) {
		case Token.INTEGER: case Token.BOOLEAN: case Token.REAL:
			try {
			t.tipo = currentToken.code;
			currentToken = this.scanner.scan();
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		break;
		
		default:
			SyntacticError1(currentToken);
			break;
		}
		return t;
	}

}
