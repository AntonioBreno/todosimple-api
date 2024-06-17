package com.brenogomes.todosimple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BancoDeDados {

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultset = null;
	
	
	public void conectar(){
		String servidor = "jdbc:mysql://localhost:3306/todosimple?createDatabaseIfNotExist=true";
		String usuario = "root";
		String senha ="brenogomes10" ;
		//String diver = "com.mysql.jdbc.Driver";
		String diver = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(diver);
			this.connection = DriverManager.getConnection(servidor, usuario, senha);
			this.statement = this.connection.createStatement();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
