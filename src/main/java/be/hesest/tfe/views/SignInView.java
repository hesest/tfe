package be.hesest.tfe.views;

import be.hesest.tfe.entities.UserEntity;
import be.hesest.tfe.repositories.UserRepository;
import be.hesest.tfe.utils.CookiesUtil;
import be.hesest.tfe.utils.CryptionUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;

@Route("signin")
@PageTitle("Connexion")
public class SignInView extends VerticalLayout implements BeforeEnterObserver {

    private UserRepository userRepository;

    @Autowired
    public SignInView(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Cookie emailCookie = CookiesUtil.getCookie("email");
        Cookie passwordCookie = CookiesUtil.getCookie("password");
        if (emailCookie != null && passwordCookie != null) {
            UserEntity user = userRepository.findByEmailAndPassword(emailCookie.getValue(), passwordCookie.getValue());
            if (user != null) {
                // Rediriger sur la page d'accueil
                event.forwardTo(HomeView.class);
            } else {
                // Supprimer les cookies qui ne fonctionnent plus
                CookiesUtil.clearCookies("email", "password");
                // Rediriger sur la page
                init();
            }
        } else {
            // Rediriger sur la page
            init();
        }
    }

    private void init() {
        // Ajouter le titre de la page
        H2 titleLabel = new H2("Connexion");

        // Créer un champ de saisie pour l'e-mail
        EmailField emailField = new EmailField("E-mail");
        emailField.setWidth("300px");

        // Créer un champ de saisie pour le nom
        PasswordField passwordField = new PasswordField("Mot de passe");
        passwordField.setWidth("300px");

        // Créer un bouton pour soumettre le formulaire
        Button submitButton = new Button("Se connecter");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Ajouter une écoute pour le clic sur le bouton
        submitButton.addClickListener(event -> {
            // Validation de l'email
            if (emailField.getValue().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
                // Cryptage du mot de passe
                try {
                    String password = CryptionUtil.encrypt(passwordField.getValue());
                    // Récupérer l'utilisateur
                    UserEntity user = userRepository.findByEmailAndPassword(emailField.getValue(), password);
                    if (user != null) {
                        // Définir les cookies
                        CookiesUtil.setCookies(user.getEmail(), user.getPassword());
                        Notification.show("Vous avez été connecté avec succès");
                        UI.getCurrent().getPage().reload();
                    } else {
                        Notification.show("L'email ou le mot de passe est erroné");
                    }
                } catch (NoSuchAlgorithmException e) {
                    Notification.show("Une erreur s'est produite");
                }
            } else {
                Notification.show("Votre email n'est pas valide");
            }
        });

        // Créer un bouton pour s'inscrire
        Button signUpButton = new Button("S'inscrire");
        signUpButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Ajouter une écoute pour le clic sur le bouton
        signUpButton.addClickListener(event -> UI.getCurrent().navigate(SignUpView.class));

        // Ajouter les éléments à la vue
        add(titleLabel, emailField, passwordField, submitButton, signUpButton);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

}