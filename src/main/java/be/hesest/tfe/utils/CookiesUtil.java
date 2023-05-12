package be.hesest.tfe.utils;

import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

public class CookiesUtil {

    public static void setCookies(String email, String password) {
        // Créer un cookie pour stocker l'email de l'utilisateur
        Cookie emailCookie = new Cookie("email", email);
        // Définir l'âge maximum du cookie à 1 semaine
        emailCookie.setMaxAge(604800);
        // Définir le chemin du cookie
        emailCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        // Créer un cookie pour stocker le mot de passe crypté de l'utilisateur
        Cookie passwordCookie = new Cookie("password", password);
        // Définir l'âge maximum du cookie à 1 semaine
        passwordCookie.setMaxAge(604800);
        // Définir le chemin du cookie
        passwordCookie.setPath(VaadinService.getCurrentRequest().getContextPath());

        // Ajouter le cookie à la réponse HTTP
        VaadinResponse http = VaadinService.getCurrentResponse();
        http.addCookie(emailCookie);
        http.addCookie(passwordCookie);
    }

    public static Cookie getCookie(String name) {
        // Récupérer tous les cookies
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Trouver le cookie
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        return null;
    }

    public static void clearCookies(String... names) {
        // Récupérer tous les cookies
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Supprimer les cookies
        for (String name : names) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setMaxAge(0);
                    cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
                    VaadinService.getCurrentResponse().addCookie(cookie);
                }
            }
        }
    }

}