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

@WebServlet(urlPatterns = {"/FacilityBrowse","/FacilitySearch"})
public class FacilitySearch extends TopServlet{

    static Logger logger = LogManager.getLogger(FacilitySearch.class);
    /**
     * The main class method doGet.
     *
     * Create an html page for the FacilityBrowse form.
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

	PrintWriter out;

	String username="";
	String allFacility = "";
	boolean success = true;
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, message="", lead_id="";
	out.println("<html>");
	FacilityList facils = null;
	allFacility = "";
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("lead_id")){
		lead_id = value;
	    }
	}
	LeadList leads = null;
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    facils = new FacilityList(debug);
	    back = facils.lookFor();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }			
	}				
	String log_out = 
	    "<a href="+url+"Logout?" +
	    " target=_parent>Log Out </a>";
	//
	// get the options for the select widgets
	//
	out.println("<center><h2>Facilities Search</h2></center>");
	out.println("<form method=post action="+url+"FacilityTable>");
	out.println("<center><table border=\"1\" width=\"80%\">");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");
	out.println("<table width=\"100%\">");
	out.println("<tr><td align=\"right\">");
	out.println("<strong>Sort by </strong></td><td align=left>");
	out.println("<select name=sortby>");
	out.println("<option selected value=id>ID");
	out.println("<option value=facility>Facility");
	out.println("</select></td></tr>");
	out.println("<tr bgcolor=#CDC9A3><td align=right><strong>Show "+
		    "Records From </strong>"+
		    "</td><td><left>");
	out.println("<input type=text name=minRecords value=0 size=6><strong>"
		    +" To </strong>");
	out.println("<input type=text name=maxRecords value=100 size=6></td></tr>");
	out.println("<tr><td align=right><strong>Lead "+
		    "</strong></td><td>");
	out.println("<select name=\"lead_id\">");
	out.println("<option value=\"\">All</option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = one.getId().equals(lead_id)?"selected=\"selected\"":""; 
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");				
	out.println("<tr><td align=right><strong>Facility Name "+
		    "</strong></td><td><left>");
	out.println("<select name=\"id\">");
	out.println("<option value=\"\">All</option>");
	if(facils != null && facils.size() > 0){
	    for(Facility one:facils){
		out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=right><strong>Type "+
		    "</strong></td><td><left>");
	out.println("<select name=\"type\">");
	out.println("<option value=\"\">All</option>");
	for(String str:Facility.facility_types){
	    out.println("<option value=\""+str+"\">"+str+"</option>");
	}
	out.println("</select></td></tr>");							
				
	out.println("</table></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<table width=\"100%\">");
	out.println("<tr><td align=\"right\">");
	out.println("<input type=\"submit\" value=\"Search\" />");
	out.println("</td></tr></table>");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</center>");
	out.println("</form>");
	out.println("</body></html>");
	out.close();
    }
    /**
     * The main class method.
     *
     * Create an html page for the facility browser form.
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






















































