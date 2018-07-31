package Utils;

import Constants.ServerConstants;
import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;
import EnigmaCompetition.Ubout;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

public class CookieUtils {

    private static List<Ally> allyList;

    public static Competition getCompetitionFromCookie(Cookie[] cookies, ServletContext context) {
        Competition competition = (Competition) context.getAttribute(getUserCookie(cookies).getValue());
        return competition;
    }

    public static void setCompetitionFromCookie(Competition competition, Cookie[] cookies, ServletContext context) {
        context.setAttribute(getUserCookie(cookies).getValue(), competition);
    }

    public static Cookie getUserCookie(Cookie[] cookies) {
        Cookie userCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ServerConstants.userName)) {
                userCookie = cookie;
            }

        }
        return userCookie;
    }

    public static Ally getAllyFromUserName(Cookie userCookie, ServletContext servletContext) {
        String allyName = userCookie.getValue();
        Ally ally = null;
        List<Competition> competitions = (List<Competition>) servletContext.getAttribute("competitions");
        for (Competition competition : competitions) {
            for (Ally allyComp : competition.getAllies()) {
                if (allyComp.getUsername().equals(allyName)) {
                    ally = allyComp;
                    break;
                }
            }
        }
        return ally;
    }


    public static Ally getAllyFromUserName(String username, ServletContext servletContext) {
        Ally ally = null;
        List<Ally> allies = getAllyList(servletContext);
        for (Ally iter : allies) {
            if (iter.getUsername().equals(username)) {
                ally = iter;
                break;
            }
        }
        return ally;
    }


    public static List<Ally> getAllyList(ServletContext servletContext) {
        List<Ally> allies = (ArrayList) servletContext.getAttribute("allies");
        if (allies == null) {
            allies = new ArrayList<>();
            servletContext.setAttribute("allies", allies);
        }
        return allies;
    }

    public static void removeCompetitionCookie(Ubout uboat, ServletContext servletContext) {
        servletContext.removeAttribute(uboat.getUsername());
    }
}
