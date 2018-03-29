package analizadores;
import OperationsAST.*;
import AST.*;

public class Main {

	public static void main(String[] args){
		try{
		String path = "C:\\Users\\oieus\\git\\Compiladores\\src\\analizadores\\programa.txt";	
		Parser parser = new Parser(path);
		nodePrograma p;
		Printer printer = new Printer();
		Checker checker = new Checker();
		p = parser.parse();
		//printer.print(p);
		checker.check(p);
	
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

}