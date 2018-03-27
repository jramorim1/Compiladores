package OperationsAST;

import java.util.ArrayList;
import AST.nodeDeclaracao;

public class IdentificationTable {
	public ArrayList<Data> table;
	public int scope;
	
	public IdentificationTable() {
		this.table = new ArrayList<Data>();
		this.scope = -1;
	}
	
	public void enter(String identificador, nodeDeclaracao decl) {
		Data data = new Data(scope, identificador, decl);
		for(int i=0; i<table.size();i++) {
			if((table.get(i).id.compareTo(identificador) == 0 ) && table.get(i).local == scope) {
				System.out.println("Variável #" + table.get(i).id + " ja declarada nesse scopo!");
				System.exit(0);
			}
		}
		this.table.add(data);
	}
	
	public byte retrieve(String id) {
		Data temp = null;
		
		for(int i=0; i<table.size();i++) { 
			
			if(table.get(i).id.compareTo(id) == 0) { 
				
				int localTemp = table.get(i).local;
				
					for(int j=0; j<table.size(); j++) {
						if(table.get(i).id.compareTo(table.get(j).id) == 0 && table.get(j).local >= localTemp) {
							
							temp = table.get(j);
							localTemp = temp.local;
							
						}
					}
					
				return temp.type; //Função deve retornar o ponteiro da declaração da variavel
								//Se houver mais de uma declaração, retornar aquela de maior Scopo
			}
		}
			System.out.println("Variavel " + id + " não declarada");
			System.exit(0);
			return 0;
	}
	
	public void openScope() {
		this.scope++;
	}
	
	public void closeScope() {
		for(int i=table.size() - 1; i>=0; i--){
			if(this.table.get(i).local == this.scope) {
				this.table.remove(i);
			}
		}
		this.scope--;
	}

}
