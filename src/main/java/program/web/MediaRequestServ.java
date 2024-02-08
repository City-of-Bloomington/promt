package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/MediaRequest"})
public class MediaRequestServ extends TopServlet{

    static Logger logger = LogManager.getLogger(MediaRequestServ.class);


    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	doPost(req, res);
    }

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter out;

	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, action="", fromBrowse="";
	String id ="";
		
	//
	// reinitialize to blank
	//
	String message = "", finalMessage="";

	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recreation</title>"); 
	boolean actionSet = false, success=true;
	User user = null;
	HttpSession	session = req.getSession(false);
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	LeadList leads = null;
	TypeList locations = null;
	MediaRequest request = new MediaRequest(debug);
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		if(value != null){
		    request.setId(value);
		    id = value;
		}
	    }
	    else if(name.equals("program_id")){
		request.setProgram_id(value);
	    }			
	    else if(name.equals("lead_id")){
		request.setLead_id(value);
	    }
	    else if(name.equals("facility_id")){
		request.setFacility_id(value);
	    }	    
	    else if(name.equals("location_id")){
		request.setLocation_id(value);
	    }
	    else if(name.equals("locationDescription")){
		request.setLocationDescription(value);		
	    }			
	    else if(name.equals("contentSepecific")){
		request.setContentSepecific(value);
	    }			
	    else if(name.equals("requestType")){
		request.setRequestType(value);
	    }
	    else if(name.equals("otherType")){
		request.setOtherType(value);
	    }
	    else if(name.equals("requestDate")){
		request.setRequestDate(value);
	    }
	    else if(name.equals("fromBrowse")){
		if(value != null)
		    fromBrowse = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if(id.equals("")){ // for new records
	    fromBrowse="y";
	}
	//
       	if(action.equals("Delete")){
	    if(user.canDelete()){
		String back = request.doDelete();
		if(back.equals("")){
		    id="";
		}
		else{
		    message += back;
		    success = false;
		}
	    }
	    else{
		message = "You can not delete ";
		success = false;
	    }
	}
	else if(action.equals("Update")){
	    if(user.canEdit()){
		String back = request.doUpdate();
		if(back.equals("")){
		    message = "Record updated successfully";
		}
		else{
		    message += back ;
		    success = false;
		}
	    }
	    else{
		message = "You can not update ";
		success = false;
	    }			
	}
	else if(action.equals("Save")){
	    //
	    if(user.canEdit()){
		String back = request.doSave();
		if(back.equals("")){
		    id = request.getId();
		    message = "Saved successfully";
		}
		else{
		    message += back ;
		    success = false;
		}
	    }
	    else{
		message = "You can not update ";
		success = false;
	    }						
	}
	else if(!id.equals("")){
	    String back = request.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    locations = new TypeList(debug, "locations");
	    back = locations.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }	    
	}
	//
	// This script validates textareas and facility
	//
	Helper.writeWebCss(out, url);
	out.println("<script>");
	out.println("    function validateTextarea(ss){                   ");
	out.println("      if (ss.value.length > 10000){                  ");
	out.println("       alert(\"Maximum Character number is 10000\"); ");
	out.println("        ss.value = ss.value.substring(0,9999);       ");
	out.println("                    }                                ");
	out.println("                }                                    ");
	out.println("     function validateForm2() {                      "); 
	out.println("				                          ");
	out.println(" answer = window.confirm (\"Delete This Record ?\"); ");  
	out.println("	if (answer == true)	                          ");
	out.println("	     return true;                                 ");
	out.println("	 return false;		                          ");
	out.println("	 }		                                  ");
	out.println("  function checkTextLen(ss,len) {                    ");
	out.println("   if (ss.value.length > len) {                      ");
	out.println("     alert(\"Maximum number of characters is \"+len); ");
	out.println("        ss.value = ss.value.substring(0,len);       ");
	out.println("                    }                               ");
	out.println("                }                                   ");
	out.println("  function validateForm(){                          "); 
	out.println("	     return true;	           ");
	out.println(" 	 }                                 ");
	out.println("	</script>                          ");   
	out.println("</head><body>");
	//
	out.println("<center>");
	Helper.writeTopMenu(out, url);		
	if(id.equals("")){
	    out.println("<h2>New Media Request</h2>");
	}
	else{
	    out.println("<h2>Edit Media Request "+id+"</h2>");
	}
	if(!message.equals("")){
	    if(success)
		out.println("<font color=\"green\">"+message+"</font>");
	    else
		out.println("<font color=\"red\">"+message+"</font>");
	    out.println("<br>");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"return "+
		    "validateForm()\" id=\"form_id\" >");
	if(!fromBrowse.equals("")){
	    out.println("<input type=\"hidden\" name=\"fromBrowse\" value=\"y\" />");
	}		
	if(!id.isEmpty()){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	    out.println("<input type=\"hidden\" name=\"requestDate\" value=\""+request.getRequestDate()+"\" />");
	}
	out.println("<input type=\"hidden\" name=\"program_id\" value=\""+request.getProgram_id()+"\" />");
	out.println("<input type=\"hidden\" name=\"facility_id\" value=\""+request.getFacility_id()+"\" />");	    
	// Plan
	out.println("<table width=\"90%\" border>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table>");
	if(!id.isEmpty()){
	    out.println("<tr><td align=\"right\"><b>Request Date: </b></td><td align=\"left\">");
	    out.println(request.getRequestDate());
	    out.println("</td></tr>");
	}
	if(!request.getProgram_id().isEmpty()){
	    out.println("<tr><td align=\"right\"><b>Program: </b></td><td align=\"left\">");
	    
	    out.println("<a href=\""+url+"Program?id="+request.getProgram_id()+"\"> "+request.getProgram_id()+"</a>");
	    out.println("</td></tr>");
	}
	if(!request.getFacility_id().isEmpty()){
	    out.println("<tr><td align=\"right\"><b>Facility: </b></td><td align=\"left\">");
	    
	    out.println("<a href=\""+url+"Facility?id="+request.getFacility_id()+"\">"+request.getFacility_id()+"</a>");
	    out.println("</td></tr>");
	}	
	out.println("<tr><td align=\"right\"><b>Lead: </b></td><td align=\"left\">");
	out.println("<select name=\"lead_id\">");
	out.println("<option value=\"\">Pick a Lead</option>");	
	if(leads != null){
	    for(Lead one:leads){
		String selected = "";
		if(one.getId().equals(request.getLead_id())){
		    selected = "selected=\"selected\"";
		}
		else if(!one.isActive()) continue;
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}	
	out.println("</select>");
	out.println("</td></tr>");	
	out.println("<tr><td align=\"right\"><b>Location:</b></td><td align=\"left\">");
	out.println("<select name=\"location_id\">");
	out.println("<option value=\"\">Pick a Location</option>");		
	if(locations != null){
	    for(Type one:locations){
		String selected = "";
		if(one.getId().equals(request.getLocation_id())){
		    selected = "selected=\"selected\"";
		}
		else if(!one.isActive()) continue;
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\"><b>Location Description:</b></td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\">");
	out.println("<textarea name=\"locationDescription\" row=\"5\" cols=\"50\" wrap=\"wrap\">");
	out.println(request.getLocationDescription());
	out.println("</textarea></td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\"><b>Content Specific</b></td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\">");
	out.println("<textarea name=\"contentSepecific\" row=\"5\" cols=\"50\" wrap=\"wrap\">");
	out.println(request.getContentSepecific());
	out.println("</textarea></td></tr>");
	out.println("</td></tr>");	
	out.println("<tr><td align=\"right\"><b>Request Type:</b></td><td align=\"left\">");	
	String requst_types[] = {"Photography","Videography","Other"};
	for(String type:requst_types){
	    String selected = "";
	    if(type.equals(request.getRequestType())){
		selected="selected=\"selected\"";
	    }
	    out.println("<input type\"radio\" name=\"requestType\" value=\""+type+"\" "+selected+"/>");
	}
	out.println("</td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\"><b>Other Type</b></td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\">");
	out.println("<textarea name=\"otherType\" row=\"5\" cols=\"50\" wrap=\"wrap\">");
	out.println(request.getOtherType());
	out.println("</textarea></td></tr>");
	out.println("</td></tr>");	
	out.println("</table></td></tr>");
	//
	out.println("<tr><td><table width=100%>");
	out.println("<tr>");
	if(!id.equals("")){
	    //
	    // if no program yet (it is new plan)
	    // can be duplicated only when a program is linked to it
	    //
	    out.println("<td valign=top align=right>");				
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Update\" /></td>");
	    if(user.canDelete()){
		out.println("<td valign=\"top\" align=\"right\">");				
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onSubmit=\"return validateForm2()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
		out.println("</td><td valign=\"top\" align=\"right\">");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Delete\">&nbsp;");
		out.println("</form></td>");
	    }
	}
	else { // delete startNew
	    out.println("<td valign=\"top\" align=\"right\">");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Save\" /></td>"+
			    "<td align=\"right\" valign=\"top\">" );
	    }
	    out.println("</form></td>");
	}
	out.println("</tr>");
	out.println("</table>");
	out.println("</td></tr></table>");
	out.println("<br />");
	if(fromBrowse.equals("")){
	    out.println("<a href=javascript:window.close();>Close</a>");
	}
	Helper.writeWebFooter(out, url);
	//
	out.print("</center></body></html>");
	out.close();
    }

}





































