package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class AnnounceList extends ArrayList<Type>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(AnnounceList.class);
    String market_id = "";
		
    List<String> errors = null;
    String message = "";
	
    public AnnounceList(boolean val){
	debug = val;
    }
    public AnnounceList(boolean val, String val2){
	debug = val;
	setMarket_id(val2);
    }
    public void setMarket_id(String val){
	if(val != null)
	    market_id = val;
    }
    public String getMessage(){
	return message;
    }
    public boolean hasMessage(){
	return !message.equals("");
    }
    public boolean hasErrors(){
	return (errors != null && errors.size() > 0);
    }
    public List<String> getErrors(){
	return errors;
    }
    void addError(String val){
	if(errors == null)
	    errors = new ArrayList<String>();
	errors.add(val);
    }
	
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select a.id,a.name,a.active from market_announces a ";
	String qw = "";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!market_id.equals("")){
		qq += ", market_announce_details d where d.an_id = a.id and d.market_id=? ";  
	    }
	    qq += " order by a.name ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!market_id.equals("")){
		pstmt.setString(1, market_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Type one =
		    new Type(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3),
			     "market_announces" // for table name
			     );
		this.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
}






















































