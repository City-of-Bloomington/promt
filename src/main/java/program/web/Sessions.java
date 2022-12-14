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

@WebServlet(urlPatterns = {"/Sessions"})
public class Sessions extends TopServlet{

    static Logger logger = LogManager.getLogger(Sessions.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	doPost(req, res);
    }
    /**
     * Create a session form and handles the view, add, update and delete 
     * operations. 
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	String allPrograms = "";
	String ptitle="", pyear="", season = "";

	res.setContentType("text/html");
	PrintWriter out = null;
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value;
	String code="", days="", startEndTime="", inCityFee="", otherFee=""; 
	String ageAge="", minMaxEnroll="", description="", startEndDate="";
	String regDeadLine="", nonCityFee="", location_id="", partGrade="";
	String classCount="", instructor="", sid="1", id="", sessionSort=""; 
	String startDate="", endDate="", ageFrom="", ageTo="", wParent="",
	    otherAge="", allAge="";
	String action = "startNew", pid="",ptitle2="", message="";
	// Newly added vars
	String d_sun="",d_mon="",d_tue="",d_wed="",d_thu="",d_fri="",
	    d_sat="",d_all="",d_mon_fri="";
	String sid2[];
	boolean setAction = false, success = true;

	//
	// this flag is used to check if the user set the session
	// fields in the program form
	// if not he/she should be warned
	//
	boolean	 sessionShowFlag = false; //to check if the user set 
	//
	// session showing info
	//
	boolean  daysShow=false, startEndTimeShow=false, 
	    inCityFeeShow=false, otherFeeShow=false; 
	boolean allAgeShow=false, minMaxEnrollShow=false, 
	    descriptionShow=false, startDateShow=false;
	boolean regDeadLineShow=false, nonCityFeeShow=false, 
	    locationShow=false, partGradeShow=false;
	
	boolean classCountShow=false, instructorShow=false;
	boolean newSession = true;
	//
	allPrograms = "";
	pyear=""; season=""; 
	String [] vals;
	LocationList locations = null;
	locations = new LocationList(debug);
	if(true){
	    String back = locations.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	Session se = new Session(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){ // program id
		id = value;
		se.setId(value);
	    }
	    else if(name.equals("pid")){ // program id from hidden field
		pid = value;
		se.setId(value);
	    }
	    else if(name.equals("sid")){ // session id
		se.setSid(value);
		newSession = false;
	    }
	    else if(name.equals("pyear")){ 
		pyear = value;
	    }
	    else if(name.equals("season")){
		season = value;
	    }
	    else if(name.equals("sessionSort")){
		sessionSort = value;
	    }
	    else if(name.equals("code")){
		se.setCode(value);
	    }
	    else if(name.equals("days")){
		se.setDays(value);
	    }
	    else if(name.equals("d_sun")){
		d_sun = value;
	    }
	    else if(name.equals("d_mon")){
		d_mon = value;
	    }
	    else if(name.equals("d_tue")){
		d_tue = value;
	    }
	    else if(name.equals("d_wed")){
		d_wed = value;
	    }
	    else if(name.equals("d_thu")){
		d_thu = value;
	    }
	    else if(name.equals("d_fri")){
		d_fri = value;
	    }
	    else if(name.equals("d_sat")){
		d_sat = value;
	    }
	    else if(name.equals("d_mon_fri")){
		d_mon_fri = value;
	    }
	    else if(name.equals("d_all")){
		d_all = value;
	    }
	    else if(name.equals("inCityFee")){
		se.setInCityFee(value);
	    }
	    else if(name.equals("nonCityFee")){
		se.setNonCityFee(value);
	    }
	    else if(name.equals("otherFee")){
		se.setOtherFee(value);
	    }
	    else if(name.equals("memberFee")){
		se.setMemberFee(value);
	    }
	    else if(name.equals("nonMemberFee")){
		se.setNonMemberFee(value);
	    }			
	    else if(name.equals("allAge")){ // all ages check flag
		se.setAllAge(value);
	    }
	    else if(name.equals("otherAge")){ 
		se.setOtherAge(value);
	    }
	    else if(name.equals("wParent")){ 
		se.setWParent(value);
	    }
	    else if(name.equals("partGrade")){
		se.setPartGrade(value);
	    }
	    else if(name.equals("minMaxEnroll")){
		se.setMinMaxEnroll(value);
	    }
	    else if(name.equals("description")){
		se.setDescription(value);
	    }
	    else if(name.equals("endTime")){
		se.setEndTime(value);
	    }
	    else if(name.equals("startTime")){
		se.setStartTime(value);
	    }						
	    else if(name.equals("regDeadLine")){
		se.setRegDeadLine(value);
	    }
	    else if(name.equals("location_id")){
		se.setLocation_id(value);
	    }
	    else if(name.equals("classCount")){
		se.setClassCount(value);
	    }
	    else if(name.equals("instructor")){
		se.setInstructor(value);
	    }
	    else if(name.equals("ageFrom")){
		se.setAgeFrom(value);
	    }
	    else if(name.equals("ageTo")){
		se.setAgeTo(value);
	    }
	    else if(name.equals("startDate")){
		se.setStartDate(value);
	    }
	    else if(name.equals("endDate")){
		se.setEndDate(value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	    else if(name.equals("action2")){
		if(!value.equals(""))
		    action = value;
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
	if(action.startsWith("Session")) action = "";//coming from Program

	if(!id.equals("")){
	    pid = id;  // in case the user changed the program id
	}
	else if(!pid.equals("") && id.equals("")){
	    id = pid;
	}
	else if(!pid.equals("") && !id.equals("")){
	    pid = id;  // when changing the program in update
	}
	if(!startDate.equals("")){
	    if(startDate.length() < 10) startDate += "/"+pyear;
	}
	if(!endDate.equals("")){
	    if(endDate.length() < 10) endDate += "/"+pyear;
	}
	if(!regDeadLine.equals("")){
	    if(regDeadLine.length() < 10) regDeadLine += "/"+pyear;
	}
	if(!success){
	    out.println("Database not available<br>");
	    out.println("Contact ITS to report the problem<br>");
	    return;
	}
	if(!d_all.equals("")){
	    days="M-Su"; // every day of the week
	}
	if(!d_mon_fri.equals("")){
	    days="M-F"; 
	}
	else{
	    boolean in = false;
	    if(!d_sun.equals("")){
		days = "Su";
		in = true;
	    }
	    if(!d_mon.equals("")){
		if(in) days += " ";
		days += "M";
		in = true;
	    }
	    if(!d_tue.equals("")){
		if(in) days += " ";
		days += "Tu";
		in = true;
	    }
	    if(!d_wed.equals("")){
		if(in) days += " ";
		days += "W";
		in = true;
	    }
	    if(!d_thu.equals("")){
		if(in) days += " ";
		days += "Th";
		in = true;
	    }
	    if(!d_fri.equals("")){
		if(in) days += " ";
		days += "F";
		in = true;
	    }
	    if(!d_sat.equals("")){
		if(in) days += " ";
		days += "Sa";
	    }
	}
	if(!days.equals("")){
	    se.setDays(days);
	}
	//
	// if only one of the parameters is passed
	//
	Program pr = new Program(debug, se.getId());
	String back = pr.doSelect();
	if(back.equals("")){
	    ptitle = pr.getTitle();
	    pyear = pr.getYear();
	    season = pr.getSeason();
	}
	else{
	    logger.error(back);
	}
	SessionOpt sopt = new SessionOpt(debug, id);
	sopt.doSelect();
	if(!back.equals("")){
	    logger.error(back);
	}
	if(sessionSort.equals("")){
	    sessionSort = sopt.getSessionSort();
	}
	else if(!sessionSort.equals(sopt.getSessionSort())){
	    sopt.setSessionSort(sessionSort);
	    sopt.doUpdate();
	}
	String query = "";
	if(action.equals("Delete")){
	    // 
	    // key is code and year for each session
	    // the code could be recycled, therefore it
	    // is not enough as a primary key
	    //
	    if(user.canDelete()){
		CalHandle ch = new CalHandle(debug, se.getId(), se.getSid());
		back = ch.doDelete();
		if(!back.equals("")){
		    message += back;
		    success = false;
		    logger.error(back);
		}
		back = se.doDelete();
		if(!back.equals("")){
		    message += " Could not delete "+back;
		    success = false;
		    logger.error(back);
		}
		else{
		    newSession = true;
		    message += " Deleted successfully";
		}
	    }
	    else{
		message += " You could not delete ";
		success = false;
	    }
	}
	else if(action.equals("zoom")){
	    back = se.doSelect();
	    if(!back.equals("")){
		message += " Could not retreive data "+back;
		success = false;
		logger.error(back);
	    }
	    else {
		days = se.getDays();
		if(!days.equals("")){
		    days=days.toUpperCase(); // backward comptability
		    if(days.indexOf("- SU") > -1 || 
		       days.indexOf("-SU") > -1) // the new format
			d_all = "y";
		    else if(days.indexOf("- F") > -1 ||
			    days.indexOf("-F") > -1) 
			d_mon_fri = "y";
		    else{
			if(days.indexOf("SU") > -1) d_sun="y";
			if(days.indexOf("M") > -1) d_mon="y";
			if(days.indexOf("TU") > -1) d_tue="y";
			if(days.indexOf("W") > -1) d_wed="y";
			if(days.indexOf("TH") > -1) d_thu="y";
			if(days.indexOf("F") > -1) d_fri="y";
			if(days.indexOf("SA") > -1) d_sat="y";
		    }
		}
	    }
	}
	else if(action.equals("Update")){
	    if(user.canEdit()){
		back = se.doUpdate();
		if(!back.equals("")){
		    message += " Could not update data "+back;
		    success = false;
		    logger.error(back);
		}
		else{
		    message = "Updated successfully";
		    CalHandle ch = new CalHandle(debug, se.getId(), se.getSid());
		    back = ch.process();
		    if(!back.equals("")){
			message += back;
			success = false;
			logger.error(back);
		    }
		}
	    }
	    else{
		message += " You can not update ";
		success = false;
	    }
	}
	else if(action.equals("Save")){
	    //
	    if(user.canEdit()){
		back = se.doSave();
		if(!back.equals("")){
		    message += " Could not save data "+back;
		    success = false;
		    logger.error(back);
		}
		else {
		    //
		    // add to agenda
		    //
		    CalHandle ch = new CalHandle(debug, id, sid);
		    back = ch.process();
		    if(!back.equals("")){
			message += back;
			success = false;
			logger.error(back);
		    }
		}
	    }
	    else{
		message += " You can not save ";
		success = false;
				
	    }
	}
	else if(action.startsWith("Start")){
	    //
	    // we blank some of the fields to avoid
	    // mistake using the same again, such as 
	    // the code
	    //
	    newSession = true;
	    se.setCode("");
	}
	//
	// for each new record we want to update sid
	//
	if(action.startsWith("Start") || action.equals("") || 
	   action.equals("Delete")){
	    sid = se.getNextId();
	}
	if(!se.getAllAge().equals("")) allAge = "checked";
	if(!se.getWParent().equals("")) wParent = "checked";
	if(!d_all.equals("")) d_all = "checked";
	if(!d_mon_fri.equals("")) d_mon_fri = "checked";
	if(!d_sun.equals("")) d_sun = "checked";
	if(!d_mon.equals("")) d_mon = "checked";
	if(!d_tue.equals("")) d_tue = "checked";
	if(!d_wed.equals("")) d_wed = "checked";
	if(!d_thu.equals("")) d_thu = "checked";
	if(!d_fri.equals("")) d_fri = "checked";
	if(!d_sat.equals("")) d_sat = "checked";
	//        
	out.println("<html><head><title>Sessions</title>");
	Helper.writeWebCss(out, url);
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");					
	out.println(" function showStatus(){      ");
	// display it in the status line 
	out.println(" defaultStatus =\" "+message+"\";"); 
	// arrange to do it all again in 1 minute 
	//  setTimeout("display_time_in_status_line()",60000);// milliseconds
	out.println("    }                          ");
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

	out.println(" function checkSelection(element){   ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
	out.println("      if(i > 0){       ");
	out.println("         return true;  ");
	out.println("         }             ");
	out.println("       }               ");
	out.println("    return false;      ");
	out.println("   }                   ");
	out.println(" function checkSelectionValue(element){   ");
	out.println("  var x = \"\"; ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
        out.println("     x = element.options[i].value;     ");
        out.println("     }                           ");
	out.println("    if(x.length == 0 ){          ");
	out.println("       return false;             ");
	out.println("         }                       ");
	out.println("    return true;                 ");
	out.println("      }                          ");
	out.println("  function validateForm(){       ");            
	out.println("	if((document.myForm.sid.value.length == 0)){ ");  
	out.println("        alert(\"Id field must be entered\"); ");
	out.println("	     return false;	       ");
	out.println(" 	    }                          ");
    
        out.println(" if(!validateNumber(document.myForm.ageFrom.value)){ ");
	out.println("  alert(\"Participation Age From not a valid number\");");
	out.println("       return false;                ");
	out.println("      }                             ");
	out.println(" if(!validateNumber(document.myForm.ageTo.value)){ ");
	out.println("  alert(\"Participation Age To not a valid number\"); ");
       	out.println("       return false;                ");
       	out.println("      }                             ");
	out.println("    return true;                   ");
	out.println("	}	                        ");
	out.println("  function validateDelete(){   ");            
	out.println("  var x = false ;                  ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   if(x){  ");
	out.println(" document.myForm.action2.value='Delete'; ");
	out.println(" document.myForm.submit;         ");
	out.println("  }	                                ");						
	out.println("   return x;                       ");
	out.println(" }	                                ");				
	out.println("/*]]>*/\n");			
	out.println(" </script>                         ");   
    
	out.println("</head><body  onLoad=\"showStatus();\">");
	Helper.writeTopMenu(out, url);
	if(action.equals("") || action.startsWith("Start") || 
	   action.equals("Delete"))
	    out.println("<h2><center>New Session</h2>");
	else
	    out.println("<h2><center>Edit Session </h2>");
	out.println("<br>");
	if(!message.equals("")){
	    out.println("<center>");
	    if(success)
		out.println("<font color=green>"+message+"</font>");
	    else
		out.println("<font color=red>"+message+"</font>");
	    out.println("<br /></center>");
	}

	//box it in 
	out.println("<center><table border align=center>");
	out.println("<tr bgcolor=#CDC9A3><td>");

	//the real table
	out.println("<form name=myForm method=post id=\"form_id\" "+
		    "onsubmit=\"return validateForm()\">");
	out.println("<input type=\"hidden\" name=\"pid\" value=\"" + se.getId() + 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"action2\" value=\"\" />");
	out.println("<input type=\"hidden\" id=\"location_id\" name=\"location_id\" value=\"" + se.getLocation_id() + "\" />");				
	out.println("<table>");
	//
	out.println("<tr><td align=right><strong>Program Title");
	out.println("</td></strong><td><left>");
	out.println(ptitle);
	out.println("&nbsp;&nbsp;<b>Season:</b>"+season+
		    "&nbsp;&nbsp;<b>Year:</b>"+pyear);
	out.println("</left></td></tr>");

	// sid, code
	out.println("<tr><td align=right><strong>Session ID");
	out.println("</td></strong><td><left>");
	out.println("<input type=text name=sid maxlength=3 size=3 "+
		    "value=\""+se.getSid()+"\" />");
	out.println("<strong>Code</strong>");
	out.println("<input type=text name=code maxlength=10 size=10 "+
		    "value=\""+se.getCode()+"\" />");
	out.println("</left></td></tr>");
	//
	// days
	if(sopt.showDays()){
	    out.println("<tr><td align=right valign=top><b>Day(s)");
	    out.println("</b></td>");
	    out.println("<td><table width=\"100%\"><tr><td align=left>");
	    out.println("<input type=checkbox name=d_sun value=y "+
			d_sun+">Su");
	    out.println("</td><td align=left>");
	    out.println("<input type=checkbox name=d_mon value=y "+
			d_mon+">M");
	    out.println("</td><td align=left>");
	    out.println("<input type=checkbox name=d_tue value=y "+
			d_tue+">Tu");
	    out.println("</td><td align=left>");
	    out.println("<input type=checkbox name=d_wed value=y "+
			d_wed+">W");
	    out.println("</td><td align=left>");
	    out.println("<input type=checkbox name=d_thu value=y "+
			d_thu+">Th");
	    out.println("</td><td align=left>");
	    out.println("<input type=checkbox name=d_fri value=y "+
			d_fri+">F");
	    out.println("</td></tr><tr><td align=left>");
	    out.println("<input type=checkbox name=d_sat value=y "+
			d_sat+">Sa");
	    out.println("</td><td> </td><td colspan=2 align=left>");
	    out.println("<input type=checkbox name=d_mon_fri value=y "+
			d_mon_fri+">M-F");
	    out.println("</td><td colspan=2 align=left>");
	    out.println("<input type=checkbox name=d_all value=y "+
			d_all+">M-Su");
	    out.println("</td></tr></table></td></tr>");
	    out.println("</td></tr>");
	}
	//
	// dates
	if(sopt.showStartDate()){
	    //
	    out.println("<tr><td align=right>");
	    out.println("<b>Start Date:</b>");
	    out.println("</td><left><td>");
	    out.println("<input type=text name=startDate value=\""+
			se.getStartDate() + "\" maxlength=10 size=10"+
			" id=\"startDate\" >");

	    out.println("<b>End Date:</b>");
	    out.println("<input type=text name=endDate value=\""+
			se.getEndDate() + "\" maxlength=10 size=10"+
			" id=\"endDate\" >");
	    out.println("</td></left></tr>");
	}
	// Time 
	if(sopt.showStartTime()){
	    out.println("<tr><td align=right><b>Start Time");
	    out.println("</b></td><td align=left>");
	    out.println("<input type=\"text\" name=\"startTime\" maxlength=\"15\" "+
			"value=\""+ se.getStartTime() + "\" "+
			"size=\"15\" readonly=\"readonly\" />");						
	    if(!newSession){
		out.println("<input type=\"button\" onclick='"+
			    "window.open(\""+url+"PickTime?id="+id+"&type=session&sid="+se.getSid()+"&wtime=startTime&time="+
			    java.net.URLEncoder.encode(se.getStartTime())+"\",\"Time\","+
			    "\"toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=300,left=300,"+
			    "resizable=1,width=400,height=250\");'"+
			    " value=\"Pick Time\">");
	    }
	    else{
		out.println("<input type=\"button\" onclick='"+
			    "window.open(\""+url+"PickTime?wtime=startTime&time="+
			    java.net.URLEncoder.encode(se.getStartTime())+"\",\"Time\","+
			    "\"toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=300,left=300,"+
			    "resizable=1,width=400,height=250\");'"+
			    " value=\"Pick Time\">");
	    }
	    out.println("<b>End Time:</b>");
	    out.println("<input type=\"text\" name=\"endTime\" maxlength=\"15\" "+
			"value=\""+ se.getEndTime() + "\" "+
			"size=\"15\" readonly=\"readonly\" />");						
	    if(!newSession){
		out.println("<input type=\"button\" onclick='"+
			    "window.open(\""+url+"PickTime?id="+id+"&type=session&sid="+se.getSid()+"&wtime=endTime&time="+
			    java.net.URLEncoder.encode(se.getEndTime())+"\",\"Time\","+
			    "\"toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=300,left=300,"+
			    "resizable=1,width=400,height=250\");'"+
			    " value=\"Pick Time\">");
	    }
	    else{
		out.println("<input type=\"button\" onclick='"+
			    "window.open(\""+url+"PickTime?wtime=endTime&time="+
			    java.net.URLEncoder.encode(se.getEndTime())+"\",\"Time\","+
			    "\"toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=300,left=300,"+
			    "resizable=1,width=400,height=250\");'"+
			    " value=\"Pick Time\">");
	    }						
	    out.println("</td></tr>");
	}
	// deadline
	if(sopt.showRegDeadLine()){
	    out.println("<tr><td align=right>");
	    out.println("<strong>Registration Deadline</strong>");
	    out.println("</td><td>");
	    out.println("<input type=text name=regDeadLine value=\""+ 
			se.getRegDeadLine() + "\" maxlength=10 size=10"+
			" id=\"regDeadLine\" >");
	    out.println("<font color=green size=-1>mm/dd/yyyy</font>");

	    out.println("</td></tr>");
	}
	//
	// in fee
	if(sopt.showInCityFee()){
	    out.println("<tr><td align=right><strong>In City Fee $</strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=inCityFee maxlength=20 size=20"+
			" value=\""+se.getInCityFee()+"\">");
	    out.println("</td></left></tr>");
	}	    
	//
	// non fee
	if(sopt.showNonCityFee()){
	    out.println("<tr><td align=right>");
	    out.println("<strong>Non City Fee $</strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=nonCityFee maxlength=20 "+
			"size=20 value=\""+se.getNonCityFee()+"\">");
	    out.println("</td></left></tr>");
	}
	//
	// other fee
	if(sopt.showOtherFee()){
	    out.println("<tr><td align=right>");
	    out.println("<strong>Other Fee $</strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=otherFee maxlength=20 size=20"+
			" value=\""+se.getOtherFee()+"\">");
	    out.println("</left></td></tr>");
	}
	if(sopt.showMemberFee()){
	    out.println("<tr><td align=right>");
	    out.println("<strong>Member Fee $</strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=memberFee maxlength=20 size=20"+
			" value=\""+se.getMemberFee()+"\">");
	    out.println("</left></td></tr>");
	}
	if(sopt.showNonMemberFee()){
	    out.println("<tr><td align=right>");
	    out.println("<strong>Non Member Fee $</strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=\"nonMemberFee\" maxlength=20 size=20"+
			" value=\""+se.getNonMemberFee()+"\">");
	    out.println("</left></td></tr>");
	}		
	//
	// location
	if(sopt.showLocation()){
	    out.println("<tr><td align=right>&nbsp;</td><td>");
	    out.println("<font color=\"green\" size=\"-1\"><i>To add/Edit a location start typing the location name then pick from the list </i></font></td></tr>");
	    out.println("<tr><td align=right><strong>Location</strong>");
	    out.println("</td><td align=\"left\">");
	    out.println("<input name=\"locationName\" id=\"locationName\" maxlength=\"80\" size=\"40\""+
			" value=\""+se.getLocationName()+"\" />");
	    out.println("</td></tr>");
						
	}
	//
	// age group
	if(sopt.showAllAge()){
	    out.println("<tr><td align=right><b>Participation age, From:"+
			"</b>");
	    out.println("</td><td>");
	    out.println("<input type=text name=ageFrom maxlength=3 size=3 "+
			"value=\""+se.getAgeFrom()+"\">"); 
	    out.println("<b> To:"+
			"</b>");
	    out.println("<input type=text name=ageTo maxlength=3 size=3 "+
			"value=\""+se.getAgeTo()+"\"> or "); 
	    out.println("<input type=checkbox name=allAge value=y "+
			allAge +"> <b>All Ages welcome </b> &nbsp;&nbsp;");
	    out.println("<input type=checkbox name=wParent value=y "+
			wParent +"> <b>w/Parent</b>");
	    out.println("</td></tr>");
	    out.println("<tr><td align=right><b>Other age:</b>");
	    out.println("</td><td>");
	    out.println("<input type=text name=otherAge maxlength=30 size=20 "+
			"value='"+se.getOtherAge()+"'>"); 
	    out.println("</left></td></tr>");
	}
	//    
	// grade
	if(sopt.showPartGrade()){
	    out.println("<tr><td align=right>");
	    out.println("<strong>Participant Grade</strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=partGrade maxlength=15 "+
			"size=15 value=\"" + se.getPartGrade() + "\">"); 
	    out.println("</left></td></tr>");
	}
	//
	// min-max enroll
	if(sopt.showMinMaxEnroll()){
	    out.println("<tr><td align=right><strong>Min-Max Enrollment"+
			"</strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=minMaxEnroll maxlength=10 "+
			"size=10 value=\""+se.getMinMaxEnroll()+"\">"); 
	    out.println("</left></td></tr>");
	}
	//
	// # classes
	if(sopt.showClassCount()){
	    out.println("<tr><td align=right>");
	    out.println("<strong># of Classes </strong>");
	    out.println("</td><td><left>");
	    out.println("<input type=text name=classCount maxlength=10 "+
			"size=10 value=\""+se.getClassCount()+"\">"); 
	    out.println("</left></td></tr>");
	}
	//
	// Description
	if(sopt.showDescription()){
	    out.println("<tr><td align=right valign=top><b>Description</b>");
	    out.println("</td><td>Not more than 1000 characters<br />");
	    out.println("<textarea name='description' wrap rows='10' "+
			"cols='80'>");
	    out.println(se.getDescription());
	    out.println("</textarea>");
	    out.println("</td></tr>");
	}
	//
	// Instructor
	if(sopt.showInstructor()){
	    out.println("<tr><td align=right><strong>Instructor</strong>");
	    out.println("</td><td>");
	    out.println("<input type=text name=instructor "+
			"maxlength=120 size=50"+
			" value=\""+se.getInstructor()+"\">");
	    out.println("</td></tr>");
	}
	out.println("<tr><td align=right><b>Sort Sessions by:</b></td><td colspan=2>");
	out.println("<select name=sessionSort>");
	for(int i=0;i<Helper.sessionSortOpt.length;i++){
	    if(sessionSort.equals(Helper.sessionSortOpt[i]))
		out.println("<option selected value='"+
			    Helper.sessionSortOpt[i]+"'>"+
			    Helper.sessionSortArr[i]);
	    else
		out.println("<option value='"+
			    Helper.sessionSortOpt[i]+"'>"+
			    Helper.sessionSortArr[i]);
	}
	out.println("</select></td></tr>");
	//
	if(action.equals("") || action.startsWith("Start") || 
	   action.equals("Delete")){
	    out.println("<tr><td colspan=2 align=right>");
	    if(user.canEdit()){
		out.println("<input type=submit "+
			    "name=action value=Save>");
	    }
	    out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			"&nbsp;&nbsp;&nbsp;<input type=reset "+
			" value=\"Clear\"></td></tr>"); 
	}
	else{
	    out.println("<tr><td>&nbsp;</td><td><table width\"100%\"><tr>");
	    if(user.canEdit()){
		out.println("<td width=\"30%\"><input type=submit "+
			    "name=action value=Update /> "+
			    "</td>");
	    }
	    out.println("<td width=\"30%\"><input type=submit "+
			"name=action value=\"Start New\" />"+
			"</td>");
	    //
	    if(user.canDelete()){
		out.println("<td align=right>");
		out.println("<button onclick=\"return validateDelete();\">Delete</button>");
		out.println("</td>");	
	    }
	    out.println("</tr></table></td></tr>");
	}
	out.println("</table></center>");
	out.println("</td></tr></table>");
	out.println("</form>");
	//
	// put all the old sessions in one table
	//
	SessionList list = new SessionList(debug, id);
	back = list.lookFor();
	if(back.equals("")){
	    if(list != null && list.size() > 0){
		Helper.writeSessions(out, list, sopt, id,
				     url, debug);
	    }
	}
	out.println("<HR>");
	out.println("<LI><A href="+url+
		    "Program.do?id="+se.getId()+
		    "&action=zoom>Go to the Related Program </A>");
	out.println("<LI><A href="+url+"Logout?"+
		    " target=_parent>Log Out </A>");
	Helper.writeWebFooter(out, url);
	String dateStr = "{ nextText: \"Next\",prevText:\"Prev\", buttonText: \"Pick Date\", showOn: \"both\", navigationAsDateFormat: true, buttonImage: \""+url+"js/calendar.gif\"}";
	out.println("<script>");
	if(sopt.showStartDate()){		
	    out.println("  $( \"#startDate\" ).datepicker("+dateStr+"); ");
	    out.println("  $( \"#endDate\" ).datepicker("+dateStr+"); ");
	}
	if(sopt.showRegDeadLine()){
	    out.println("  $( \"#regDeadLine\" ).datepicker("+dateStr+"); ");
	}
	out.println("	$(\"#locationName\").autocomplete({ ");
	out.println("		source: '"+url+"LocationService?format=json', ");
	out.println("		minLength: 2, ");
	out.println("		select: function( event, ui ) { ");
	out.println("			if(ui.item){ ");
	out.println("				$(\"#location_id\").val(ui.item.id); ");
	out.println("			} ");
	out.println("		}  ");
	out.println("	}); ");
	out.println("$(function() { ");
	out.println("$('#form_id').areYouSure(); ");
	out.println("}); ");							
	out.println("</script>");			
	out.print("</body></html>");
	out.close();

    }

}






















































