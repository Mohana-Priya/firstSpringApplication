package com.howtodoinjava.web;

import java.sql.*;

public class DataCon
{
	public static Connection con;
	public static Statement st;	
	
	static void establishConnection(){
		try{
			Class.forName("oracle.jdbc.OracleDriver");			
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","pp5849");
			st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}							
	}
	
	static void closeConnection(){
		try{
			st.close();
			con.close();
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}							
	}
}