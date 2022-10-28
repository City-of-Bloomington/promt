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

@WebServlet(urlPatterns = {"/ContactBrowse","/ContactSearh"})
public class ContactSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(ContactSearch.class);

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
	String name, value, action="";
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
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	ContactList clist = new ContactList(debug);
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		clist.setId(value);
		id = value;
	    }
	    else if(name.equals("name")){
		clist.setName(value);
	    }
	    else if(name.equals("phone")){
		clist.setPhone(value);
	    }
	    else if(name.equals("email")){
		clist.setEmail (value);
	    }
	    else if(name.equals("address")){
		clist.setAddress (value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}

	//
       	if(!action.equals("")){
	    String back = clist.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    if(clist.size() == 1){
		Contact one = clist.get(0);
		String str = url+"Contact.do?fromBrowse=y&action=zoom&id="+one.getId();
		res.sendRedirect(str);
		return;
	    }
	}
	//
	// This script validates textareas and facility
	// 
	out.println("<script type=\"text/javascript\">");
	out.println("	</script>                          ");   
	out.println("</head><body>");
	//
	out.println("<center>");
	out.println("<h2>Instructors Search</h2>");
	if(!message.equals("")){
	    if(success)
		out.println("<font color=\"green\">"+message+"</font>");
	    else
		out.println("<font color=\"red\">"+message+"</font>");
	    out.println("<br>");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\">");
	if(action.equals("")){
	    out.println("<p>Before you decide to add a new instructor, Please make sure he/she is not already in the system by doing search in this form</p>");
	}
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table>");
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Instructor Search Options </font></h3></td></tr>");
	out.println("<tr><td align=\"right\"><b>Instructor ID: </b></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"id\" "+
		    "value=\"\" maxlength=\"10\" size=\"10\" /></td></tr>"); 
	out.println("<tr><td align=\"right\"><b>Name: </b></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"name\" "+
		    "value=\"\" maxlength=\"30\" size=\"30\" />*</td></tr>"); 
	out.println("<tr><td align=\"right\"><b>Phone:</b></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"phone\" maxlength=\"12\" size=\"12\" />*</td></tr>");
	out.println("<tr><td align=\"right\"><b>Email:</b></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"email\" "+
		    "value=\"\" maxlength=\"50\" size=\"50\" /></td></tr>");
	out.println("<tr><td align=\"right\"><b>Address:</b></td><td align=\"left\">");
	out.print("<input type=\"text\" name=\"address\" size=\"30\" />");
	out.println("*</td></tr>");
	out.println("</table></td></tr>");
	//
	out.println("<tr><td><table width=\"100%\">");
	out.println("<td align=\"right\">");				
	out.println("<input type=\"submit\" "+
		    "name=\"action\" value=\"Search\" /></td>");
	out.println("<td align=\"right\">");			
	out.println("<input type=\"button\" onclick=\"document.location='"+url+"Contact.do';\" value=\"New Instructor\" /></td>");		
	out.println("</tr>");
	out.println("</table></td></tr>");
			
	out.println("</table>");
	out.println("<p>*<font color=\"green\" size=\"-1\"> Notice:you can enter partial name, word or number. </font></p>");
	if(!action.equals("") && clist.size() == 0){
	    out.println("<h3> No match found </h3>");
	}
	else if(clist.size() > 0){
	    out.println("<h3> Found "+clist.size()+" records </h3>");
	    out.println("<table border=\"1\"><caption>Search Results</caption>");
	    out.println("<tr><th>ID</th><th>Name</th><th>Address</th><th>Email</th><th>Phones</th></tr>");
	    for(Contact one:clist){
		out.println("<tr>");
		out.println("<td><a href=\""+url+"Contact.do?fromBrowse=y&action=zoom&id="+one.getId()+"\">Edit</a></td>");
		out.println("<td>"+one.getName()+"</td>");				
		out.println("<td>&nbsp;"+one.getAddress()+"</td>");
		out.println("<td>&nbsp;"+one.getEmail()+"</td>");
		out.println("<td>&nbsp;"+one.getPhones()+"</td>");
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	out.println("<br />");
	out.print("</center></body></html>");
	out.close();
    }

}





































