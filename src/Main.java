
import java.io.IOException;

public class Main {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		try{
		String path = "C:\\Users\\jramo\\Downloads\\Compiladores-20180118T124317Z-001\\Teste\\src\\programa.txt";
		Scanner scanner = new Scanner(path);
		Token t = scanner.scan();
		while(t.code != 99){
			System.out.println("name: " + t.spelling);
			System.out.println("code: " + t.code);
			System.out.println("line: " + t.linha);
			System.out.println("column: " + t.coluna);
			t = scanner.scan();
		}
		System.out.println(""+t.spelling);
	
		}catch(Exception e){
			System.out.println("N�o foi possivel abrir o arquivo!");
		}

	}

}