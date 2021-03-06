import EnigmaCompetition.Competition;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/preCompetition")
public class preCompetitionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setInitialCode(req);
        setStringToDecrypt(req);
        setGameLevel(req);
        resp.sendRedirect("/EnigmaServer/theGameUboat.html");
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        addCompetitionToList(competition);
    }

    private void setInitialCode(HttpServletRequest req) {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        String[] rotorIds = req.getParameter("rotorNumbers").split(",");
        String[] initialLoc = req.getParameter("initialLocation").split(",");
        String chosenRefl = req.getParameter("reflector");
        competition.getIntegrator().setInitialCode(rotorIds, initialLoc, chosenRefl);
    }

    private void setStringToDecrypt(HttpServletRequest req) {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        String stringToEncrypt = req.getParameter("stringToProcess").toUpperCase();
        competition.setDecryptedString(stringToEncrypt.toUpperCase());
        competition.setEncryptedString(competition.getIntegrator().getMachine().encryptCodeToString(stringToEncrypt.toUpperCase()));
    }

    private void setGameLevel(HttpServletRequest req) {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        String gameLevel = competition.getBattlefield().getLevel();
        //TODO::get competition from string - easy, medium etc.
        competition.setTaskLevel(Utils.generalUtils.getLevelFromString(gameLevel));
    }

    private void addCompetitionToList(Competition competition) {
        List<Competition> competitionList =(ArrayList)getServletContext().getAttribute("competitions");
        if(competitionList == null)
            competitionList = new ArrayList<>();
        competitionList.add(competition);
        getServletContext().setAttribute("competitions", competitionList);
    }
}
