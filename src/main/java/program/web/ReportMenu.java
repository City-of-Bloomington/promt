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

@WebServlet(urlPatterns = {"/ReportMenu"})
public class ReportMenu extends TopServlet{

    String lastUpdate="";
    static Logger logger = LogManager.getLogger(ReportMenu.class);
    String allCategory = "";
    String allLead = "";
    String allArea = "";
    /**
     * Create an html page for the report menu form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String year="", season="";
	String allPrograms = "";
	Enumeration values = req.getParameterNames();
	String id="", area_id="", category_id="", lead_id="";
	String brstr = "",nraccount="", sortby="";
	String message="";
	String name, value;
	boolean success = true;
		
	LeadList leads = null;
	TypeList areas = null;
	TypeList locations = null;
	TypeList categories = null;				
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("year")){
		year = value;
	    }
	    else if(name.equals("season")){
		season = value;
	    }
	    else if(name.equals("id")){  // progam id
		id = value;
	    }
	    else if(name.equals("category_id")){   // by zoom
		category_id = value;
	    }
	    else if(name.equals("lead_id")){   // by zoom
		lead_id = value;
	    }
	    else if(name.equals("area_id")){   // by zoom
		area_id = value;
	    }
	    else if(name.equals("sortby")){   // by zoom
		sortby = value;
	    }
	    else if(name.equals("nraccount")){   // by zoom
		nraccount = value;
	    }
	}
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
	brstr =	"year="+year+
	    "&id="+id+
	    "&season="+season.replace(' ','+');
	//
	if(!year.equals("") && !season.equals("")){
	    allPrograms = "";
	    ProgramList pl = new ProgramList(debug);
	    pl.setYear(year);
	    pl.setSeason(season);
	    String back = pl.findAbraviatedList();
	    if(back.equals("")){
		Collections.sort(pl);
		for(Program pp:pl){
		    allPrograms += "<option value=\""+pp.getId()+"\">" + 
			pp.getTitle();
		}
	    }
	}		
	leads = new LeadList(debug);
	if(!season.equals(""))
	    leads.setSeason(season);
	if(!year.equals(""))
	    leads.setYear(year);
	String back = leads.find();
	if(!back.equals("")){
	    message += back;
	    success = false;
	}
	areas = new TypeList(debug, "areas");
	back = areas.find();
	if(!back.equals("")){
	    message += back;
	    success = false;
	}
	categories = new TypeList(debug, "categories");
	back = categories.find();
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
	out.println("<HTML><HEAD><TITLE>Programs Management Menu </TITLE>");
	out.println(" <script language=Javascript>");
	out.println("  function validateForm1() {              ");    
	out.println("  var x=\"\" ;                            ");
	out.println("   x = document.cForm.id.value;           ");
	out.println("   if(x.length == 0){                      ");
	out.println("   for(var i = 0; i < document.cForm.id.options.length; i++){ ");
	out.println("    if (document.cForm.id.options[i].selected){  "); 
	out.println("      x = document.cForm.id.options[i].value;  ");
	out.println(" document.mForm.id.value=x;          ");  
	out.println("         }                            "); 
	out.println("      }                               ");
	out.println("     }                               ");
	out.println("   for(var i = 0; i < document.cForm.category_id.options.length; i++){ ");
	out.println("    if (document.cForm.category_id.options[i].selected){  "); 
	out.println("      x = document.cForm.category_id.options[i].value;  ");
	out.println(" document.mForm.category_id.value=x;          ");  
	out.println("         }                                  "); 
	out.println("      }                                     "); 

	out.println("   for(var i = 0; i < document.cForm.lead_id.options.length; i++){ ");
	out.println("    if (document.cForm.lead_id.options[i].selected){  "); 
	out.println("      x = document.cForm.lead_id.options[i].value;  ");
	out.println(" document.mForm.lead_id.value=x;          ");  
	out.println("         }                            "); 
	out.println("      }                               "); 
	out.println("   for(var i = 0; i < document.cForm.area_id.options.length; i++){ ");
	out.println("    if (document.cForm.area_id.options[i].selected){  "); 
	out.println("      x = document.cForm.area_id.options[i].value;  ");
	out.println(" document.mForm.area_id.value=x;          ");  
	out.println("         }                            "); 
	out.println("      }                               "); 

	out.println("   return true;                 ");
	out.println("  }                             ");
	//
	out.println("  function validateForm(form) {           ");    
	out.println("  var x=\"\" ;                            ");
	out.println("  var i = document.cForm.id.options.selectedIndex; ");
	out.println("    if (i > -1){  "); 
	out.println("      x = document.cForm.id.options[i].value;    ");
	out.println("        form.id.value=x;                       ");  
	// out.println("     alert('id '+x);                            ");
	out.println("      }                                          "); 
	out.println(" var i = document.cForm.category_id.options.selectedIndex;");
	out.println("    if ( i > -1){  "); 
	out.println("      x = document.cForm.category_id.options[i].value; ");
	out.println("         form.category_id.value=x;                    ");  
	out.println("      }                                           "); 
	out.println("   var i = document.cForm.lead_id.options.selectedIndex; ");
	out.println("    if (i > -1){  "); 
	out.println("      x = document.cForm.lead_id.options[i].value;  ");
	out.println("         form.lead_id.value=x;                   ");  
	out.println("         }                            ");
	out.println("   var i = document.cForm.area_id.options.selectedIndex; ");
	out.println("    if (i > -1){  "); 
	out.println("      x = document.cForm.area_id.options[i].value;  ");
	out.println("       form.area_id.value=x;          ");  
	out.println("         }                            "); 
	out.println("    if (document.cForm.nraccount.value.length > 0){  ");
	out.println("      x = document.cForm.nraccount.value; ");
	out.println("      form.nraccount.value=x;   ");
	out.println("    }                           ");
	out.println("   var i=document.cForm.sortby.options.selectedIndex; ");
	out.println("    if (i > -1){  "); 
	out.println("      x = document.cForm.sortby.options[i].value;  ");
	out.println("          form.sortby.value=x;        ");  
	out.println("      }                               "); 
	out.println("   return true;                       ");
	out.println("  }                                   ");
	out.println(" </script>                            ");
	out.println("</head><body>");
	out.println("<center><br />");		
	out.println("<form name=\"cForm\" method=\"get\">");
	out.println("<h2>Reports Menu</h2>");
	out.println("<b>Reports for "+season+" - "+year);
	out.println("<br /><br />");
	out.println("<table border=\"1\" width=\"90%\">");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");			
	out.println("<table width=\"100%\">");
	out.println("<tr><td align=\"center\" colspan=\"2\">");
	out.println("<b>Additional Query Options</b> "+
		    "<br /><br />");
	out.println("<tr><td align=\"right\"><b>Area/Division</b></td><td align=\"left\">");
	out.println("<select name=\"area_id\">");
	out.println("<option value=\"\">All</option>");
	if(areas != null){
	    for(Type one:areas){
		String selected = "";
		if(one.getId().equals(area_id))
		    selected = "selected=\"selected\"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}				
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><b>Guide Heading:</b></td><td align=\"left\">");
	out.println("<select name=\"category_id\">");
	out.println("<option value=\"\">All</option>");
	if(categories != null){
	    for(Type one:categories){
		String selected = "";
		if(one.getId().equals(category_id))
		    selected = "selected=\"selected\"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	//
	out.println("<tr><td align=\"right\"><b>Program:</b></td>");
	out.print("<td align=\"left\">");
	out.println("<select name=\"id\">");
	out.println("<option selected value=\"\">All</option>");
	out.println(allPrograms);
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td align=right><b>Lead Programmer:"+
		    "</b></td>");
	out.print("<td align=\"left\">");		
	out.println("<select name=\"lead_id\">");
	out.println("<option value=\"\">All</option>");
	if(leads != null){
	    for(Type one:leads){
		String selected = "";
		if(one.getId().equals(lead_id))
		    selected = "selected=\"selected\"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><b>Non-Reverting Account:"+
		    "</b></td>");
	out.print("<td align=\"left\">");		
	out.println("<input type=\"text\" name=\"nraccount\" size=\"10\" "+
		    " value=\"\" maxlength=\"20\">");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><b>Sort by:</b></td>");
	out.print("<td align=\"left\">");		
	out.println("<select name=\"sortby\">");
	out.println("<option value=\"\"></option>");
	out.println("<option value=\"p.id\">ID</option>");
	out.println("<option value=\"p.startDate\">Date</option>");
	out.println("<option value=\"p.title\">Program Title</option>");
	out.println("<option value=\"s.code,p.code\">Program Code</option>");		
	out.println("</select></td></tr>");
	out.println("</table>");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	out.println("<br />");
	//
	out.println("<fieldset style=\"width:85%;background-color:#CDC9A3;\">");
	//
	// form 4
	out.println("<form name=\"mForm4\" method=\"post\" action=\""+
		    url + "ReportBrochure\" "+
		    "onSubmit=\"return validateForm(this)\">");
		
	out.println("<input type=\"hidden\" name=\"year\" value=\""+year+ 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\""+season+ 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");
	out.println("<tr><td align=\"left\"><li><b>Brochure:</b></li></td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=\"submit\" value=\"Click here\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	//
	//
	// form 6
	out.println("<form name=mForm6 method=post action=\""+
		    url + "MarketReport\" "+
		    "onsubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");		
	out.println("<tr><td align=\"left\"><li><b>Marketing:</li></b></td>");
	out.println("<td align=\"left\" width=\"50%\">");		
	out.println("<input type=\"submit\" value=\"Click here\" />");
	out.println("</td></tr>");
	out.println("</table>");		
	out.println("</form>");
	//
	// form 7
	out.println("<form name=\"mForm7\" method=\"post\" action=\""+
		    url + "ReportManager\" "+
		    "onSubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");				
	out.println("<tr><td align=\"left\"><li><b>Manager:</b></li></td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=\"submit\" value=\"Click here\"\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	//
	// form 3
	out.println("<form name=\"mForm3\" method=\"post\" action=\""+
		    url + "ReportSpon\" "+
		    "onSubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");
	out.println("<tr><td align=\"left\"><li><b>Sponsorships:</b></li></td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=submit value=\"Click here\"\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	//
	// form 8
	out.println("<form name=mForm8 method=post action="+
		    url + "ReportCodeNeed "+
		    "onSubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"unsetCode\" value=\"checked\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");
	out.println("<tr><td align=\"left\"><li><b>Code Needed Programs:</b></li></td><td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=\"submit\" value=\"Click here\"\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	//
	// form 9
	out.println("<form name=\"mForm9\" method=\"post\" action=\""+
		    url+"ReportCode\" "+
		    "onSubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"unsetCode\" value=\"checked\"  />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");
	out.println("<tr><td align=\"left\"><li><b>Programs Code Title List:</b></li></td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=\"submit\" value=\"Click here\"\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	//
	// This report will not work on the old data (before 2004)
	// therefor we will hide this options for request 
	// befor 2004/Fall Winter
	//
	boolean accept = false;
	int yy = 0;
	try{
	    yy =  Integer.parseInt(year);
	    if(yy>2004) accept = true;
	    else if(yy == 2004 && season.equals("Fall/Winter"))
		accept = true;
	}
	catch(Exception ex){}
	if(accept){
	    //
	    // form 9
	    out.println("<form name=mForm10 method=post action="+
			url + "DeadLineReport "+
			"onSubmit=\"return validateForm(this)\">");
	    out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
			"\" />");
	    out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
			"\" />");
	    out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
			"\" />");
	    out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
			"\" />");
	    out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
			"\" />");
	    out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
			"\" />");
	    out.println("<input type=\"hidden\" name=unsetCode value=checked "+ 
			"\" />");
	    out.println("<input type=\"hidden\" name=\"nraccount\" value=\""+nraccount + 
			"\" />");
	    out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
			"\" />");
	    out.println("<table width=\"75%\">");
	    out.println("<tr><td align=\"left\"><li><b>Registration Deadline Report</b></li></td>");
	    out.println("<td align=\"left\" width=\"50%\">");
	    out.println("<INPUT type=submit value=\"Click here\"\" />");
	    out.println("</td></tr>");
	    out.println("</table>");
	    out.println("</form>");
	}
	// form 10
	out.println("<form name=mForm2 method=post action="+
		    url + "ReportVol "+
		    "onSubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");
	out.println("<tr><td align=\"left\"><li><b>Volunteer</b></li></td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=\"submit\" value=\"Click here\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	//
	// form 11
	out.println("<form name=mForm11 method=post action="+
		    url + "ReportInclusive "+
		    "onSubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=unsetCode value=checked "+ 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");
	out.println("<tr><td align=\"left\"><li><b>Inclusion Recreation Report</b></li></td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=submit value=\"Click here\"\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	// form 5
	out.println("<form name=mForm5 method=post action="+
		    url + "FacilityTable "+
		    "onSubmit=\"return validateForm(this)\">");
	out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"season\" value=\"" + season + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + area_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + lead_id + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"nraccount\" value=\"" + nraccount + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"sortby\" value=\"" + sortby + 
		    "\" />");
	out.println("<table width=\"75%\">");
	out.println("<tr><td align=\"left\"><li><b>Facility</b></li></td>");
	out.println("<td align=\"left\" width=\"50%\">");
	out.println("<input type=\"submit\" value=\"Click here\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	out.println("</fieldset><br />");
	//
	out.println("</center></body></html>");
	out.close();

    }				   
    /**
     * The main class method.
     *
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */		  
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	doGet(req, res);
    }


}
