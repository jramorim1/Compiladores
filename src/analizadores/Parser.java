package analizadores;

import java.io.IOException;

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
	
	private void parsePrograma(){
			accept(Token.PROGRAM);
			parseIdentifier();
			accept(Token.SEMICOLON);
			parseCorpo();
			accept(Token.DOT);
	}
	
	private void parseIdentifier(){
		accept(Token.IDENTIFIER);
	}

	private void parseComando(){
		
		switch(currentToken.code){
		case Token.IF:	//para a regra de condição, first if
			parseCondicional();
			break;
		
		case Token.WHILE:	// para a regra de iteraçao, first while
			parseIterativo();
			break;
		
		case Token.BEGIN:	//para a regra de begin, first begin
			parseComandoComposto();
			break;
		
		case Token.IDENTIFIER:
		{
			parseIdentifier(); //reconhece o identificador
			
			/**para as regras de atribuição e chamada de procedimento
			 * fazendo a fatoração das duas regras, first externo é id
			 * apos isso é verificado as regras 
			 * [expressao]* := expressao
			 * (lista de expressao | vazio) 
			 * 
			 * como os firsts dessas regras internas são disjuntos
			 * podemos aplicar o algoritmo
			 * */
			
			switch(currentToken.code) {  //faz um switch interno para verificar as proximas regras
		
		case Token.RCOL:	//se achar um "[" de inicio, então pega "[expressao]*" 
			while(currentToken.code == Token.RCOL) {
				acceptIt();
				parseExpressao();
				accept(Token.RCOL);
			}
			case Token.BECOMES:	//se nao foi o [ mas foi o := , entao pega := expressao
				acceptIt();
				parseExpressao();
				break;	//sai do laço interno
				
				
			case Token.LPAREN: // "(" (<lista-de-expressões> | <vazio>) ")"	 OBS first lista={bool, int, float, lparen}
				acceptIt();
				
				if(compare(Token.BOOLLIT) || compare(Token.INTLITERAL) || compare(Token.FLOATLIT) || compare(Token.LPAREN) || compare(Token.IDENTIFIER))
					parseListaDeExpressoes();
				
				accept(Token.RPAREN);
				break; // sai do laço interno
			}
//			if(compare(Token.END) || compare(Token.ELSE) || compare(Token.SEMICOLON))
				break;
		}
			
		default: // default do comando
			SyntacticError1(currentToken);
			break;
		}
	}
	
	private boolean compare(byte code) {
		return (currentToken.code == code);
	}
	
	private void parseComandoComposto(){
		accept(Token.BEGIN);
		while(compare(Token.IF) || compare(Token.BEGIN) || compare(Token.WHILE) || compare(Token.IDENTIFIER)) {
			parseComando();
			accept(Token.SEMICOLON);
		}
		accept(Token.END);
	}
    
	private void parseCondicional(){
		accept(Token.IF);
		parseExpressao();
		accept(Token.THEN);
		parseComando();
		if(currentToken.code == Token.ELSE) {
			acceptIt();
			parseComando();
		}
		
	}
	
    private void parseCorpo(){
    	
    	while(compare(Token.VAR) || compare(Token.FUNCTION) || compare(Token.PROCEDURE)) {
    		parseDeclaracao();
    		accept(Token.SEMICOLON);
    	}
    	parseComandoComposto();
	}
    
    private void parseDeclaracao(){
    	
    	switch(currentToken.code){
    		
    	case Token.VAR:
    		parseDeclaracaoDeVariavel();
    		break;
    	
    	case Token.FUNCTION:
    		parseDeclaracaoDeFuncao();
    		break;
    	
    	case Token.PROCEDURE:
    		parseDeclaracaoDeProcedimento();
    		break;
    	
    	default:
    		SyntacticError1(currentToken);
    		break;
    	}
	}
    
    private void parseDeclaracaoDeFuncao(){
    	accept(Token.FUNCTION);
    	parseIdentifier();
    	accept(Token.LPAREN);
    	
    	if(compare(Token.VAR) || compare(Token.IDENTIFIER))
    		parseListaDeParametros();
    	
    	accept(Token.RPAREN);
    	accept(Token.COLON);
    	parseTipoSimples();
    	accept(Token.SEMICOLON);
    	parseCorpo();
	}
    
    private void parseDeclaracaoDeProcedimento() {
 
    	accept(Token.PROCEDURE);
    	parseIdentifier();
    	accept(Token.LPAREN);
    	
    	if(compare(Token.VAR) || compare(Token.IDENTIFIER))
    		parseListaDeParametros();
    	
    	accept(Token.RPAREN);
    	accept(Token.SEMICOLON);
    	parseCorpo();
    	
	}
    
    private void parseDeclaracaoDeVariavel(){
    	
    	accept(Token.VAR);
    	parseListaDeIDs();
    	accept(Token.COLON);
    	parseTipo();
		
	}
    
    private void parseExpressao(){
    	parseExpressaoSimples();
    	if(compare(Token.OPREL)) {
    		accept(Token.OPREL);
    		parseExpressaoSimples();
    	}
    	
	}   

	private void parseExpressaoSimples() {
		parseTermo();
		while(compare(Token.OPAD)) {
			accept(Token.OPAD);
			parseTermo();
		}
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
    
    private void parseTermo(){
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
		}
	}

}
