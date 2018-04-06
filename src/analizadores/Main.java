package analizadores;
import OperationsAST.*;
import AST.*;

public class Main {

	public static void main(String[] args){
		try{
		String path = "C:\\Users\\oieus\\git\\Compiladores\\src\\analizadores\\programa.txt";	
		//Scanner scanner = new Scanner(path);
		/*Token t = scanner.scan();
		while(t.code != Token.EOF) {
			if(t.code == Token.ERRO) {
				System.out.println("##################");
				System.out.println("O Token " + t.spelling + " é invalido!");
				System.out.println("Linha: " + t.linha);
				System.out.println("Coluna: " + t.coluna);
			}else {
			System.out.println("##################");
			System.out.println("Nome: " + t.spelling);
			System.out.println("Código: " + t.code);
			System.out.println("Linha: " + t.linha);
			System.out.println("Coluna: " + t.coluna);
			}
			t = scanner.scan();
			
		}*/
		Parser parser = new Parser(path);
		nodePrograma p;
		Printer printer = new Printer();
		Checker checker = new Checker();
		p = parser.parse();
		//printer.print(p);
		checker.check(p);
		System.out.println("End.");
	
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.exit(0);
	}
}