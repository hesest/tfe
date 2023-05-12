package be.hesest.tfe.views;

import be.hesest.tfe.entities.MarketEntity;
import be.hesest.tfe.entities.ProductEntity;
import be.hesest.tfe.entities.UserEntity;
import be.hesest.tfe.managers.MarketsManager;
import be.hesest.tfe.managers.PricesManager;
import be.hesest.tfe.managers.ProductsManager;
import be.hesest.tfe.models.PriceModel;
import be.hesest.tfe.repositories.UserRepository;
import be.hesest.tfe.units.ProductTypeUnit;
import be.hesest.tfe.utils.ConverterUtil;
import be.hesest.tfe.utils.CookiesUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Route("")
@PageTitle("Accueil")
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

    private UserRepository userRepository;

    @Autowired
    public HomeView(UserRepository userRepository) {
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
        Button settingsButton = new Button("Paramètres");

        // Ajouter une écoute pour le clic sur le bouton
        settingsButton.addClickListener(event -> UI.getCurrent().navigate(SettingsView.class));

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
        textAndButtonsLayout.add(textLabel, settingsButton, logoutButton);
        horizontalLayout.add(title, textAndButtonsLayout);

        // Ajouter un espace entre le titre et le texte/bouton
        horizontalLayout.setFlexGrow(1, title);

        // Conteneur principal
        HorizontalLayout main = new HorizontalLayout();
        main.setSpacing(true);

        // Conteneur de gauche
        VerticalLayout first = new VerticalLayout();
        first.setSpacing(true);

        // Texte 1
        Label label1 = new Label("Assortiment");
        first.add(label1);

        // Boîte 1
        Div box1 = new Div();
        box1.getStyle().set("padding", "10px"); // Ajouter un espacement entre les éléments
        box1.getStyle().set("overflow-y", "auto"); // Ajouter le défilement vertical
        box1.getStyle().set("width", "40vw"); // Utilise toute la largeur de l'écran
        box1.getStyle().set("height", "37.5vw"); // Utilise toute la longeur de l'écran
        box1.getStyle().set("border-radius", "10px"); // Bordures arondies
        box1.getStyle().set("border", "1px solid #cccccc"); // Bordures grises

        Map<String, List<ProductEntity>> productsByName = new HashMap<>();
        for (ProductEntity product : ProductsManager.getProducts().values()) {
            if (!productsByName.containsKey(product.getName())) {
                productsByName.put(product.getName(), new ArrayList<>());
            }
            productsByName.get(product.getName()).add(product);
        }

        Grid<ProductEntity> grid1 = new Grid<>(ProductEntity.class, false);
        grid1.addComponentColumn(product -> {
            Image image = new Image("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(product.getImage()), product.getName());
            image.setHeight("50px");
            return image;
        }).setTextAlign(ColumnTextAlign.START).setAutoWidth(true);
        grid1.addColumn(ProductEntity::getName).setAutoWidth(true);
        grid1.addComponentColumn(product -> {
            List<PriceModel> productModels = new ArrayList<>();
            for (ProductEntity p : productsByName.get(product.getName())) {
                productModels.add(PricesManager.getProductPrice(p));
            }
            Button button = new Button(VaadinIcon.PLUS.create());
            button.addClickListener(event -> {
                ViewProductDialog viewProductDialog = new ViewProductDialog(user, productsByName.get(product.getName()), productModels);
                viewProductDialog.open();
            });
            return button;
        }).setTextAlign(ColumnTextAlign.END).setAutoWidth(true);
        grid1.setSelectionMode(Grid.SelectionMode.NONE);
        grid1.getStyle().set("border", "none");
        grid1.setSizeFull();

        List<ProductEntity> products = new ArrayList<>();
        for (List<ProductEntity> product : productsByName.values()) {
            products.add(product.get(0));
        }

        GridListDataView<ProductEntity> dataView1 = grid1.setItems(products);

        TextField searchField1 = new TextField();
        searchField1.setWidthFull();
        searchField1.setPlaceholder("Recherche...");
        searchField1.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField1.setValueChangeMode(ValueChangeMode.EAGER);
        searchField1.addValueChangeListener(e -> dataView1.refreshAll());

        dataView1.addFilter(product -> {
            String searchTerm = searchField1.getValue().trim();
            if (!searchTerm.isEmpty()) {
                return product.getName().toLowerCase().contains(searchTerm.toLowerCase());
            }
            return true;
        });

        // Ajouter les éléments à la vue
        box1.add(searchField1, grid1);
        first.add(box1);
        main.add(first);

        // Conteneur de droite
        VerticalLayout second = new VerticalLayout();
        second.setSpacing(true);

        // Texte 2
        Label label2 = new Label("Liste de courses");
        second.add(label2);

        // Boîte 2
        Div box2 = new Div();
        box2.getStyle().set("padding", "10px"); // Ajouter un espacement entre les éléments
        box2.getStyle().set("overflow-y", "auto"); // Ajouter le défilement vertical
        box2.getStyle().set("width", "40vw"); // Utilise toute la largeur de l'écran
        box2.getStyle().set("height", "37.5vw"); // Utilise toute la longeur de l'écran
        box2.getStyle().set("border-radius", "10px"); // Bordures arondies
        box2.getStyle().set("border", "1px solid #cccccc"); // Bordures grises

        Grid<Map.Entry<PriceModel, Integer>> grid2 = new Grid<>();
        grid2.addComponentColumn(price -> {
            Image image = new Image("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(price.getKey().getProduct().getImage()), price.getKey().getProduct().getName());
            image.setHeight("50px");
            return image;
        }).setTextAlign(ColumnTextAlign.START).setAutoWidth(true);
        grid2.addColumn(price -> price.getKey().getProduct().getName()).setAutoWidth(true);
        grid2.addColumn(price -> price.getKey().getProduct().getMarketName()).setAutoWidth(true);
        grid2.addColumn(Map.Entry::getValue).setAutoWidth(true);
        grid2.addColumn(price -> String.valueOf(price.getKey().getPrice()).replaceAll("\\.", ",") + " €").setAutoWidth(true);
        grid2.addComponentColumn(price -> {
            HorizontalLayout layout = new HorizontalLayout();
            Icon checkIcon = VaadinIcon.CHECK.create();
            checkIcon.setColor("green");
            Button checkButton = new Button(checkIcon);
            checkButton.addClickListener(event -> {});
            Icon trashIcon = VaadinIcon.TRASH.create();
            trashIcon.setColor("red");
            Button trashButton = new Button(trashIcon);
            trashButton.addClickListener(event -> {});
            layout.add(checkButton, trashButton);
            layout.setJustifyContentMode(JustifyContentMode.END);
            return layout;
        }).setTextAlign(ColumnTextAlign.END).setAutoWidth(true);
        grid2.setSelectionMode(Grid.SelectionMode.NONE);
        grid2.getStyle().set("border", "none");
        grid2.setSizeFull();

        Map<PriceModel, Integer> prices = new HashMap<>();
        Map<String, Integer> shoppingList = ConverterUtil.convertStringToMap(user.getShoppingList());
        for (Map.Entry<String, Integer> entry : shoppingList.entrySet()) {
            if (ProductsManager.getProducts().containsKey(entry.getKey())) {
                ProductEntity product = ProductsManager.getProducts().get(entry.getKey());
                prices.put(PricesManager.getProductPrice(product), entry.getValue());
            }
        }

        GridListDataView<Map.Entry<PriceModel, Integer>> dataView2 = grid2.setItems(prices.entrySet());

        TextField searchField2 = new TextField();
        searchField2.setWidthFull();
        searchField2.setPlaceholder("Recherche...");
        searchField2.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField2.setValueChangeMode(ValueChangeMode.EAGER);
        searchField2.addValueChangeListener(e -> dataView2.refreshAll());

        dataView2.addFilter(price -> {
            String searchTerm = searchField1.getValue().trim();
            if (!searchTerm.isEmpty()) {
                return price.getKey().getProduct().getName().toLowerCase().contains(searchTerm.toLowerCase());
            }
            return true;
        });

        // Ajouter les éléments à la vue
        box2.add(searchField2, grid2);
        second.add(box2);
        main.add(second);

        Button itineraryButton = new Button("Générer l'itinéraire");
        itineraryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        itineraryButton.addClickListener(event -> {
            List<MarketEntity> markets = new ArrayList<>();
            List<String> marketsId = ConverterUtil.convertStringToList(user.getAvailableMarkets());
            for (PriceModel price : prices.keySet()) {
                for (String marketId : marketsId) {
                    if (MarketsManager.getMarkets().containsKey(marketId) && MarketsManager.getMarkets().get(marketId).getName().equals(price.getProduct().getMarketName())) {
                        MarketEntity market = MarketsManager.getMarkets().get(marketId);
                        if (!markets.contains(market)) {
                            markets.add(market);
                        }
                    }
                }
            }
            for (MarketEntity market : markets) {
                System.out.println("Magasin : " + market.getName() + " (" + market.getAddressLatitude() + "," + market.getAddressLongitude() + ")");
            }
            UI.getCurrent().getPage().open("https://www.google.com/maps/dir/'" + user.getStartAddressLatitude() + "," + user.getStartAddressLongitude() + "'/'" + markets.get(0).getAddressLatitude() + "," + markets.get(0).getAddressLongitude() + "'", "_blank");
        });

        // Ajouter les éléments à la vue
        add(horizontalLayout, main, itineraryButton);
        setAlignItems(Alignment.CENTER); // Centrer les boîtes verticalement
    }

    public class ViewProductDialog extends Dialog {

        public ViewProductDialog(UserEntity user, List<ProductEntity> products, List<PriceModel> prices) {
            // Créer un layout vertical pour afficher l'image, le texte et le tableau en dessous
            VerticalLayout main = new VerticalLayout();

            // Créer une image
            Image productImage = new Image("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(products.get(0).getImage()), products.get(0).getName());
            productImage.setHeight("100px");

            // Créer un texte
            H3 productName = new H3(products.get(0).getName());

            // Créer un layout horizontal pour afficher l'image et le texte côte à côte
            HorizontalLayout header = new HorizontalLayout();
            header.setAlignItems(Alignment.CENTER);
            header.setWidthFull();
            header.add(productImage, productName);
            main.add(header);

            // Créer un tableau à afficher
            Grid<PriceModel> grid = new Grid<>(PriceModel.class, false);
            grid.addColumn(price -> String.valueOf(price.getProduct().getQuantity()).replaceAll("\\.", ",") + (price.getProduct().getType() == ProductTypeUnit.DRY ? " kg" : " L")).setHeader("Quantité").setSortable(true);
            grid.addColumn(price -> price.getProduct().getMarketName()).setHeader("Magasin").setSortable(true);
            grid.addColumn(price -> String.valueOf(price.getPrice()).replaceAll("\\.", ",") + " €").setHeader("Prix").setSortable(true);
            grid.addColumn(price -> String.valueOf(price.getUnitPrice()).replaceAll("\\.", ",") + (price.getProduct().getType() == ProductTypeUnit.DRY ? " €/kg" : " €/L")).setHeader("Prix par mesure").setSortable(true);
            grid.addComponentColumn(price -> {
                Button button = new Button(VaadinIcon.CART.create());
                button.addClickListener(event -> {
                    AddProductDialog addProductDialog = new AddProductDialog(user, price);
                    addProductDialog.open();
                });
                return button;
            }).setHeader("Ajouter").setSortable(false);
            grid.setSelectionMode(Grid.SelectionMode.NONE);
            grid.setItems(prices);
            grid.setWidthFull();
            main.add(grid);

            // Ajouter les éléments à la vue
            add(main);
            // Modifier la taille
            setWidth("65%");
        }

        public class AddProductDialog extends Dialog {

            public AddProductDialog(UserEntity user, PriceModel price) {
                // Créer le champ de texte modifiable
                TextField quantityField = new TextField();
                quantityField.setPattern("[1-9][0-9]{0,3}"); // Limite la valeur à entre 1 et 9999
                quantityField.setValue("1"); // Initialise la valeur à 1

                // Créer les boutons pour incrémenter et décrémenter la valeur
                Button minusButton = new Button("-");
                minusButton.addClickListener(event -> {
                    int value = Integer.parseInt(quantityField.getValue());
                    if (value > 1) {
                        quantityField.setValue(String.valueOf(value - 1));
                    }
                });
                Button plusButton = new Button("+");
                plusButton.addClickListener(event -> {
                    int value = Integer.parseInt(quantityField.getValue());
                    if (value < 9999) {
                        quantityField.setValue(String.valueOf(value + 1));
                    }
                });

                // Créer un layout horizontal pour les deux boutons
                HorizontalLayout buttonsLayout = new HorizontalLayout(minusButton, quantityField, plusButton);
                buttonsLayout.setAlignItems(Alignment.CENTER);
                buttonsLayout.setSpacing(true);

                // Créer un bouton "Ajouter"
                Button addButton = new Button("Ajouter");
                addButton.addClickListener(event -> {
                    int value = Integer.parseInt(quantityField.getValue());
                    if (value >= 1 && value <= 9999) {
                        Map<String, Integer> shoppingList = ConverterUtil.convertStringToMap(user.getShoppingList());
                        if (shoppingList.containsKey(price.getProduct().getId())) {
                            shoppingList.replace(price.getProduct().getId(), shoppingList.get(price.getProduct().getId()) + value);
                        } else {
                            shoppingList.put(price.getProduct().getId(), value);
                        }
                        user.setShoppingList(ConverterUtil.convertMapToString(shoppingList));
                        userRepository.save(user);
                        UI.getCurrent().getPage().reload();
                    }
                });
                addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                addButton.setWidthFull();

                // Ajouter les éléments à la vue
                add(buttonsLayout, addButton);
            }

        }

    }

}