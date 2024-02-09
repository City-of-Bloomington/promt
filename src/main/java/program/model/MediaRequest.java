package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class MediaRequest extends CommonInc{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(MediaRequest.class);    
    String id="", program_id="", lead_id="", location_id="",
	facility_id="", 
	location_description="", content_specific="", notes="";
	; // request_tyes: Photography, Videography, Other
    String request_type[] = null;
    String other_type="", request_date="";
    Lead lead = null;
    Location location = null;
    public MediaRequest(boolean val){
	debug = val;
    }
    public MediaRequest(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    public MediaRequest(boolean val,
			String val2,
			String val3,
			String val4,
			String val5,
			String val6,
			String val7,
			String val8,
			String[] val9,
			String val10,
			String val11,
			String val12
	    ){
	setVals(val,val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12);

    }
    private void
	setVals(
		boolean val,
		String val2,
		String val3,
		String val4,
		String val5,
		String val6,
		String val7,
		String val8,
		String val9[],
		String val10,
		String val11,
		String val12
		){
	debug = val;
	setId(val2);
	setProgram_id(val3);
	setFacility_id(val4);
	setLead_id(val5);
	setLocation_id(val6);
	setLocationDescription(val7);
	setContentSepecific(val8);		
	setRequestType(val9);
	setOtherType(val10);
	setRequestDate(val11);
	setNotes(val12);
    }	

    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setProgram_id (String val){
	if(val != null)
	    program_id = val;
    }
    public void setFacility_id (String val){
	if(val != null)
	    facility_id = val;
    }    
    public void setLead_id (String val){
	if(val != null)
	    lead_id = val;
    }        
    public void setLocation_id(String val){
	if(val != null)
	   location_id = val;
    }
    public void setLocationDescription(String val){
	if(val != null)
	    location_description = val;
    }
    public void setContentSepecific(String val){
	if(val != null)
	    content_specific = val;
    }
    public void setRequestTypeStr(String val){
	if(val != null){
	    String [] arr = null;
	    if(val.indexOf(",") > -1){
		arr = val.split(",");
	    }
	    else{
		arr = new String[1];
	        arr[0] = val;
	    }
	    setRequestType(arr);
	}
    }
    public void setRequestType(String[] val){
	if(val != null)
	    request_type = val;
    }
    public void setOtherType(String val){
	if(val != null)
	    other_type = val;
    }
    public void setRequestDate(String val){
	if(val != null)
	    request_date = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }    
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getProgram_id(){
	return program_id;
    }
    public String getFacility_id(){
	return facility_id;
    }    
    public String getLead_id(){
	return lead_id;
    }
    public String getLocation_id(){
	return location_id;
    }    
    public String getLocationDescription(){
	return location_description;
    }	
    public String getContentSpecific(){
	return content_specific;
    }
    public String[] getRequestType(){
	return request_type;
    }
    public String getRequestTypeStr(){
	String str = "";
	if(request_type != null){
	    for(String one:request_type){
		if(!one.isEmpty()) one += ", ";
		str += one;
	    }
	}
	return str;
    }
    public String getOtherType(){
	return other_type;
    }
    public String getRequestDate(){
	return request_date;
    }
    public String getNotes(){
	return notes;
    }    
    public Lead getLead(){
	if(lead == null && !lead_id.isEmpty()){
	    Lead one = new Lead(debug, lead_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		lead = one;
	    }
	}
	return lead;
    }
    public Location getLocation(){
	if(location == null && !location_id.isEmpty()){
	    Location one = new Location(debug, location_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		location = one;
	    }
	}
	return location;
    }
    public boolean hasLocation(){
	return !location_id.isEmpty();
    }
    public boolean hasProgram(){
	return !program_id.isEmpty();
    }
    public boolean hasFacility(){
	return !facility_id.isEmpty();
    }
    public boolean hasLead(){
	return !lead_id.isEmpty();
    }
    public boolean hasLocationDescription(){
	return !location_description.isEmpty();
    }
    public boolean hasContentSpecific(){
	return !content_specific.isEmpty();
    }
    public boolean hasOtherType(){
	return !other_type.isEmpty();
    }
    public boolean hasNotes(){
	return !notes.isEmpty();
    }    
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof MediaRequest){
	    match = id.equals(((MediaRequest)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int code = 0;
	try{
	    code = Integer.parseInt(id);
	}catch(Exception ex){};
	return code;
    }	
    public String toString(){
	return id;
    }
    //
    public String doDelete(){

	String back = "";
	String qq = "delete from media_requests where id=?";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}			
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message = " Error deleting record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSelect(){
	//
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "select id,program_id,facility_id,lead_id,location_id,location_description,content_specific,request_type,other_type,date_format(request_date,'%m%d/%Y'),notes "+
	    " from media_requests where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		String str  = rs.getString(8);
		String [] arr = null;
		if(str != null){
		    if(str.indexOf(",") > -1){
			arr = str.split(",");
		    }
		    else{
			arr = new String[1];
			arr[0] = str;
		    }
		}
		setVals(debug,
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
	    }
	    else{
		message = "No match found";
	    }
	}
	catch(Exception ex){
	    message += " Error retreiving the record " +ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doUpdate(){

	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "update media_requests set program_id=?,facility_id=?,lead_id=?,location_id=?,location_description=?,content_specific=?,request_type=?,other_type=?,notes=? "+
	    " where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}		
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(program_id.equals(""))
		pstmt.setString(1,null);
	    else
		pstmt.setString(1,program_id);
	    if(facility_id.equals(""))
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,facility_id);
	    if(lead_id.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,lead_id);	    
	    if(location_id.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,location_id);
	    if(location_description.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,location_description);			
	    if(content_specific.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,content_specific);
	    if(request_type != null){
		String r_types = "";
		for(String str:request_type){
		    if(!r_types.isEmpty())  r_types +=",";
		    r_types += str;
		}
		pstmt.setString(7,r_types);
	    }
	    else
		pstmt.setString(7,null);
	    if(other_type.equals(""))
		pstmt.setString(8,null);
	    else
		pstmt.setString(8,other_type);
	    if(notes.equals(""))
		pstmt.setString(9,null);
	    else
		pstmt.setString(9,notes);	    
	    pstmt.setString(10,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message += " Error updating the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSave(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String back = "";
	String qq = "insert into media_requests values (0,?,?,?,?, ?,?,?,?,now()),?";
	request_date = Helper.getToday2();
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}				
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(program_id.equals(""))
		pstmt.setString(1,null);
	    else
		pstmt.setString(1,program_id);
	    if(facility_id.equals(""))
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,facility_id);
	    if(lead_id.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,lead_id);	    
	    if(location_id.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,location_id);
	    if(location_description.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,location_description);			
	    if(content_specific.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,content_specific);
	    if(request_type == null)
		pstmt.setString(7,null);
	    else{
		String r_types = "";
		for(String str:request_type){
		    if(!r_types.isEmpty())  r_types +=",";
		    r_types += str;
		}
		pstmt.setString(7,r_types);
	    }
	    if(other_type.equals(""))
		pstmt.setString(8,null);
	    else
		pstmt.setString(8,other_type);
	    if(notes.equals(""))
		pstmt.setString(9,null);
	    else
		pstmt.setString(9,notes);	    
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    message = " Error adding the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }

    /**
       create table media_requests (
       id int auto_increment primary key,
       program_id int,
       facility_id int,
       lead_id int,
       location_id int,
       location_description varchar(1024),
       content_specific varchar(1024),
       request_type varchar(1024),
       other_type varchar(512),
       request_date date,
       notes varchar(1024),
       foreign key(program_id) references programs(id),
       foreign key(lead_id) references leads(id),
       foreign key(location_id) references locations(id),
       foreign key(facility_id) references facilities(id)
       )engine=InnoDB;
       
     */
}





































