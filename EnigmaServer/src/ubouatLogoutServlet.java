import EnigmaCompetition.Competition;
import Utils.CookieUtils;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/uboutLogout")
public class ubouatLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Competition competition = CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        CookieUtils.removeCompetitionCookie(competition.getUboat(), getServletContext());
        List<Competition> competitionList = (List<Competition>)getServletContext().getAttribute("competitions");
        competitionList.remove(competition);
        resp.sendRedirect("index.html");
    }
}

