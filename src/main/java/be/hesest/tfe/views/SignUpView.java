package be.hesest.tfe.views;

import be.hesest.tfe.entities.UserEntity;
import be.hesest.tfe.repositories.UserRepository;
import be.hesest.tfe.units.TransportModeUnit;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;

@Route("signup")
@PageTitle("Inscription")
public class SignUpView extends VerticalLayout implements BeforeEnterObserver {

    private UserRepository userRepository;

    @Autowired
    public SignUpView(UserRepository userRepository) {
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
        H2 titleLabel = new H2("Inscription");

        // Créer un champ de saisie pour le nom
        TextField nameField = new TextField("Nom");
        nameField.setWidth("300px");

        // Créer un champ de saisie pour l'e-mail
        EmailField emailField = new EmailField("E-mail");
        emailField.setWidth("300px");

        // Créer un champ de saisie pour le mot de passe
        PasswordField passwordField = new PasswordField("Mot de passe");
        passwordField.setWidth("300px");

        // Créer un bouton pour soumettre le formulaire
        Button submitButton = new Button("S'inscrire");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Ajouter une écoute pour le clic sur le bouton
        submitButton.addClickListener(event -> {
            // Validation de l'email
            if (emailField.getValue().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
                if (!userRepository.existsByEmail(emailField.getValue())) {
                    // Cryptage du mot de passe
                    try {
                        String password = CryptionUtil.encrypt(passwordField.getValue());
                        // Créer l'utilisateur
                        UserEntity user = new UserEntity(emailField.getValue(), password, nameField.getValue(), System.currentTimeMillis(), "", "", "", "", "", "", "Belgique", null, null, "", "", "", "", "Belgique", null, null, TransportModeUnit.DRIVING_CAR);
                        userRepository.save(user);
                        // Définir les cookies
                        CookiesUtil.setCookies(user.getEmail(), user.getPassword());
                        Notification.show("Vous avez été connecté avec succès");
                        UI.getCurrent().getPage().reload();
                    } catch (NoSuchAlgorithmException e) {
                        Notification.show("Une erreur s'est produite");
                    }
                } else {
                    Notification.show("Cet email existe déjà");
                }
            } else {
                Notification.show("Votre email n'est pas valide");
            }
        });

        // Créer un bouton pour se connecter
        Button signInButton = new Button("Se connecter");
        signInButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Ajouter une écoute pour le clic sur le bouton
        signInButton.addClickListener(event -> UI.getCurrent().navigate(SignInView.class));

        // Ajouter les éléments à la vue
        add(titleLabel, nameField, emailField, passwordField, submitButton, signInButton);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

}