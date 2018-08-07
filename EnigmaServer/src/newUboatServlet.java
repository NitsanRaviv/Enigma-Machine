import EnigmaCompetition.Competition;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/newUboatSignIn")
public class newUboatServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO:: save user name
        //need to change this flow - competition is created only when a uboat is signed in
        Competition competition = new Competition();
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
        //addCompetitionToList(competition);
        resp.sendRedirect("/createCompetition.html");
    }
}
