
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
        //TODO:: save user name
        //need to change this flow - competition is created only when a uboat is signed in
        getServletContext().setAttribute(req.getParameter("uname"), new Competition());
        //open a session to know who is the username
        resp.sendRedirect("/signUpToContest.html");
    }
}
