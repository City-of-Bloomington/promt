package program.web;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/DuplicatePlan.do","/DuplicatePlan"})
public class DuplicatePlan extends TopServlet{

    String allCategory = "";
    String allLead = "";
    String allArea = "";
    static Logger logger = LogManager.getLogger(DuplicatePlan.class);
    /**
     * The main class method.
     *
     * Create an html page for the program form.
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
     * The main class method.
     *
     * Create an html page for the program form.
     * It also handles all the operations related to adding/updaing/delete
     * or just viewing a record.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	final int baseNumber = 1;
	String message = "";
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String id = "", action="";
	Enumeration values = req.getParameterNames();
	String name, value;
	String lead_id ="", season="", year="",id_dup=""; 
	boolean success = true;
	HttpSession session = null;
	session = req.getSession(false);
	User user = null;
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	if(user != null){
	    control = new Control(debug, user);
	}
	Plan pp = new Plan(debug);		
	String [] vals;
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){
		pp.setId(value);
		id = value;
	    }
	    else if(name.equals("lead_id")){
		lead_id = value;
	    }
	    else if(name.equals("season")){
		season = value;
	    }
	    else if(name.equals("year")){
		year = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	String back = pp.doSelect();
	if(!back.equals("")){
	    message = back;
	    success = false;
	}
	LeadList leads = new LeadList(debug);
	back = leads.find();
	if(!back.equals("")){
	    message += back;
	    success = false;
	}		
	if(action.startsWith("Dup") && user.canEdit()){
	    pp.setYear_season(year+" "+season);
	    pp.setYear(year);
	    pp.setLead_id(lead_id);
	    pp.setSeason(season);
	    back = pp.doDuplicate();
	    if(back.equals("")){
		id_dup = pp.getId();
		String msg="Plan+duplicated+successfully";
		String str = url+"PrePlan.do?action=zoom&id="+id_dup+"&msg="+msg;
		res.sendRedirect(str);
		return;				
	    }
	    else{
		message += back;
		success = false;
	    }
	}
	out.println("<html>");
	out.println("<head><title>Promt </title>");
	Helper.writeWebCss(out, url);	
	//
	// This script validate 
	//
       	out.println(" <script language=Javascript>");
	out.println(" function showStatus(){                   ");
	// display it in the status line 
	out.println("    }                                     ");
	out.println("  function validateInteger(x) {           ");            
	out.println("	if((x >= 0 ) && (x <= 9)){             ");
	out.println("	            return true;               ");
	out.println(" 	        }                              ");
	out.println("	       return false;	               ");
	out.println(" 	   }                                   ");
	out.println("  function validateNumber(x){             ");            
	out.println("   if(x.length > 0 ){                     ");
	out.println("     for(i=0; i<x.length;++i){            ");
	out.println("     if(!validateInteger(x.charAt(i))){   ");
	out.println("      return false;                       ");
	out.println("     }}}                                  ");
	out.println("      return true;                        ");
	out.println("     }                                    ");
	out.println("  function validateDate(x){               ");            
	out.println("   if(!(x.length == 5) &&                 ");
	out.println("    !(x.length == 10)) return false;      ");
	out.println("   if(x.length == 5){                     ");
	out.println("    if(!validateInteger(x.charAt(0)) ||   ");
	out.println("    !validateInteger(x.charAt(1)) ||      "); 
	out.println("    !(x.charAt(2) == \"/\")       ||      ");
	out.println("    !validateInteger(x.charAt(3)) ||      "); 
	out.println("    !validateInteger(x.charAt(4))){       "); 
	out.println("	     return false;}		       ");
	out.println(" 	 } else{                               ");
	out.println("    if(!validateInteger(x.charAt(0)) ||   ");
	out.println("    !validateInteger(x.charAt(1)) ||      "); 
	out.println("    !(x.charAt(2) == \"/\")       ||      ");
	out.println("    !validateInteger(x.charAt(3)) ||      "); 
	out.println("    !validateInteger(x.charAt(4)) ||      "); 
	out.println("    !(x.charAt(5) == \"/\")       ||      ");
	out.println("    !validateInteger(x.charAt(6)) ||      "); 
	out.println("    !validateInteger(x.charAt(7)) ||      "); 
	out.println("    !validateInteger(x.charAt(8)) ||      "); 
	out.println("    !validateInteger(x.charAt(9))){       "); 
	out.println("	     return false;}		       ");
	out.println("	   }		                       ");
	out.println("    return true;                          ");
	out.println("	}	                               ");
	out.println("  function validateString(x){             ");            
	out.println("     if((x.value.length > 0)){            "); 
	out.println("       var eq = 0;	                       ");
	out.println("    for(t = 0; t < x.value.length; t++){  ");
	out.println("    if (x.value.substring(t,t+1) != \" \") eq = 1;	");
	out.println("    	       }                       ");
	out.println("     if (eq == 0) {	               ");
	out.println("	      return false;		       ");
	out.println("            }                             ");
	out.println("	     return true;		       ");
	out.println("         }                                ");  
	out.println("	     return false;		       ");
	out.println("      }                                   ");  
	out.println(" function checkSelection(element){        ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){     "); 
	out.println("      if(i > 0){                          ");
	out.println("         return true;                     ");
	out.println("         }                                ");
	out.println("       }                                  ");
	out.println("    return false;                         ");
	out.println("   }                                      ");
	out.println("  function validateForm() {               ");    
	out.println(" if(!checkSelection(document.myForm.pyear)){ ");
	out.println("  alert(\"Need to select a year for the new program\")");;
	out.println("   document.myForm.pyear.focus();         ");
	out.println("  return false;                           ");
	out.println("    }	                               ");
      	out.println(" if(!checkSelection(document.myForm.season)){ ");
	out.println("  alert(\"Need to select a season for the new "+
		    "program\")");
	out.println("   document.myForm.season.focus();        ");
	out.println("   return false;                          ");            
	out.println("    }	                               ");
	out.println("   return true;                           ");
	out.println("  }	                               ");
	out.println(" </script>                                ");
	out.println("</head>   ");	
	out.println("<body onLoad=\"showStatus();\">    ");
	Helper.writeTopMenu(out, url);		
	if(!success){
	    out.println("<p><font color=red>"+message+"</font></p>");
	}
	out.println("<h2><center>Duplicate Plan</h2>");
	out.println("<form name=myForm method=post "+
		    "onSubmit=\" return validateForm()\">");
	//
	if(!id.equals("")) 
	    out.println("<input type=hidden name=id value=" + id + " />");
	//
	// Title season year
	out.println("<table border width=\"60%\"><tr><td><table>");
	out.println("<tr><td colspan=\"2\">");
	out.println("<font color=green>To make a similar copy of this "+
		    "plan<br>"+
		    "Select the new year, season and lead for the new plan "+
		    "<br> and then click on Duplicate "+
		    "<br>You will get a message about the duplication<br>"+
		    "process is successful you will be forwarded to the pre plan page "+
		    "and newly duplicated plan where you can make<br>"+
		    " additional changes.</font>");
	out.println("</td></tr>");
	out.println("<tr><td align=right><b>Plan ID:");
	out.println("</b></td><td><left>"+id);
	out.println("</td></tr>");
	out.println("<tr><td align=right>");
	out.println("<b>Season:</b></td><td>");
	out.println("<select name=\"season\">");
	out.println("<option value=\"\"></option>");
	out.println(Helper.allSeasons);		
	out.println("</select>");
	out.println("<b>Year:</b>");
	out.println("<select name=\"year\">");
	int[] years = Helper.getFutureYears();
	for(int yy:years){
	    out.println("<option>"+yy+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=right><b>Lead:");
	out.println("</b></td><td><left>");
	out.println("<select name=\"lead_id\">");
	out.println("<option value=\"\">Pick one</option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = "";
		if(one.getId().equals(pp.getLead_id())){
		    selected = "selected=\"selected\"";
		}
		else if(!one.isActive()) continue;
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</left></td></tr>");				
	out.println("<tr><td colspan=2 align=right><input type=submit "+
		    "name=action value=Duplicate>&nbsp;&nbsp;"+
		    "&nbsp;&nbsp;&nbsp;" +
		    "&nbsp;&nbsp;<input type=reset value=Clear>"+
		    "</td></tr>"); 
	out.println("</form>");
	out.println("</table></td></tr></table>");
	out.println("<HR>");
	out.println("<HR>");
	out.println("</center>");
	out.print("</body></html>");
	out.flush();
	out.close();

    }

}























































