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

@WebServlet(urlPatterns = {"/MarketBrowse","/MarketSearch"})
public class MarketSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(MarketSearch.class);
    /**
     * The search engine for marketing.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String program_area_options = "";

	String sortby = "",username="", message="";
	String startDateFrom="", startDateTo="";
	String endDateFrom="", endDateTo="";
	Enumeration values = req.getParameterNames();
	String allCategory = "";
	String allPrograms = "";
	TypeList categories = null;
	boolean success = true;
	String name, value;
	out.println("<html>");

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("username")){
		username = value;
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
	List<Type> allAds = null;
	List<Type> allAnnounces = null;
	List<Type> marketTypes = null;
	if(true){
	    categories = new TypeList(debug, "categories");
	    String back = categories.find();
	    if(allAds == null){
		AdList ones = new AdList(debug);
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    allAds = ones;
		}
	    }
	    if(allAnnounces == null){
		AnnounceList ones = new AnnounceList(debug);
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    allAnnounces = ones;
		}				
	    }
	    if(marketTypes == null){
		TypeList ones = new TypeList(debug, "marketing_types");
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    marketTypes = ones;
		}					
	    }
	}
	//
	// Browsing the records
	//
	out.println("<head><title>Search Marketing</title>");
	//
	// This script validate 
	//
	out.println(" <script type=text/javascript>");
	out.println(" </script>                    ");
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);
	//
	out.println("<h2>Marketing Search</h2>");
	if(!message.equals("")){
	    if(!success)
		out.println("<font color=\"red\">"+message+"</font><br />");
	}
	out.println("<form method=\"post\" name=\"myForm\" "+
		    " action=\""+url+"MarketTable\" >");
	out.println("<center><table border=\"1\" width=\"90%\">");
	out.println("<tr bgcolor=#CDC9A3><td>");
	out.println("<table width=\"100%\">");
	out.println("<tr><td align=\"right\"><b>");
	out.println("Sort by: </b></td><td align=\"left\">");
	out.println("<select name=\"sortBy\">");
	out.println("<option value=\"m.id\">ID");
	out.println("</select></td></tr>");
	//
	// Year season
	out.println("<tr><td align=\"right\"><strong>Year:");
	out.println("</td></strong><td align=\"left\">");
	out.println("<select name=\"year\">");
	int years[] = Helper.getPrevYears();
	out.println("<option value=\"\">All\n");		
	for(int yy:years){
	    out.println("<option>"+yy+"</option>");
	}		
	out.println("</select>");
	out.println("&nbsp;&nbsp;<strong>Season:");
	out.println("</strong>");
	out.println("<select name=\"season\">");
	out.println("<option selected value=\"\">All\n"); // all
	out.println(Helper.allSeasons);
	out.println("</select></td></tr>");
	//
	// category, programs
	out.println("<tr><td align=right>");
	out.println("<strong>Guide Heading: </strong></td><td>");
	out.println("<select name=\"category_id\">");
	out.println("<option value=\"\">All</option>");
	if(categories != null){
	    for(Type cat:categories){
		if(cat.isActive())
		    out.println("<option value=\""+cat.getId()+"\">"+cat+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</left></td></tr>");
	//
	// Program Ad 
	out.println("<tr><td align=right><strong>Market Ads:");
	out.println("</td></strong><td align=\"left\">");
	out.println("<select name=\"market_ad\">");
	out.println("<option value=\"\">All</option>");
	for(Type one:allAds){
	    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	}
	out.println("</select></td></tr>");
	//
	out.println("<tr><td align=right valign=top><b>Announcements: ");
	out.println("</b></td><td>");
	out.println("<select name=\"market_announce\">");
	out.println("<option value=\"\">All</option>");
	for(Type one:allAnnounces){
	    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	}
	out.println("</select></td></tr>");		
	out.println("</td></tr>");
	out.println("<tr><td align=right valign=top><b>Marketing Type: ");
	out.println("</b></td><td>");
	out.println("<select name=\"market_type\">");
	out.println("<option value=\"\">All</option>");
	for(Type one:marketTypes){
	    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	}
	out.println("</select></td></tr>");		
	out.println("</td></tr>");
	//
	// Start Dates
	//
	out.println("<tr><td align=\"left\" colspan=\"2\">Note:The following due date applies to marketing ads or maketing pieces due dates</td></tr>");		
	out.println("<tr><td align=right><b>Due Date </b>");
	out.println("</td><td align=\"left\">");
	out.println("<b>from: </b>");
	out.println("<input type=text name=date_from maxlength=10 "+
		    "value=\"\" size=10>");
	out.println("<b>To: </b>");
	out.println("<input type=text name=date_to maxlength=10 "+
		    "value=\"\" size=10>");
	out.println("(mm/dd/yyyy)</td></tr>");
	// 
	out.println("</table></td></tr>");
	out.println("<tr><td align=right><input type=\"submit\" value=\"Browse\" "+
		    "></td></tr>");
	out.println("</table>");
	out.println("<br />");
	out.println("</form>");		
	out.println("</center>");
	out.println("</body>\n</html>");
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






















































