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
    	
    	if(currentToken.code==Token.FUNCTION){
    		acceptIt();
    	}
		
	}
    
    private void parseDeclaracaoDeFuncao(){
		
	}
    
    private void parseDeclaracaoDeProcedimento(){
		
	}
    
    private void parseDeclaracaoDeVariavel(){
		
	}
    
    private void parseExpressao(){
		
	}   
    
    private void parseFator(){
		
	}
    
    private void parseIterativo(){
		
	}
    
    
    private void parseListaDeExpressoes(){
		
	}
	
    private void parseListaDeIDs(){
		
	}
    
    private void parseListaDeParametros(){
		
	}
    
    private void parseParametros(){
		
	}
    
    private void parseTermo(){
		
	}
    
    private void parseTipo(){
		
	}
    
    private void parseTipoAgregado(){
		
	}
    
    private void parseTipoSimples(){
		
	}

}
