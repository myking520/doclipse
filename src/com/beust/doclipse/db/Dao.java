package com.beust.doclipse.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.preferences.template.TemplateElement;

public class Dao {
	String[] table_elements = { "id", "kind", "name", "update_time", "parent_id" };
	private String getID(){
		return UUID.randomUUID().toString();
	}
	public void saveOrUpdateTemplateElement(TemplateElement element) {
		Connection conn = ConnectionManager.getConnection(DoclipseProject.getCurrentProject().getName());
		if(element.getId()==null){
				
		}
	}
	public void saveOrUpdateParetFolder(TemplateElement element){
	
	}
	public void initDB(String databaseName) {
		Connection conn = ConnectionManager.getConnection(databaseName);
		this.dropTable(conn,"element");
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(this.createNormalTable("element", table_elements));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmt, null);
		}
	}
	private void dropTable(Connection conn,String tableName){
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute("drop Table "+tableName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionManager.close(null, stmt, null);
		}
	}
	private String createNormalTable(String tableName, String[] columns) {
		StringBuffer sql = new StringBuffer(" CREATE TABLE IF NOT EXISTS  ");
		sql.append(tableName).append(" ( ");
		for (int i = 0; i < columns.length; i++) {
			String column = columns[i];
			sql.append(column).append(" VARCHAR(500) ");
			if (i == 0) {
				sql.append("NOT NULL PRIMARY KEY,");
				continue;
			}
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		return sql.toString();
	}
	private String[] pathFolder(File file){
		File parent=null;
		List<String>  path=new ArrayList<String>();
		while((parent=file.getParentFile())!=null){
			if(parent.getName().length()>0){
				path.add(parent.getName());
			}else{
				path.add(parent.getPath());
			}
			file=parent;
		}
		Collections.reverse(path);
		return  path.toArray(new String[path.size()]);
	}
	public static void main(String[] args) throws IOException {
		Dao dao = new Dao();
		dao.initDB("doclipse");
	}
}
