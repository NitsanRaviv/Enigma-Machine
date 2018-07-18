package Utils;

import Constants.ServerConstants;
import EnigmaCompetition.Competition;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

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
}
