
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;

public class Scanner {
	private FileInputStream file;
	private InputStreamReader openFile;
	private PushbackReader reader;
	private int line=0;
	private int column=0;
	
	private Character currentChar;
	private byte currentCode;
	private StringBuffer currentSpelling;
	
	public Scanner(String path) throws IOException {
		this.file = new FileInputStream(path);
		this.openFile = new InputStreamReader(this.file);
		this.reader = new PushbackReader(this.openFile);
		this.currentChar = getChar();
	}
	
	//metodo que retorna o proximo char do buffer
	//se for EOF retorna null
	public char getChar() throws IOException{
		Character c = null;
		int i = (int)this.reader.read();
		if(i != -1)
			c = (char)i;
		column++;
		return c;
	}
	
	//Metodo que retorna um token
	private byte scanToken() throws IOException{
		switch(currentChar){
		
		case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
		case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
		case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'x': case 'w': 
		case 'y': case 'z':
			takeIt();
			while(isLetter(currentChar)||isDigit(currentChar))
				takeIt();
			return Token.IDENTIFIER;
			
		case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
			takeIt();
			while(isDigit(currentChar))
				takeIt();
					if(currentChar == '.'){
						takeIt();
						if(isDigit(currentChar)){
							takeIt();
							while(isDigit(currentChar))
								takeIt();
							return Token.FLOATLIT;
						}
						return Token.FLOATLIT;
					}else	
						return Token.INTLITERAL;
		case '.':
			takeIt();
			if(isDigit(currentChar)){
				takeIt();
				while(isDigit(currentChar))
					takeIt();
				return Token.FLOATLIT;
			}else if(currentChar == '.')
				return Token.DOUBLEDOT;
			else 
				return Token.DOT;
			
		case '+': case '-':
			takeIt();
			return Token.OPAD;
			
		case '*': case '/':
			takeIt();
			return Token.OPMUL;
			
		case '=':
			takeIt();
			return Token.OPREL;
			
		case '<':
			takeIt();
			if(currentChar == '='){
				takeIt();
				return Token.OPREL;
			}else if(currentChar == '>'){
				takeIt();
				return Token.OPREL;
			}else
			return Token.OPREL;
			
		case '>':
			takeIt();
			if(currentChar == '='){
				takeIt();
				return Token.OPREL;
			}else
				return Token.OPREL;
			
		case ':':
			takeIt();
			if(currentChar == '='){
				takeIt();
				return Token.BECOMES;
			}else
				return Token.COLON;
			
		case ',':
			takeIt();
			return Token.POINT;
			
		case ';':
			takeIt();
			return Token.SEMICOLON;
			
		case '[':
			takeIt();
			return Token.LCOL;
			
		case ']':
			takeIt();
			return Token.RCOL;
			
		case '(':
			takeIt();
			return Token.LPAREN;
			
		case ')':
			takeIt();
			return Token.RPAREN;
			
		default:
			return Token.ERRO;
			
		}
	}
	
	//consome os separadores
private void scanSeparator() throws IOException{
	switch(currentChar){
	case ' ': case '\n': case '\r':
		if(currentChar == '\n'){
			line++;
			column=0;
		}
		currentChar = getChar();
		//takeIt();
		break;
	case '!':
		while(isGraphic(currentChar))
			currentChar=getChar();
		break;
	}
	//falta implementar o comentario com // ou /*
}
//metodo que retorna um token do buffer
public Token scan() throws IOException, EOFException{
	try{
	while((currentChar == ' ')|| (currentChar == '\n') || (currentChar == '\r') || (currentChar=='!')){
		scanSeparator();
	}
	
	currentSpelling = new StringBuffer("");
	currentCode = scanToken();
	
	return new Token(currentCode,currentSpelling.toString(),line,column-currentSpelling.toString().length());
	}catch(NullPointerException e){
		return new Token(Token.EOF,"fim do arquivo",line,column);
	}
}
	
//metodo que aceita um char condicional
private void take(Character expected) throws IOException{
	if(currentChar == expected){
		currentSpelling.append(expected);
		currentChar = getChar();
	}else{
	//erro lexico
	}
}

//método que aceita um char sem condição
private void takeIt() throws IOException{
		this.currentSpelling.append(currentChar);
		this.currentChar = getChar();
}

//metodo que verifica se é digito
private boolean isDigit(Character c){
	int valorAsc2 = (int)c;
	if(valorAsc2 >= 48 && valorAsc2 <= 57)
		return true;
	return false;
}

//metodo que verifica se é letra
private boolean isLetter(Character c){
	int valorAsc2 = (int)c;
	if(valorAsc2 >= 97 && valorAsc2 <= 122)
		return true;
	return false;
}

private boolean isGraphic(Character c){
	int valorAsc2 = (int)c;
	if(valorAsc2 >= 32 && valorAsc2 <=126)
		return true;
	return false;
}

}
