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
		p = parser.parse();
		printer.print(p);
	
		}catch(Exception e){
			System.out.println("N�o foi possivel abrir o arquivo!");
		}

	}

}