package analizadores;

import java.io.IOException;

public class Main {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		try{
		String path = "C:\\Users\\oieus\\git\\Compiladores\\src\\programa.txt";	
		Parser parser = new Parser(path);
		parser.parse();
	
		}catch(Exception e){
			System.out.println("N�o foi possivel abrir o arquivo!");
		}

	}

}