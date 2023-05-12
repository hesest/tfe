# tfe

Pour lancer, ne pas oublier de créer un fichier `application.properties` dans un dossier `resources` avec les éléments suivants :
```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://[base de données]
spring.datasource.username=[nom d'utilisateur]
spring.datasource.password=[mot de passe]
spring.colruyt.api-key=[clé d'API Colruyt]
spring.openrouteservice.api-key=[clé d'API OpenRouteService]
```
