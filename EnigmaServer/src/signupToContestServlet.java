
import EnigmaCompetition.Competition;
import EnigmaCompetition.Ubout;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import javax.servlet.ServletContext;

@WebServlet("/signUp")
public class signupToContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uboutName = req.getParameter("uname");
        Ubout ubout = new Ubout(uboutName);
        Competition competition = new Competition();
        competition.setUboat(ubout);
        getServletContext().setAttribute(uboutName, competition);
        req.getSession().setAttribute("user", "roy");
        resp.addCookie(req.getCookies()[0]);
        resp.sendRedirect("/signUpToContest.html");
    }
}
