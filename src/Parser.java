import java.io.IOException;

public class Parser {
	
	private Token currentToken;
	private Scanner scanner;
	
	public Parser(String path) throws IOException{
		
		scanner = new Scanner(path);
		currentToken = scanner.scan();
	}
	
	private void acceptIt() throws IOException{
		
		if(Token.ERRO!=currentToken.code)
		currentToken = this.scanner.scan();
		else
		erro();
		
	}
	
	private void erro(){
		
		System.out.println("ERRO");
		
	}
	
	private void parsePrograma() {
		
	}
	
	private void parseComando() throws IOException{
		
		switch(currentToken.code){
		
		case Token.IF:
			parseCondicional();
		
		case Token.WHILE:
			parseIterativo();
		
		case Token.BEGIN:
			parseComandoComposto();
		
		case Token.IDENTIFIER:
			acceptIt();
			while(currentToken.code==Token.LCOL){
				acceptIt();
				parseExpressao();
				if(currentToken.code==Token.RCOL)
					acceptIt();
				else
					erro();
			}
			if(currentToken.code==Token.BECOMES){
				acceptIt();
				parseExpressao();
				
			}
			else 
				erro();
		
		case Token.RPAREN:
			acceptIt();
			if(currentToken.code==Token.BOOLLIT || currentToken.code==Token.INTLITERAL || currentToken.code==Token.FLOATLIT || currentToken.code==Token.LPAREN ||currentToken.code==Token.IDENTIFIER){
				parseListaDeExpressoes();
			}
			if(currentToken.code==Token.LPAREN)
				acceptIt();
			else
				erro();
			
		default:
			erro();
		}
			
	}
	
	private void parseComandoComposto(){
		
	}
    
	private void parseCondicional() throws IOException{
		
		if(currentToken.code==Token.IF){
			acceptIt();
			parseExpressao();
			if(currentToken.code==Token.THEN){
				acceptIt();
				parseComando();
				if(currentToken.code==Token.ELSE){
					acceptIt();
					parseComando();
				}
				
			}
			else
				erro();
		}
		
		else
			erro();
		
	}
	
    private void parseCorpo(){
		
	}
    
    private void parseDeclaracao() throws IOException{
    	
    	switch(currentToken.code){
    		
    	case Token.VAR:
    		parseDeclaracaoDeVariavel();
    	
    	case Token.FUNCTION:
    		parseDeclaracaoDeFuncao();
    	
    	case Token.PROCEDURE:
    		parseDeclaracaoDeProcedimento();
    	
    	default:
    		erro();
    	}
	}
    
    private void parseDeclaracaoDeFuncao(){
		
	}
    
    private void parseDeclaracaoDeProcedimento() throws IOException{
    	
    	if(currentToken.code==Token.PROCEDURE){
    
    		acceptIt();
    		if(currentToken.code==Token.IDENTIFIER){
    			
    			acceptIt();
    			if(currentToken.code==Token.LPAREN){
    				
    				acceptIt();
    				if(currentToken.code==Token.VAR || currentToken.code==Token.IDENTIFIER){
    					
    					parseListaDeParametros();
    				}
    				if(currentToken.code==Token.RPAREN){
    					
        				acceptIt();
        				if(currentToken.code==Token.SEMICOLON){
        					
            				acceptIt();
            				if(currentToken.code==Token.BEGIN || currentToken.code==Token.VAR || currentToken.code==Token.FUNCTION || currentToken.code==Token.PROCEDURE ){
            					
            					parseCorpo();
            				}
            				else
            					erro();
        				}
        				else
        					erro();
    				}
    				else
    					erro();
    			}
    			else
            		erro();
    		}
    		else
        		erro();
    		   	     	
    	}
    	else
    		erro();
		
	}
    
    private void parseDeclaracaoDeVariavel(){
		
	}
    
    private void parseExpressao() throws IOException{
    	
    	if(currentToken.code==Token.BOOLLIT || currentToken.code==Token.INTLITERAL || currentToken.code==Token.FLOATLIT || currentToken.code==Token.LPAREN ||currentToken.code==Token.IDENTIFIER){
			
			parseTermo();
			while(currentToken.code==Token.OPAD){
				acceptIt();
				parseTermo();
			}
			if(currentToken.code==Token.OPREL){
				acceptIt();
				parseTermo();
				while(currentToken.code==Token.OPAD){
					acceptIt();
					parseTermo();
				}
			}
			else
				erro();
			
		}
		else
			erro();
		
	}   
    
    private void parseFator(){
		
	}
    
    private void parseIterativo() throws IOException{
		
    	if(currentToken.code==Token.WHILE){
			acceptIt();
    		parseExpressao();
    		if(currentToken.code==Token.DO){
    			acceptIt();
    			parseComando();
    		}
    		else
    			erro();
			
		}
		else
			erro();
	}
    
    
    private void parseListaDeExpressoes(){
		
	}
	
    private void parseListaDeIDs() throws IOException{
		if(currentToken.code==Token.IDENTIFIER){
			acceptIt();
			while(currentToken.code==Token.POINT){
				acceptIt();
				if(currentToken.code==Token.IDENTIFIER){
					acceptIt();
				}
				else
					erro();
			}
		}
		else
			erro();
	}
    
    private void parseListaDeParametros(){
		
	}
    
    private void parseParametros() throws IOException{
		
    	switch(currentToken.code){
    	
    	case Token.VAR:
    		acceptIt();
    		parseListaDeIDs();
    		if(currentToken.code==Token.COLON){
    			acceptIt();
    			parseTipoSimples();
    		}
    		else
    			erro();
    	case Token.IDENTIFIER:	
    		parseListaDeIDs();
    		if(currentToken.code==Token.COLON){
    			acceptIt();
    			parseTipoSimples();
    		}
    		else
    			erro();
    	
    	default:
    		erro();
    	}
	}
    
    private void parseTermo(){
		
	}
    
    private void parseTipo() throws IOException{
		switch(currentToken.code){
		
		case Token.ARRAY:
			parseTipoAgregado();
		
		case Token.INTEGER:
			parseTipoSimples();
		
		default:
			erro();
		}
	}
    
    private void parseTipoAgregado(){
		
	}
    
    private void parseTipoSimples() throws IOException{
		if(currentToken.code==Token.INTEGER || currentToken.code==Token.REAL || currentToken.code==Token.BOOLEAN){
			acceptIt();
		}
		else
			erro();
	}

}
