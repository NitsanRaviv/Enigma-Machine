
import EnigmaCompetition.Competition;
import EnigmaCompetition.Ubout;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletContext;

@WebServlet("/signUp")
public class signUpServlet extends HttpServlet {
    private StringBuilder proposal;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (checkAvailableUserName(req.getParameter("uname")) == true) {
            resp.addCookie(new Cookie("uname", req.getParameter("uname")));
            resp.sendRedirect("/EnigmaServer/signUpToContest.html");
        } else {
            resp.getOutputStream().print(proposal.toString());
            resp.getWriter().write(proposal.toString());
            resp.sendRedirect("/index.html");
        }
    }

    private boolean checkAvailableUserName(String uname) {
        Set<String> unames = (Set<String>) getServletContext().getAttribute("unames");
        boolean available = true;
        if (unames == null) {
            unames = new HashSet();
            unames.add(uname);
            getServletContext().setAttribute("unames", unames);
            available = true;
        } else {
            if (unames.contains(uname)) {
                proposal = new StringBuilder();
                proposal.append(uname);
                while (unames.contains(proposal.toString()))
                    proposal.append(new Random().nextInt() % 9);
                available = false;
                StringBuilder sb = createDefaultMessage(uname);
                sb.append(proposal);
                proposal = sb;
                proposal.append("\nplease go back to index page and re-sign.\n\n*the-nice-bonus was implemented late at night, and Big Lebowski is a great movie!");
            }

            else {
                unames.add(uname);
                available = true;
            }
        }
        return available;
    }

    private StringBuilder createDefaultMessage(String uname) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hey, the username: ");
        sb.append(uname);
        sb.append(" is taken.\nwe have a nice proposition for you, which is:\n");
        return sb;
    }
}
