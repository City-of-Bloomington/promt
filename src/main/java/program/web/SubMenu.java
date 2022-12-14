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

// Sub menu for calendar, report, code, etc

@WebServlet(urlPatterns = {"/SubMenu"}, loadOnStartup = 1)
public class SubMenu extends TopServlet{

    static Logger logger = LogManager.getLogger(SubMenu.class);
    String allCategory = "";
    String allArea = "";
    String allLocation="";
    String allLead = "";
    String Months[] = {"","Jan","Feb","March","April","May","June","July",
	"Aug","Sept","Oct","Nov","Dec"};
    /**
     * Create an html page for the main menu form.
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
	String pyear="", season="";
	PrintWriter os;
	boolean success = true;
	Enumeration values = req.getParameterNames();
	String id="", title="", message="";
	String allPrograms="";
	String name, value, year="", choice="";
	String allMonths = "", category_id="";
	for(int i=0; i<Months.length;++i){
	    allMonths += "<option value="+i+">"+Months[i]+"\n";
	}
	int month=0;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("year")){
		year = value;
	    }
	    else if(name.equals("season")){
		season = value;
	    }
	    else if(name.equals("choice")){
		choice = value; // report, code, calendar
	    }
	    else if(name.equals("title")){
		title = value;
	    }
	    else if(name.equals("category_id")){
		category_id = value;
	    }
	    else if(name.equals("id")){  // progam id
		id = value;
	    }
	    else if(name.equals("year")){  // for calendar 
		year = value;
	    }
	    else if(name.equals("month")){  // for calendar
		if(!value.equals("")){
		    month = Integer.parseInt(value);
		}
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
	TypeList leads = null;
	TypeList areas = null;
	TypeList locations = null;
	TypeList categories = null;
	if(true){
	    leads = new TypeList(debug, "leads");
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
	}		
	//
	// in case year and season are set but not id
	if(!year.equals("") && !season.equals("") && id.equals("")){
	    allPrograms = "";
	    ProgramList pl = new ProgramList(debug);
	    pl.setYear(year);
	    pl.setSeason(season);
	    String back = pl.findAbraviatedList();
	    if(back.equals("")){
		for(Program pp:pl){
		    allPrograms += "<option value=\""+pp.getId()+"\">" + 
			pp.getTitle();
		}
	    }
	}
	if(choice.equals("report")) title = "Reports";
	else if(choice.equals("code"))title = "Code Entry";
	else if(choice.equals("calendar")) title="Calendar";
	else if(choice.equals("toPublish")) title="Web Publishing Program Selection";
	else if(choice.equals("unPublish")) title="Unpublishing Program Selection";								
	int years[] = Helper.getPrevYears();
	//
	// check for the user
	//
	out.println("<html><head><title>Promt Menu </title>");
	Helper.writeWebCss(out, url);
	out.println(" <script>");
	out.println("  function validateForm(){            ");    
	out.println("  var x=\"\" ;                        ");
	out.println("   x = document.myForm.year.options[document.myForm.year.options.selectedIndex].text; ");
	out.println("  if(x == \"\"){                      ");
	out.println("   alert(\"No year selected yet\");   ");
	out.println("   return false;                      ");
	out.println("  }                                   ");
	out.println("  x=\"\";                             ");
	out.println("      x = document.myForm.season.options[document.myForm.season.options.selectedIndex].text;  ");
	out.println("  if(x == \"\"){                      ");
	out.println("   alert(\"No season selected yet\"); ");
	out.println("   return false;                      ");
	out.println("  }                                   ");
	out.println("   return true;                       ");
	out.println("  }                                   ");
	out.println(" </script>                            ");
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);
	out.println("<h3> "+title+" </h3>");
	out.println("<table border=\"1\">");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");				
	if(choice.equals("report")){
	    //
	    // vol table
	    out.println("<table border=\"0\"><tr><td align=\"center\">");
	    out.println("</td></tr><tr><td align=center>");
	    out.println("Select the year and season then click on Next");

	    out.println("<form name=\"myForm\" action=\""+url+"ReportMenu\" "+
			"onSubmit=\"return validateForm();\">");
	    out.println("<input type=\"hidden\" name=\"choice\" value=\""+choice + "\" />");
	    if(year.equals("") && season.equals("")){
		out.println("<tr><td align=\"center\"><strong>Year: </strong>");
		out.println("<select name=\"year\">");
		out.println("<option value=\"\">Pick Year</option>");
		for(int yy:years){
		    out.println("<option>"+yy+"</option>");
		}				
		out.println("</select>");
		out.println("&nbsp;&nbsp;<strong>Season: </strong>");
		out.println("<select name=\"season\">");
		out.println("<option value=\"\">Pick Season</option>\n");
		out.println(Helper.allSeasons);
		out.println("</select></td></tr>");
	    }
	    out.println("<tr><td align=\"right\"> ");
	    out.println("<input type=\"submit\" value=\"Next\" />");
	    out.println("</td></tr>");
	    out.println("</form>");
	    out.println("</td></tr></table>");
	    out.println("</td></tr>"); // end of middle
	    //
	}
	else if(choice.equals("toPublish")){
	    List<WebPublish> publishes = null;
	    WebPublishList wpl = new WebPublishList(debug);
	    String back = wpl.find();
	    if(back.equals("")){
		publishes = wpl.getPublishes();
	    }						
	    //
	    // vol table
	    out.println("<table border=\"0\"><tr><td align=\"center\">");
	    out.println("</td></tr><tr><td align=\"center\">");
	    out.println("Select the year and season then click on Next");
	    out.println("<form name=\"myForm\" action=\""+url+"ToPublish.do\" "+
			"onSubmit=\"return validateForm()\">");
	    out.println("<input type=\"hidden\" name=\"choice\" value=\"" + 
			choice + "\" />");
	    out.println("<tr><td align=\"center\"><strong>Year: </strong>");
	    out.println("<select name=\"year\">");
	    out.println("<option value=\"\">Pick Year</option>");
	    for(int yy:years){
		out.println("<option>"+yy+"</option>");
	    }				
	    out.println("</select>");
	    out.println("&nbsp;&nbsp;<strong>Season: </strong>");
	    out.println("<select name=\"season\">");
	    out.println("<option value=\"\">Pick Season</option>\n");
	    out.println(Helper.allSeasons);
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=\"right\"> ");
	    out.println("<input type=\"submit\" value=\"Next\" />");
	    out.println("</td></tr>");
	    out.println("</form>");
	    out.println("</td></tr></table>");
	    out.println("</td></tr>"); // end of middle
	    if(publishes != null && publishes.size() > 0){
		out.println("<tr><td>");
		out.println("<table width=\"80%\"><caption>Most recent Publishing Approvals</caption>");
		out.println("<tr><td>ID</td><td>Date</td><td>Approved by</td></tr>");
		for(WebPublish one:publishes){
		    out.println("<tr><td><a href=\""+url+"UnPublish.do?id="+one.getId()+"\">"+one.getId()+"</a></td>");
		    out.println("<td>"+one.getDate()+"</td>");
		    out.println("<td>"+one.getUser()+"</td></tr>");
		}
		out.println("</table>");
		out.println("</td></tr>");
	    }						
	    //
	}
	else if(choice.equals("unPublish")){
	    //
	    // vol table
	    out.println("<table border=\"0\"><tr><td align=\"center\">");
	    out.println("</td></tr><tr><td align=\"center\">");
	    out.println("Select the year and season then click on Next");
	    out.println("<form name=\"myForm\" action=\""+url+"UnPublish.do\" "+
			"onSubmit=\"return validateForm()\">");
	    out.println("<input type=\"hidden\" name=\"choice\" value=\"" + 
			choice + "\" />");
	    out.println("<tr><td align=\"center\"><strong>Year: </strong>");
	    out.println("<select name=\"year\">");
	    out.println("<option value=\"\">Pick Year</option>");
	    for(int yy:years){
		out.println("<option>"+yy+"</option>");
	    }				
	    out.println("</select>");
	    out.println("&nbsp;&nbsp;<strong>Season: </strong>");
	    out.println("<select name=\"season\">");
	    out.println("<option value=\"\">Pick Season</option>\n");
	    out.println(Helper.allSeasons);
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=\"right\"> ");
	    out.println("<input type=\"submit\" value=\"Next\" />");
	    out.println("</td></tr>");
	    out.println("</form>");
	    out.println("</td></tr></table>");
	    out.println("</td></tr>"); // end of middle
	    //
	}										
	else if(choice.equals("code")){
	    // Code
	    //
	    GregorianCalendar cal = new GregorianCalendar();
	    String year2 =  "" + cal.get(Calendar.YEAR);  
	    //
	    out.println("<form name=\"myForm\" method=\"post\" action=\""+url +
			"CodeBrowse\""+
			" onSubmit=\"return validateForm()\">");			
	    out.println("<table border=0><tr><td align=\"center\">"); 
	    out.println("<font color=\"#000099\" size=\"+2\">Code Entry"+
			"</font></td></tr>");
	    out.println("<tr><td><font color=\"green\"> Use this option to add "+
			"codes to programs and sessions that need codes.<br>");
	    out.println("Select the year and season then click on the "+
			"Submit button</font>");

	    out.println("</td></tr><tr><td align=\"center\">");
	    out.println("<b>Year: </b>");
	    out.println("<select name=\"year\">");
	    if(year.equals("")) year = year2;
	    int[] years2 = Helper.getFutureYears();
	    for(int yy:years2){
		String selected = "";
		if(year.equals(""+yy)){
		    selected="selected=\"selected\"";
		}
		out.println("<option "+selected+">"+yy+"</option>");
	    }					
	    out.println("</select>");
	    out.println("&nbsp;&nbsp;&nbsp;<b>Season: </b>");
	    out.println("<select name=\"season\">");
	    out.println("<option selected=\"selected\">"+season+"\n");
	    out.println(Helper.allSeasons);
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=\"right\">");
	    out.println("<input type=\"submit\" name=\"action\" value=\"Submit\" "+
			" />");
	    out.println("</td></tr></form>");
	    out.println("</td></tr></table>");
	    out.println("</td></tr>"); // end of code
	    //
	}
	else if(choice.equals("calendar")){
	    //
	    GregorianCalendar cal = new GregorianCalendar();
	    String year2 =  "" + cal.get(Calendar.YEAR);  
	    int month2 = cal.get(Calendar.MONTH) + 1;
	    
	    out.println("<table border=0><tr><td align=center colspan=2>"); // 
	    out.println("<FONT color=#000099 size=+2>Calendar "+
			"</FONT>");
	    out.println("<form name=calForm method=post action="+
			url + "Agenda?>");
	    out.println("</td></tr><tr><td align=center colspan=2>");
	    out.println("<b>Year: </b>");
	    out.println("<select name=\"year\">");
	    if(year.equals("")) year = year2;
	    for(int yy:years){
		String selected = "";
		if(year.equals(""+yy)){
		    selected="selected=\"selected\"";
		}
		out.println("<option "+selected+">"+yy+"</option>");
	    }					
	    out.println("</select>");
	    out.println("&nbsp;&nbsp;&nbsp;<b>Month: </b>");
	    out.println("<select name=\"month\">");
	    if(month == 0) month = month2;
	    out.println("<option selected value="+month+">"+
			Months[month]+"\n");
	    out.println(allMonths);
	    out.println("</select></td></tr>");
	    // Area
	    out.println("<tr><td><b>Area: </b></td><td>");
	    out.println("<select name=\"area_id\">");
	    out.println("<option value=\"\"></option>");
	    if(areas != null){
		for(Type one:areas){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }		
	    out.println("</select></td></tr>");
	    //
	    // Lead
	    out.println("<tr><td><b>Lead: </b></td><td>");
	    out.println("<select name=\"lead_id\">");
	    out.println("<option value=\"\"></option>");
	    if(leads != null){
		for(Type one:leads){
		    if(one.isActive())
			out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select></td></tr>");
	    //
	    // category
	    out.println("<tr><td><b>Heading: </b></td><td>");
	    out.println("<select name=\"category_id\">");
	    out.println("<option value=\"\"></option>");
	    if(categories != null){
		for(Type one:categories){
		    String selected = "";
		    if(!one.isActive()) continue;
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }		
	    out.println("</select></td></tr>");
	    //
	    // Location
	    out.println("<tr><td><b>Location: </b></td><td>");
	    out.println("<select name=\"location_id\">");
	    out.println("<option value=\"\"></option>");			
	    if(locations != null){
		for(Type one:locations){
		    String selected = "";
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }		
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=right colspan=2>");
	    out.println("<input type=submit value=Submit"+
			"></input>");
	    out.println("</td></tr></table>");// end of calendar table
	}
	out.println("</td></tr></table>"); // end of overall
	//
	// log out section
	//
	out.println("</form>");

	out.println("<center><table>");
	out.println("<tr><td><A href="+url+
		    "Logout?"+
		    " target=_parent>Log Out </a>");
	out.println("</td></tr></table></center>");

	out.println("</BODY></HTML>");
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

