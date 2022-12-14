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

@WebServlet(urlPatterns = {"/MarketReport"})
public class MarketReport extends TopServlet{

    static Logger logger = LogManager.getLogger(MarketReport.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    /**
     * Processes the marketing search request. 
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter os, out;
	String sortby = "";
	String message="";
	boolean success = true;

	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	os = res.getWriter();
	Enumeration values = req.getParameterNames();
	//obtainProperties(os);
	String name, value;
	String year = "", season = "";
	os.println("<html>");
	ProgramList plist = new ProgramList(debug);
	plist.setHasMarket(); // join marketing table
	GeneralList glist = new GeneralList(debug);
	glist.setHasMarket();
	FacilityList flist = new FacilityList(debug);
	flist.setHasMarket();
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("season")){
		season = value;
		plist.setSeason(value);
		glist.setSeason(value);
		flist.setSeason(value);
	    }
	    else if(name.equals("year")){
		year = value;
		plist.setYear(value);
		glist.setYear(value);
		flist.setYear(value);
	    }
	    else if(name.equals("sortby")){
		plist.setSortby(value);
	    }
	    else if(name.equals("id")){  // progam id
		plist.setId(value);
	    }
	    else if(name.equals("category_id")){
		plist.setCategory_id(value);
	    }
	    else if(name.equals("lead_id")){   // by zoom
		plist.setLead_id(value);
		glist.setLead_id(value);
	    }
	    else if(name.equals("area_id")){   // by zoom
		plist.setArea_id(value);
	    }
	    else if(name.equals("nraccount")){   // by zoom
		plist.setNraccount(value);
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
	os.println("<head><title>Marketing Report" + 
		   "</title>");
	Helper.writeWebCss(os, url);
	os.println("</head>");
	os.println("<body>");
	Helper.writeTopMenu(os, url);
	//
	// check where clause 
	//
	String back = plist.find();
	back += glist.find();
	back += flist.lookFor();
	if(!back.equals("")){
	    os.println("<h3> Error "+back+"</b3>");
	    os.println("</body></html>");
	    os.close();
	    return;
	}

	if(plist.size() == 0 && glist.size() == 0 && flist.size() == 0){
	    //
	    Helper.sendMessage(os, "No Programs","No records found "+
			       "make sure you select the right year and season");
	}
	else{
	    Helper.writeFirstPage(os,"Marketing Report",year, season);
	    os.println("</font><br />");
	    if(glist.size() > 0){
		os.println(" Total General Listings :"+ glist.size() + "<br />");
		os.println("<hr width=\"75%\"/>");
		Helper.writeGenerals(os, glist, url,"General Listings", false);
	    }
	    if(flist.size() > 0){
		os.println(" Total Facilities :"+ flist.size() + "<br />");
		os.println("<hr width=\"75%\"/>");
		Helper.writeFacilities(os, flist, url);
	    }
	    if(plist.size() > 0){
		os.println("Total Programs :"+ plist.size() + "<br />");
		os.println("<hr width=\"75%\"/>");
		//
		for(Program prog:plist){
		    String cat="", id="";
		    Market market = null;				
		    String str2="";
		    id = prog.getId(); 
		    Helper.writeProgram(os, prog, false, false, false, url);
		    //
		    // write sessions
		    if(prog.hasSessions()){
			Helper.writeSessions(os, prog.getSessions(), prog.getSessionOpt(), prog.getId(), url, debug);
		    }
		    if(prog.hasFiles()){
			Helper.printFiles(os, url, prog.getFiles());
		    }
		    if(prog.hasMarket()){
			market = prog.getMarket();
			Helper.writeMarket(os, market, url);
			if(market.hasFiles()){
			    Helper.printFiles(os, url, market.getFiles());
			}
		    }
		    //
		    if(prog.hasShifts()){
			Helper.writeVolShifts(os, prog.getShifts(), url,"Volunteer Shifts", false);
		    }
		    //
		    // sponsors
		    if(prog.hasSponsors()){
			List<Sponsor> slist = prog.getSponsors();
			Helper.writeSponsors(os, slist, url, debug);
		    }
		    //
		    os.println("<br /><hr width=\"75%\"><br />");
										
		}
	    }
	}
	os.flush();
	os.println("</form>");
	os.println("</center></body>");
	os.println("</html>");
	os.flush();
	os.close();
    }
				
}






















































