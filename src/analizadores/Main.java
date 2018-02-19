package analizadores;

import java.io.IOException;

public class Main {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		try{
		String path = "D:\\teste.txt";	
		Parser parser = new Parser(path);
		parser.parse();
	
		}catch(Exception e){
			System.out.println("Não foi possivel abrir o arquivo!");
		}

	}

}