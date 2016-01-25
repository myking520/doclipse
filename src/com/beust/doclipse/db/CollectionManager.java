package com.beust.doclipse.db;

import java.sql.Connection;

public class CollectionManager {

	public Connection getConnection(){
		try {
			Class.forName( "smallsql.database.SSDriver" );
			java.sql.Connection conn = java.sql.DriverManager.getConnection( "jdbc:smallsql:doclipse" );
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
