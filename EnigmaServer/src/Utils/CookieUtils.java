package Utils;

import Constants.ServerConstants;
import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import java.util.List;

public class CookieUtils {

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
                if(allyComp.getUsername().equals(allyName)){
                    ally = allyComp;
                    break;
                }
            }
        }
        return ally;
    }
}
