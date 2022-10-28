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
/**
 * All users menu form.
 */
@WebServlet(urlPatterns = {"/UserMenu"})
public class UserMenu extends TopServlet{

    static Logger logger = LogManager.getLogger(UserMenu.class);
    /**
     * Generates the menu form.
     *
     * @param req
     * @param res
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	int access = 0;
	String username = "", id="";

	Enumeration values = req.getParameterNames();
	String name, value,user_dept_no="",fullName="";
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("id")){
		id = value;
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
	// check for the user
	//
	out.println("<HTML><HEAD><TITLE> User Menu </TITLE>");
	out.println(" <script language=Javascript>");
	out.println("  function validateForm() {             ");    
	out.println("   return true;                         ");
	out.println("      }                                 ");               
	out.println("  function movepic(img_name,img_src) {  ");
	out.println("  document[img_name].src=img_src;       ");
	out.println("      }                                 ");   
	out.println("</script>                               ");      
	out.println("</HEAD><BODY><br>");
	out.println("<center><b><font color=blue>Main&nbsp;Menu</font></b>"+
		    "<br><br>");
	out.println("<table border=0 cellspacing=0 cellpadding=0><tr><td>");
	out.println("<table cellpadding=0 cellspacing=0>");
	//
	// for high access users only
	//
	// New Plan
	String url2 = url +"images/";
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"PrePlan.do\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('plan','"+url2+
		    "plan2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('plan','"+url2+
		    "plan.jpg');\">");
	out.println("<img src=\""+url2+"plan.jpg\" name=\"plan\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"New Plan\" /></a>");
	out.println("</td></tr>");
	//
	// Plan search
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"PlanSearch\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('plan_s','"+url2+
		    "plan_search2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('plan_s','"+url2+
		    "plan_search.jpg');\">");
	out.println("<img src=\""+url2+"plan_search.jpg\" name=\"plan_s\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Search Plans\" /></a>");
	out.println("</td></tr>");
	//
	//
	// Search Program
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"Search\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('prog_s','"+url2+
		    "program_search2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('prog_s','"+url2+
		    "program_search.jpg');\">");
	out.println("<img src=\""+url2+"program_search.jpg\" name=\"prog_s\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Search Programs\" /></a>");
	out.println("</td></tr>");
	//
	// New Facility
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"Facility\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('facil','"+url2+
		    "facility2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('facil','"+url2+
		    "facility.jpg');\">");
	out.println("<img src=\""+url2+"facility.jpg\" name=\"facil\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"New Facility\" /></a>");
	out.println("</td></tr>");
	//
	// Search Facility
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"FacilitySearch\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('facil_s','"+url2+
		    "facility_search2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('facil_s','"+url2+
		    "facility_search.jpg');\">");
	out.println("<img src=\""+url2+"facility_search.jpg\" name=\"facil_s\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Facility Search\" /></a>");
	out.println("</td></tr>");
	//
	// Marketing
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"MarketSearch\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('market','"+url2+
		    "market2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('market','"+url2+
		    "market.jpg');\">");
	out.println("<img src=\""+url2+"market.jpg\" name=\"market\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Marketing Search\" /></a>");
	out.println("</td></tr>");
	//
	// Volunteer
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"VolunteerSearch\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('volunt','"+url2+
		    "volunt2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('volunt','"+url2+
		    "volunt.jpg');\">");
	out.println("<img src=\""+url2+"volunt.jpg\" name=\"volunt\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Volunteer Search\" /></a>");
	out.println("</td></tr>");
	//
	// General Listings
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"GeneralSearch\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('gen','"+url2+
		    "gen2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('gen','"+url2+
		    "gen.jpg');\">");
	out.println("<img src=\""+url2+"gen.jpg\" name=\"gen\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"General Listings\" /></a>");
	out.println("</td></tr>");		
	//
	// Code Entry
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"SubMenu?choice=code\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('code','"+url2+
		    "code2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('code','"+url2+
		    "code.jpg');\">");
	out.println("<img src=\""+url2+"code.jpg\" name=code "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Code Entry\"></a>");
	out.println("</td></tr>");

	// Program Publishing
	if(user.isAdmin()){				
	    out.println("<tr><td width=\"100\" height=\"20\">");
	    out.println("<a href=\""+url+"SubMenu?choice=toPublish\""+
			" target=\"rightFrame\" ");
	    out.println(" onmouseover=\"movepic('publish','"+url2+
			"publish2.jpg');\" " ); 
	    out.println(" onmouseout=\"movepic('publish','"+url2+
			"publish.jpg');\">");
	    out.println("<img src=\""+url2+"publish.jpg\" name=\"publish\" "+
			"width=\"100\" height=\"20\" border=\"0\" "+
			"alt=\"Program selection for publishing\"></a>");
	    out.println("</td></tr>");
						
	    // Program Unpublishing
	    out.println("<tr><td width=\"100\" height=\"20\">");
	    out.println("<a href=\""+url+"SubMenu?choice=unPublish\""+
			" target=\"rightFrame\" ");
	    out.println(" onmouseover=\"movepic('unpublish','"+url2+
			"unpublish2.jpg');\" " ); 
	    out.println(" onmouseout=\"movepic('unpublish','"+url2+
			"unpublish.jpg');\">");
	    out.println("<img src=\""+url2+"unpublish.jpg\" name=\"unpublish\" "+
			"width=\"100\" height=\"20\" border=\"0\" "+
			"alt=\"Unpublish program selection\"></a>");
	    out.println("</td></tr>");
	}
	//
	// Instructors
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"ContactBrowse\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('cont','"+url2+
		    "cont2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('cont','"+url2+
		    "cont.jpg');\">");
	out.println("<img src=\""+url2+"cont.jpg\" name=\"cont\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Instructors\"></a>");
	out.println("</td></tr>");
	//
	// Locations
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"LocationBrowse\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('loc','"+url2+
		    "loc2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('loc','"+url2+
		    "loc.jpg');\">");
	out.println("<img src=\""+url2+"loc.jpg\" name=\"loc\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Locations\" /></a>");
	out.println("</td></tr>");		
		
	//
	// Reports
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"SubMenu?"+
		    "&choice=report\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('report','"+url2+
		    "report2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('report','"+url2+
		    "report.jpg');\">");
	out.println("<img src=\""+url2+"report.jpg\" name=\"report\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Reports\" /></a>");
	out.println("</td></tr>");
	//
	// Calendar
	out.println("<tr><td width=\"100\" height=\"20\">");
	out.println("<a href=\""+url+"SubMenu?"+
		    "choice=calendar\""+
		    " target=\"rightFrame\" ");
	out.println(" onmouseover=\"movepic('cal','"+url2+
		    "calendar2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('cal','"+url2+
		    "calendar.jpg');\">");
	out.println("<img src=\""+url2+"calendar.jpg\" name=\"cal\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Events Calendar\" /></a>");
	out.println("</td></tr>");
	// media
	/**
	if(user.isAdmin()){
	    out.println("<tr><td width=\"100\" height=\"20\">");
	    out.println("<a href=\""+url+"MediaUpload?obj_type=Default&obj_id=0\""+
			" target=\"rightFrame\" ");
	    out.println(" onmouseover=\"movepic('media','"+url2+
			"media2.jpg');\" " ); 
	    out.println(" onmouseout=\"movepic('media','"+url2+
			"media.jpg');\">");
	    out.println("<img src=\""+url2+"media.jpg\" name=\"media\" "+
			"width=\"100\" height=\"20\" border=\"0\" "+
			"alt=\"Default Media Files\" /></a>");
	    out.println("</td></tr>");
	}
	*/
	//
	// logout
	out.println("<tr><td valign=bottom width=\"100\" height=\"20\">");
	out.println("<A href=\""+url+"Logout\" target=\"_top\" ");
	out.println(" onmouseover=\"movepic('logout','"+url2+"logout2.jpg');\" " ); 
	out.println(" onmouseout=\"movepic('logout','"+url2+"logout.jpg');\">");
	out.println("<img src=\""+url2+"logout.jpg\" name=\"logout\" "+
		    "width=\"100\" height=\"20\" border=\"0\" "+
		    "alt=\"Logout\" /></a>");
	out.println("</td></tr></table>");

	out.println("</td></tr></table>");
	out.println("</BODY></HTML>");
	out.close();
    }				   
    /**
     * @link #doGet
     * @see #doGet
     */		  
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	doGet(req, res);
    }

}

