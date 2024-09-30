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

@WebServlet(urlPatterns = {"/ProgramNote"})
public class ProgramNoteServ extends TopServlet{

    static Logger logger = LogManager.getLogger(ProgramNoteServ.class);

    /**
     * The main class method doGet.
     *
     * Create an html page for the Facility form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	doPost(req, res);
    }
    /**
     * The main class method doPost.
     *
     * Create an html page for the planning form.
     * @param req request input parameters
     * @param res reponse output parameters
     * 
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter out;

	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, action="", fromBrowse="";
	String id ="", program_id="";
		
	//
	// reinitialize to blank
	//
	String message = "", finalMessage="";

	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recreation</title>"); 
	boolean actionSet = false, success=true;
	User user = null;
	HttpSession	session = req.getSession(false);
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	ProgramNote cont = new ProgramNote(debug);
	Program prog = null;
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		cont.setId(value);
		id = value;
	    }
	    else if(name.equals("notes")){
		cont.setNotes(value);
	    }
	    else if(name.equals("fromBrowse")){
		fromBrowse = value;
	    }			
	    else if(name.equals("program_id")){
		cont.setProgram_id (value);
		program_id = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	//
       	if(action.equals("Delete")){
	    if(user.canDelete()){
		cont.doSelect();
		program_id = cont.getProgram_id();
		String back = cont.doDelete();
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
	else if(action.equals("Save")){
	    //
	    if(user.canEdit()){
		cont.setAdded_by(user.getId());
		String back = cont.doSave();
		if(back.isEmpty()){
		    id = cont.getId();
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
	else if(action.equals("Update")){
	    //
	    if(user.canEdit()){
		cont.setAdded_by(user.getId());
		String back = cont.doUpdate();
		if(back.equals("")){
		    id = cont.getId();
		    message = "Saved successfully";
		}
		else{
		    message += back ;
		    success = false;
		    cont.doSelect();
		    program_id = cont.getProgram_id();
		}
	    }
	    else{
		message = "You can not update ";
		success = false;
	    }						
	}	
	else if(!id.equals("")){
	    String back = cont.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    program_id = cont.getProgram_id();
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
	if(id.isEmpty()){
	    out.println("<h2>New Program Notes</h2>");
	}
	else{
	    out.println("<h2>Notes Id: "+id+"</h2>");
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
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	out.println("<table width=\"90%\" border>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table>");
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Program Notes </font></h3></td></tr>");
	if(!id.isEmpty()){
	    out.println("<tr><td align=\"right\"><b>Added by:</b></td><td align=\"left\">");
	    out.println(cont.getUser());
	    out.println("</td></tr>");
	    out.println("<tr><td align=\"right\"><b>Date & Time:</b></td><td align=\"left\">");
	    out.println(cont.getDate_time());
	    out.println("</td></tr>");
	}	
	out.println("<tr><td align=\"left\" colspan=\"2\"><b>Notes </b></td></tr>");	
	out.println("<tr><td align=\"left\" colspan=\"2\">");	
	out.println("<textarea name=\"notes\" rows=\"10\" cols=\"50\" >");
	out.println(cont.getNotes());
	out.println("</textarea></td</tr>");

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
	out.println("<br /> ");
	out.println("<a href=\""+url+"Program.do?id="+program_id+
			    "\"> Back to Program "+program_id+"</a>");
	out.println("<br />");
	Helper.writeWebFooter(out, url);
	//
	out.print("</center></body></html>");
	out.close();
    }

}





































