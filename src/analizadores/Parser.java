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
	
	public void parse() {
		try {
		currentToken = scanner.scan();
		
		if(compare(Token.EOF)) {
				System.out.println("Erro: Nenhum texto foi encontrado!");
				return;
			}
		
		parsePrograma();
		if(compare(Token.EOF)) {
			System.out.println("Compilado!");
		}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
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
			//comando = parseCondicional();
			break;
		
		case Token.WHILE:	// para a regra de iteraçao, first while
			//comando = parseIterativo();
			break;
		
		case Token.BEGIN:	//para a regra de begin, first begin
			//comando = parseComandoComposto();
			break;
		
		case Token.IDENTIFIER:
		{
			nodeIdentificador id = parseIdentifier(); //reconhece o identificador
			switch(currentToken.code) {  //faz um switch interno para verificar as proximas regras
		
		case Token.RCOL:	//se achar um "[" de inicio, então pega "[expressao]*"
			nodeAtribComando atribAST = new nodeAtribComando();
			
			listaExpressao first,last,d;
			first = null;
			last = null;
			while(currentToken.code == Token.RCOL) {
				acceptIt();
				d = new listaExpressao(); 
				d.exp = parseExpressao();
				d.next = null;
				accept(Token.RCOL);
				if(first == null)
					first = d;
				else
					last.next = d;
				last=d;
			}
			//criando o ponteiro para a variavel
			nodeVariavel var = new nodeVariavel();
			var.id = id;
			var.exp = first;
			
			case Token.BECOMES:	//se nao foi o [ mas foi o := , entao pega := expressao
				acceptIt();
				nodeExpressao eAST = parseExpressao();
				
				atribAST.var = var;
				atribAST.expressao = eAST;
				comando = atribAST;
				break;	//sai do laço interno
				
				
			case Token.LPAREN: // "(" (<lista-de-expressões> | <vazio>) ")"
				nodePComando callProced = new nodePComando();
				nodeExpressao listaE;
				
				acceptIt();
				if(compare(Token.BOOLLIT) || compare(Token.INTLITERAL) || compare(Token.FLOATLIT) || compare(Token.LPAREN) || compare(Token.IDENTIFIER)) {
				listaE = parseListaDeExpressoes();
				}
				accept(Token.RPAREN);
				callProced.id = id;
				callProced.lista = listaE;
				comando = callProced;
				break; // sai do laço interno
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
    	nodeDeclaracao decAST;
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

	private void parseFator(){ //regra em observação
		switch(currentToken.code) {
		case Token.BOOLLIT:case Token.INTLITERAL: case Token.FLOATLIT:
			acceptIt();
			break;
			
		case Token.LPAREN:
			acceptIt();
			parseExpressao();
			accept(Token.RPAREN);
			break;
			
		case Token.IDENTIFIER:
		{
			parseIdentifier();
			switch(currentToken.code) {
			case Token.LCOL:
				while(compare(Token.LCOL)) {
					acceptIt();
					parseExpressao();
					accept(Token.RCOL);
				}
				break;
			case Token.LPAREN:
				acceptIt();
				if(compare(Token.BOOLLIT) || compare(Token.INTLITERAL) || compare(Token.FLOATLIT) || compare(Token.LPAREN) || compare(Token.IDENTIFIER))
				parseListaDeExpressoes();
				
				accept(Token.RPAREN);
				break;
			}
			break;
//			if(compare(Token.OPAD) || compare(Token.OPREL) || compare(Token.RPAREN) || compare(Token.THEN) || 
//					compare(Token.POINT) || compare(Token.DO) || compare(Token.RCOL) || compare(Token.END) || 
//					compare(Token.SEMICOLON)) // loockaheads
//				break;
		}
			default:
				SyntacticError1(currentToken);
		}
	}
    
    private void parseIterativo(){
		accept(Token.WHILE);
		parseExpressao();
		accept(Token.DO);
		parseComando();
	}
    
    
    private void parseListaDeExpressoes(){
		parseExpressao();
		while(compare(Token.POINT)) {
			acceptIt();
			parseExpressao();
		}
	}
	
    private void parseListaDeIDs(){
		parseIdentifier();
		while(compare(Token.POINT)) {
			acceptIt();
			parseIdentifier();
		}
    	
	}
    
    private void parseListaDeParametros(){
    	parseParametros();
    	while(compare(Token.SEMICOLON)) {
    		acceptIt();
    		parseParametros();
    	}
		
	}
    
    private void parseParametros(){
		if(compare(Token.VAR))
			acceptIt();
		parseListaDeIDs();
		accept(Token.COLON);
		parseTipoSimples();
	}
    
    private nodeExpressao parseTermo(){
    	
    	parseFator();
    	while(compare(Token.OPMUL)) {
    		acceptIt();
    		parseFator();
    	}
	}
    
    private void parseTipo(){
		switch(currentToken.code){
		
		case Token.ARRAY:
			parseTipoAgregado();
			break;
		
		case Token.INTEGER:
		case Token.REAL:
		case Token.BOOLEAN:
			parseTipoSimples();
			break;
		
		default:
			SyntacticError1(currentToken);
			break;
		}
	}
    
    private void parseTipoAgregado(){
		accept(Token.ARRAY);
		accept(Token.LCOL);
		accept(Token.INTLITERAL);
		accept(Token.DOUBLEDOT);
		accept(Token.INTLITERAL);
		accept(Token.RCOL);
		accept(Token.OF);
		parseTipo();
	}
    
    private void parseTipoSimples(){
		switch(currentToken.code) {
		case Token.INTEGER: case Token.BOOLEAN: case Token.REAL:
			acceptIt();
		break;
		
		default:
			SyntacticError1(currentToken);
			break;
		}
	}

}
