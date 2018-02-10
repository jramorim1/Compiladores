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
	
	private void parsePrograma() throws IOException {
		
		if(currentToken.code==Token.PROGRAM){
			acceptIt();
			if(currentToken.code==Token.IDENTIFIER){
				acceptIt();
				if(currentToken.code==Token.SEMICOLON){
					acceptIt();
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
	
	private void parseComando() throws IOException{
		
		switch(currentToken.code){
		
		case Token.IF:
			parseCondicional();
			break;
		
		case Token.WHILE:
			parseIterativo();
			break;
		
		case Token.BEGIN:
			parseComandoComposto();
			break;
		
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
			break;
		
		case Token.RPAREN:
			acceptIt();
			if(currentToken.code==Token.BOOLLIT || currentToken.code==Token.INTLITERAL || currentToken.code==Token.FLOATLIT || currentToken.code==Token.LPAREN ||currentToken.code==Token.IDENTIFIER){
				parseListaDeExpressoes();
			}
			if(currentToken.code==Token.LPAREN)
				acceptIt();
			else
				erro();
			break;
			
		default:
			erro();
			break;
		}
			
	}
	
	private void parseComandoComposto() throws IOException{
		
		if(currentToken.code==Token.BEGIN){
			
			while(currentToken.code==Token.IF|| currentToken.code==Token.WHILE || currentToken.code==Token.BEGIN || currentToken.code==Token.IDENTIFIER ||currentToken.code==Token.LPAREN){
				parseComando();
				if(currentToken.code==Token.SEMICOLON)
					acceptIt();
				else
					erro();
			}
			
			if(currentToken.code==Token.END)
				acceptIt();
			else
				erro();
			
		}
		else
			erro();
		
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
	
    private void parseCorpo() throws IOException{
    	
    	while(currentToken.code==Token.VAR || currentToken.code==Token.FUNCTION || currentToken.code==Token.PROCEDURE){
    		parseDeclaracao();
    		if(currentToken.code==Token.SEMICOLON)
				acceptIt();
			else
				erro();
    	}
    	
    	if(currentToken.code==Token.BEGIN)
			acceptIt();
		else
			erro();
		
	}
    
    private void parseDeclaracao() throws IOException{
    	
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
    		erro();
    		break;
    	}
	}
    
    private void parseDeclaracaoDeFuncao() throws IOException{
    	
    	if(currentToken.code==Token.FUNCTION){
    	    
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
        				if(currentToken.code==Token.COLON){
        					
            				acceptIt();
            				if(currentToken.code==Token.INTEGER || currentToken.code==Token.REAL || currentToken.code==Token.BOOLEAN){
            					
            					parseTipoSimples();
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
    		else
        		erro();
    		   	     	
    	}
    	else
    		erro();
		
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
    
    private void parseDeclaracaoDeVariavel() throws IOException{
    	
    	if(currentToken.code==Token.VAR){
			
    		acceptIt();
			parseListaDeIDs();
			if(currentToken.code==Token.COLON){
				
				acceptIt();
				parseTipo();
			}
			else
				erro();
		}
		else
			erro();
		
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
    
    private void parseFator() throws IOException{
		
    	switch(currentToken.code){
    		
    		case Token.BOOLLIT: 
    		case Token.INTLITERAL:
    		case Token.FLOATLIT:
    			acceptIt();
    			break;
    		
    		case Token.LPAREN:
    			
    			acceptIt();
    			if(currentToken.code==Token.BOOLLIT || currentToken.code==Token.INTLITERAL || currentToken.code==Token.FLOATLIT || currentToken.code==Token.LPAREN ||currentToken.code==Token.IDENTIFIER){
    				
    				parseExpressao();
    				while(currentToken.code==Token.POINT){
    					acceptIt();
    					parseExpressao();
    				}
    			}
    			
    			if(currentToken.code==Token.RPAREN){
    				acceptIt();
    			}
    			else
    				erro();
    			break;
    		
    		case Token.IDENTIFIER:
    			
    			acceptIt();
    			while(currentToken.code==Token.LCOL){
    				acceptIt();
    				parseExpressao();
    				if(currentToken.code==Token.RCOL)
    				{
    					acceptIt();
    				}
    				else
    					erro();
    			}
    			break;
    		
    		default:
    			erro();
    			break;
    		
    	}
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
    
    
    private void parseListaDeExpressoes() throws IOException{
		
    	if(currentToken.code==Token.BOOLLIT || currentToken.code==Token.INTLITERAL || currentToken.code==Token.FLOATLIT || currentToken.code==Token.LPAREN ||currentToken.code==Token.IDENTIFIER){
			
			parseExpressao();
			while(currentToken.code==Token.POINT){
				acceptIt();
				parseExpressao();
			}
		}
    	else
    		erro();
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
    
    private void parseListaDeParametros() throws IOException{
    	
    	if(currentToken.code==Token.VAR || currentToken.code==Token.IDENTIFIER){
    		
    		parseParametros();
    		while(currentToken.code==Token.POINT){
    			
    			acceptIt();
    			parseParametros();
    		}
    	}
    	else
    		erro();
		
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
    		break;
    	case Token.IDENTIFIER:	
    		parseListaDeIDs();
    		if(currentToken.code==Token.COLON){
    			acceptIt();
    			parseTipoSimples();
    		}
    		else
    			erro();
    		break;
    	
    	default:
    		erro();
    		break;
    	}
	}
    
    private void parseTermo() throws IOException{
    	
    	if(currentToken.code==Token.BOOLLIT || currentToken.code==Token.INTLITERAL || currentToken.code==Token.FLOATLIT || currentToken.code==Token.LPAREN ||currentToken.code==Token.IDENTIFIER){
    		parseFator();
    		while(currentToken.code==Token.OPMUL){
    			acceptIt();
    			parseFator();
    		}
    	}
    	else
    		erro();
	}
    
    private void parseTipo() throws IOException{
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
			erro();
			break;
		}
	}
    
    private void parseTipoAgregado() throws IOException{
		
    	if(currentToken.code==Token.ARRAY){
    		acceptIt();
    		if(currentToken.code==Token.LCOL){
        		acceptIt();    		
        		if(currentToken.code==Token.INTLITERAL){
            		acceptIt();    		
            		if(currentToken.code==Token.DOUBLEDOT){
                		acceptIt();    		
                		if(currentToken.code==Token.INTLITERAL){
                    		acceptIt();    		
                    		if(currentToken.code==Token.RCOL){
                        		acceptIt();    		
                        		if(currentToken.code==Token.OF){
                            		acceptIt();    		
                            		parseTipo();
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
    	else
    		erro();
	}
    
    private void parseTipoSimples() throws IOException{
		if(currentToken.code==Token.INTEGER || currentToken.code==Token.REAL || currentToken.code==Token.BOOLEAN){
			acceptIt();
		}
		else
			erro();
	}

}
