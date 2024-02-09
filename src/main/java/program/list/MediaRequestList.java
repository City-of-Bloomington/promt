package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;
/**
 *
 */

public class MediaRequestList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(MediaRequestList.class);
    //	
    String program_id="", id="", lead_id="", facility_id="";
    List<MediaRequest> requests = null;
    List<String> errors = null;
    String message = "";
	
    public MediaRequestList(boolean val){
	debug = val;
    }
    public MediaRequestList(boolean val, String val2){
	debug = val;
	setProgram_id(val2);
    }
    public void setProgram_id(String val){
	if(val != null)
	    program_id = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
    public void setFacility_id(String val){
	if(val != null)
	    facility_id = val;
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
    public List<MediaRequest> getRequests(){
	return requests;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select id,program_id,facility_id,lead_id,location_id,location_description,content_specific,request_type,other_type,date_format(request_date,'%m/%d/%Y'),notes from media_requests ";	
	String qw = "", qf="";
	if(!id.isEmpty()){
	    if(!qw.equals("")) qw += " and ";
	    qw += " id = ? ";
	}			
	else if(!program_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " program_id = ? ";
	}
	else if(!facility_id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " facility_id = ? ";
	}
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!qw.isEmpty()){
		qq += " where "+qw;
	    }
	    if(debug){
		logger.debug(qq);
	    }
			
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!id.equals("")){
		pstmt.setString(j++, id);
	    }
	    else if(!program_id.equals("")){
		pstmt.setString(j++, program_id);
	    }
	    else if(!facility_id.equals("")){
		pstmt.setString(j++, facility_id);
	    }
	    	    
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str  = rs.getString(8);
		String [] arr = null;
		if(str.indexOf(",") > -1){
		    arr = str.split(",");
		}
		else{
		    arr = new String[1];
		    arr[0] = str;
		}
		MediaRequest one =
		    new MediaRequest(debug,
				     rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5),
				     rs.getString(6),
				     rs.getString(7),
				     arr,
				     rs.getString(9),
				     rs.getString(10),
				     rs.getString(11)
				     );
		if(requests == null)
		    requests = new ArrayList<>();
		if(!requests.contains(one))
		    requests.add(one);
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






















































