import EnigmaCompetition.Competition;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/openCompetitions")
public class openCompetitionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Competition> competitions = (List<Competition>) getServletContext().getAttribute("competitions");
        if(competitions == null){
            resp.getOutputStream().print("error no open competitions");
        }
        else {
            JsonObject jsonObject = new JsonObject();
            int indexKey = 1;
            for (Competition competition : competitions) {
                jsonObject.addProperty(String.valueOf(indexKey), competition.getUboat().getUsername());
                indexKey++;
            }
            resp.getOutputStream().print(jsonObject.toString());
        }
    }
}
