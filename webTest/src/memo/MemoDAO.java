package memo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import db.DBOpen;
import db.DBclose;

public class MemoDAO {
	
	
	public int total(Map map) {
		int total=0;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String col=(String)map.get("col");
		String word=(String)map.get("word");
		
		StringBuffer sql=new StringBuffer();
		sql.append(" select count(*) from memo ");
		if(word.trim().length()>0)
			sql.append(" where "+col+" like '%'||?||'%' ");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			if(word.trim().length()>0)
				pstmt.setString(1, word);
			rs=pstmt.executeQuery();
			rs.next();
			total=rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBclose.close(rs, pstmt, con);
		}
		
		return total;
	}
	
	public boolean check(int memono) {
		boolean f=false;
		Connection con=DBOpen.open();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append(" select count(refnum) ");
		sql.append(" from memo where refnum=? ");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setInt(1, memono);
			rs=pstmt.executeQuery();
			rs.next();
			int cnt=rs.getInt(1);
			if(cnt>0) f=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBclose.close(rs, pstmt, con);
		}
		
		
		return f;
		
	}
	
	public void up(Map map) {
		Connection con=DBOpen.open();
		PreparedStatement pstmt=null;
		int grpno=(Integer)map.get("grpno");
		int ansnum=(Integer)map.get("ansnum");
		
		StringBuffer sql=new StringBuffer();
		sql.append(" update memo ");
		sql.append(" set ansnum=ansnum+1 ");
		sql.append(" where grpno=? ");
		sql.append(" and ansnum>? ");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setInt(1, grpno);
			pstmt.setInt(2, ansnum);
			
			pstmt.executeUpdate();
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			DBclose.close(pstmt, con);
		}
					
	}
	
	public boolean reCreate(MemoDTO dto) {
		boolean f=false;
		Connection con=DBOpen.open();
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append(" insert into memo(memono, title, content, wdate,grpno,indent,ansnum,refnum) ");
		sql.append(" values(memo_seq.nextval,?,?,sysdate, ");
		sql.append(" ?,?,?,?) ");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getGrpno());
			pstmt.setInt(4, dto.getIndent()+1);
			pstmt.setInt(5, dto.getAnsnum()+1);
			pstmt.setInt(6, dto.getMemono());
			
			int cnt=pstmt.executeUpdate();
			
			if(cnt>0) 
				f=true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBclose.close(pstmt, con);
		}
		
		return f;
	}
	
	public MemoDTO replyRead(int memono) {
		MemoDTO dto=null;
		Connection con=DBOpen.open();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append(" select title,memono,grpno,indent,ansnum ");
		sql.append(" from memo where memono=? ");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setInt(1, memono);
			
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new MemoDTO();
				dto.setTitle(rs.getString("title"));
				dto.setMemono(rs.getInt("memono"));
				dto.setGrpno(rs.getInt("grpno"));
				dto.setAnsnum(rs.getInt("ansnum"));
				dto.setIndent(rs.getInt("indent"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBclose.close(rs,pstmt, con);
		}
		
		return dto;
	}
	
	public List<MemoDTO> list(Map map) {
		List<MemoDTO> list = new ArrayList<MemoDTO>();
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String col=(String)map.get("col");
		String word=(String)map.get("word");
		int sno=(Integer)map.get("sno");
		int eno=(Integer)map.get("eno");

		StringBuffer sql = new StringBuffer();
		
		sql.append(" select memono,title, ");
		sql.append(" wdate,viewcnt, ");
		sql.append(" grpno,indent,ansnum,r ");
		sql.append(" from( ");
		sql.append(" select memono,title, ");
		sql.append(" wdate,viewcnt, ");
		sql.append(" grpno,indent,ansnum,rownum as r ");
		sql.append(" from( ");
		sql.append(" select memono,title, ");
		sql.append(" to_char(wdate,'yyyy-mm-dd') as wdate, viewcnt, ");
		sql.append(" grpno,indent,ansnum ");
		sql.append(" from memo ");
		if(word.trim().length()>0)
			sql.append(" where "+col+" like '%'||?||'%' ");
		sql.append(" order by grpno DESC,ansnum ");
		sql.append(" 	) ");
		sql.append(" )where r>=? and r<=? ");
		
		int i=0;
		try {
			pstmt = con.prepareStatement(sql.toString());
			if(word.trim().length()>0)
				pstmt.setString(++i, word);
			pstmt.setInt(++i, sno);
			pstmt.setInt(++i, eno);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemoDTO dto = new MemoDTO();
				dto.setMemono(rs.getInt("memono"));
				dto.setTitle(rs.getString("title"));
				dto.setWdate(rs.getString("wdate"));
				dto.setViewcnt(rs.getInt("viewcnt"));
				dto.setGrpno(rs.getInt("grpno"));
				dto.setIndent(rs.getInt("indent"));
				dto.setAnsnum(rs.getInt("ansnum"));

				list.add(dto);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBclose.close(rs, pstmt, con);
		}

		return list;
	}

	public boolean delete(int memono) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" delete from memo ");
		sql.append(" where memono = ? ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, memono);
			int cnt = pstmt.executeUpdate();
			if (cnt > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBclose.close(pstmt, con);
		}

		return flag;
	}

	public boolean update(MemoDTO dto) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" update memo ");
		sql.append(" set title = ?, ");
		sql.append(" content = ? ");
		sql.append(" where memono = ? ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getMemono());

			int cnt = pstmt.executeUpdate();
			if (cnt > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBclose.close(pstmt, con);
		}

		return flag;
	}

	public MemoDTO read(int memono) {
		MemoDTO dto = null;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" select memono, title, content, ");
		sql.append(" to_char(wdate,'yyyy-mm-dd')wdate, viewcnt");
		sql.append(" from memo ");
		sql.append(" where memono=? ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, memono);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new MemoDTO();
				dto.setMemono(rs.getInt("memono"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setWdate(rs.getString("wdate"));
				dto.setViewcnt(rs.getInt("viewcnt"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBclose.close(rs, pstmt, con);
		}

		return dto;
	}

	public boolean create(MemoDTO dto) {
		boolean flag = false;

		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
	
		StringBuffer sql = new StringBuffer();

		sql.append(" insert into memo(memono, title, content, wdate,grpno) ");
		sql.append(" values(memo_seq.nextval,?,?,sysdate, ");
		sql.append(" (select nvl(max(grpno),0)+1 as grpno from memo)) ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			int cnt = pstmt.executeUpdate();

			if (cnt > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBclose.close(pstmt, con);
		}

		return flag;
	}

	public void updateViewcnt(int memono) {
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" update memo ");
		sql.append(" set viewcnt = viewcnt + 1 ");
		sql.append(" where memono = ? ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, memono);

			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBclose.close(pstmt, con);
		}
	}
}
