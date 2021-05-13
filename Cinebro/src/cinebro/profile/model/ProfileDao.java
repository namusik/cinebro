package cinebro.profile.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cinebro.common.model.SuperDao;
import cinebro.lists.model.FilmList;

public class ProfileDao extends SuperDao {

	public Profile selectMyinfo(String email) {
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		Profile bean = new Profile();
		
		String sql = " select * from members where email = ? " ;
		
		try {
			if(conn == null) {super.conn = super.getConnection() ; }
			pstmt = conn.prepareStatement(sql) ;
			pstmt.setString(1, email);
			rs = pstmt.executeQuery() ; 
			if(rs.next()) {
				bean = new Profile() ;
				
				bean.setPassword(rs.getString("password"));				
				bean.setEmail(rs.getString("email"));
				bean.setNickname(rs.getString("nickname"));
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) {rs.close();}
				if(pstmt != null) {pstmt.close();} 
				super.closeConnection(); 
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return bean ;
	}

	public int editNickname(Profile bean) {
		String sql = "update members set nickname = ? where email = ?";
		
		PreparedStatement pstmt = null ;
		int cnt = -99999 ;
		
		try {
			if( conn == null ){ super.conn = super.getConnection() ; }
			conn.setAutoCommit( false );
			pstmt = super.conn.prepareStatement(sql) ;
			
			pstmt.setString(1, bean.getNickname());
			pstmt.setString(2, bean.getEmail());
			
			cnt = pstmt.executeUpdate(); 
			conn.commit(); 
			
		} catch (Exception e) {
			SQLException err = (SQLException)e ;
			cnt = - err.getErrorCode() ;			
			e.printStackTrace();
			try {
				conn.rollback(); 
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} finally{
			try {
				if( pstmt != null ){ pstmt.close(); }
				super.closeConnection(); 
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return cnt ;
	}

	public List<Profile> selectMyfollowing(String email) {
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		
		String sql = " select m.email, m.nickname, m.password, m2.email, m2.nickname as following from members m inner join myfollowing mf on m.email = mf.my_email inner join members m2 on m2.email = mf.follow_email where mf.my_email = ? " ;
		
		List<Profile> lists = new ArrayList<Profile>();
		
		try {
			if( conn == null ){ super.conn = super.getConnection() ; }
			pstmt = super.conn.prepareStatement(sql) ;
			
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery() ;	
			
			while( rs.next() ){
				Profile bean = new Profile();
								
				bean.setEmail(rs.getString("emil"));
				bean.setNickname(rs.getString("nickname"));				
				bean.setPassword(rs.getString("password"));
				bean.setF_email(rs.getString("f_email"));
				bean.setF_nickname(rs.getString("f_nickname"));
				
				lists.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if( rs != null ){ rs.close(); }
				if( pstmt != null ){ pstmt.close(); }
				super.closeConnection(); 
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		
		return lists ;
	}
}