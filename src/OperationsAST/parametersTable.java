package OperationsAST;

import java.util.ArrayList;
import AST.nodeDeclaracao;

public class parametersTable {
	public ArrayList<Data> table;
	public int scope;
	
	public parametersTable(int scope) {
		this.table = new ArrayList<Data>();
		this.scope = scope;
	}
	
	public void enter(String identificador, nodeDeclaracao decl) {
		Data data = new Data(scope, identificador, decl);
		for(int i=0; i<table.size();i++) {
			if(table.get(i).id == identificador && table.get(i).local == scope) {
				//return
				System.out.println("Variável #" + table.get(i).id + " ja declarada nesse scopo!");
			}
		}
		this.table.add(data);
	}

}
