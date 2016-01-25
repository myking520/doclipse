package doclipse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Main m=new Main();
		m.initDataBase();
//		m.getConnection();
	}
	public Connection getConnection(){
		try {
			Class.forName( "smallsql.database.SSDriver" );
			java.sql.Connection conn = java.sql.DriverManager.getConnection( "jdbc:smallsql:A" );
		} catch (Exception e) {
			e.printStackTrace();
			initDataBase();
		}
		return null;
	}
	public void close(ResultSet rs,Statement stmt,	Connection conn){
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
	private void initDataBase(){
		Statement stmt=null;
		Connection conn=null;
		try {
			try {
				Class.forName( "smallsql.database.SSDriver" );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn=java.sql.DriverManager.getConnection( "jdbc:smallsql" );
			stmt=	conn.createStatement();
			stmt.execute("DROP  DATABASE doclipse");
			stmt.execute("CREATE DATABASE doclipse");
			this.createTableElement(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.close(null, stmt, conn);
		}
		conn=this.getConnection();
	}
	private void createTableElement(Connection conn){
		StringBuffer sb=new StringBuffer();
		sb.append("create table element {");
		sb.append("id String IDENTITY NOT NULL,");
		sb.append("kind Integer,");
		sb.append("text String  NOT NULL,");
		sb.append("parentID String");
		sb.append("}");
	}
}
