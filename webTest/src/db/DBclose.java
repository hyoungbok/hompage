package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBclose {

	public static void close(Statement stmt, Connection con) {
		
		try {
			if(stmt!=null)stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (con!=null)con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
		public static void close(PreparedStatement pstmt, Connection con) {
			
			try {
				if(pstmt!=null)pstmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (con!=null)con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		
		
		
		
	}

	public static void close(ResultSet rs, Statement stmt, Connection con) {
		try {
			if(rs!=null)rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		try {
			if(stmt!=null)stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (con!=null)con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
		public static void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
			try {
				if(rs!=null)rs.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				if(pstmt!=null)pstmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (con!=null)con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		
	}

}
