package be.hesest.tfe.views;

import be.hesest.tfe.entities.MarketEntity;
import be.hesest.tfe.entities.UserEntity;
import be.hesest.tfe.repositories.UserRepository;
import be.hesest.tfe.units.TransportModeUnit;
import be.hesest.tfe.utils.ConverterUtil;
import be.hesest.tfe.utils.CookiesUtil;
import be.hesest.tfe.utils.ItineraryUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Route("settings")
@PageTitle("Paramètres")
public class SettingsView extends VerticalLayout implements BeforeEnterObserver {

    private UserRepository userRepository;

    @Autowired
    public SettingsView(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Cookie emailCookie = CookiesUtil.getCookie("email");
        Cookie passwordCookie = CookiesUtil.getCookie("password");
        if (emailCookie != null && passwordCookie != null) {
            UserEntity user = userRepository.findByEmailAndPassword(emailCookie.getValue(), passwordCookie.getValue());
            if (user != null) {
                // Rediriger sur la page
                init(user);
            } else {
                // Supprimer les cookies qui ne fonctionnent plus
                CookiesUtil.clearCookies("email", "password");
                // Rediriger sur la page de connexion
                event.forwardTo(SignInView.class);
            }
        } else {
            // Rediriger sur la page de connexion
            event.forwardTo(SignInView.class);
        }
    }

    private void init(UserEntity user) {
        // Créer un conteneur horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        // Ajouter le titre H2 à gauche
        H2 title = new H2("SuperShopper");

        // Créer un conteneur horizontal pour le texte et les boutons
        HorizontalLayout textAndButtonsLayout = new HorizontalLayout();
        textAndButtonsLayout.setWidthFull();
        textAndButtonsLayout.setAlignItems(Alignment.CENTER);
        textAndButtonsLayout.setJustifyContentMode(JustifyContentMode.END);

        // Ajouter le texte
        Label textLabel = new Label("Bonjour, " + user.getName() + " !");

        // Ajouter le bouton "Paramètres"
        Button homeButton = new Button("Accueil");

        // Ajouter une écoute pour le clic sur le bouton
        homeButton.addClickListener(event -> UI.getCurrent().navigate(HomeView.class));

        // Ajouter le bouton "Déconnexion"
        Button logoutButton = new Button("Déconnexion");

        // Ajouter une écoute pour le clic sur le bouton
        logoutButton.addClickListener(event -> {
            // Supprimer les cookies
            CookiesUtil.clearCookies("email", "password");
            // Rediriger sur la page de connexion
            UI.getCurrent().getPage().reload();
        });

        // Ajouter les éléments à la vue
        textAndButtonsLayout.add(textLabel, homeButton, logoutButton);
        horizontalLayout.add(title, textAndButtonsLayout);

        // Ajouter un espace entre le titre et le texte/bouton
        horizontalLayout.setFlexGrow(1, title);

        // Texte pour le départ
        Label startLabel = new Label("Votre point de départ");

        // Création des champs de texte pour le départ
        TextField startStreet = new TextField("Rue");
        startStreet.setValue(user.getStartAddressStreet());
        TextField startNumber = new TextField("Numéro");
        startNumber.setWidth("100px");
        startNumber.setValue(user.getStartAddressNumber());
        HorizontalLayout startFirstRow = new HorizontalLayout(startStreet, startNumber);
        TextField startPostalCode = new TextField("Code postal");
        startPostalCode.setWidth("100px");
        startPostalCode.setValue(user.getStartAddressPostalCode());
        TextField startCity = new TextField("Ville");
        startCity.setValue(user.getStartAddressCity());
        HorizontalLayout startSecondRow = new HorizontalLayout(startPostalCode, startCity);

        // Texte pour le maximum
        Label endLabel = new Label("Votre point maximum");

        // Création des champs de texte pour le départ
        TextField endStreet = new TextField("Rue");
        endStreet.setValue(user.getEndAddressStreet());
        TextField endNumber = new TextField("Numéro");
        endNumber.setWidth("100px");
        endNumber.setValue(user.getEndAddressNumber());
        HorizontalLayout endFirstRow = new HorizontalLayout(endStreet, endNumber);
        TextField endPostalCode = new TextField("Code postal");
        endPostalCode.setWidth("100px");
        endPostalCode.setValue(user.getEndAddressPostalCode());
        TextField endCity = new TextField("Ville");
        endCity.setValue(user.getEndAddressCity());
        HorizontalLayout endSecondRow = new HorizontalLayout(endPostalCode, endCity);

        // Texte pour le mode de transport
        Label transportLabel = new Label("Votre mode de transport");

        // Choix du mode de transport
        RadioButtonGroup<String> transportModeRadio = new RadioButtonGroup<>("Sélectionnez un mode de transport");
        transportModeRadio.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        transportModeRadio.setItems(Arrays.stream(TransportModeUnit.values()).map(TransportModeUnit::getName).toList());
        transportModeRadio.setValue(user.getTransportMode().getName());

        // Ajouter le bouton "Sauvegarder"
        Button saveButton = new Button("Sauvegarder");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Ajouter une écoute pour le clic sur le bouton
        saveButton.addClickListener(event -> {
            List<Double> startCoordinate = ItineraryUtil.getCoordinates(startStreet.getValue() + " " + startNumber.getValue() + " " + startCity.getValue() + " " + startPostalCode.getValue() + " " + user.getStartAddressCountry());
            List<Double> endCoordinate = ItineraryUtil.getCoordinates(endStreet.getValue() + " " + endNumber.getValue() + " " + endCity.getValue() + " " + endPostalCode.getValue() + " " + user.getEndAddressCountry());
            if (!startCoordinate.isEmpty() && !endCoordinate.isEmpty()) {
                TransportModeUnit transportMode = TransportModeUnit.findByName(transportModeRadio.getValue());
                List<MarketEntity> markets = ItineraryUtil.findMarkets(transportMode.getName(), startCoordinate.get(0), startCoordinate.get(1), endCoordinate.get(0), endCoordinate.get(1));
                user.setAvailableMarkets(ConverterUtil.convertListToString(markets.stream().map(MarketEntity::getId).toList()));
                user.setStartAddressStreet(startStreet.getValue());
                user.setStartAddressNumber(startNumber.getValue());
                user.setStartAddressPostalCode(startPostalCode.getValue());
                user.setStartAddressCity(startCity.getValue());
                user.setStartAddressLatitude(startCoordinate.get(0));
                user.setStartAddressLongitude(startCoordinate.get(1));
                user.setEndAddressStreet(endStreet.getValue());
                user.setEndAddressNumber(endNumber.getValue());
                user.setEndAddressPostalCode(endPostalCode.getValue());
                user.setEndAddressCity(endCity.getValue());
                user.setEndAddressLatitude(endCoordinate.get(0));
                user.setEndAddressLongitude(endCoordinate.get(1));
                user.setTransportMode(transportMode);
                userRepository.save(user);
                Notification.show("Paramètres sauvegardés avec succès");
            } else {
                Notification.show("Les adresses sont erronées");
            }
        });

        // Ajouter les éléments à la vue
        add(horizontalLayout, startLabel, startFirstRow, startSecondRow, endLabel, endFirstRow, endSecondRow, transportLabel, transportModeRadio, saveButton);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

}