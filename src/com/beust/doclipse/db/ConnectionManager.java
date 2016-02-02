package com.beust.doclipse.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

	public static Connection getConnection(String projectName){
		try {
			Class.forName( "org.hsqldb.jdbcDriver" );
			java.sql.Connection conn = java.sql.DriverManager.getConnection( "jdbc:hsqldb:file:"+projectName );
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void close(Connection conn,Statement stmt,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws Exception {
		ConnectionManager mg=new ConnectionManager();
		Connection conn=mg.getConnection("goclipse");
		Statement stmt=conn.createStatement();
	}
}
