package be.hesest.tfe;

import be.hesest.tfe.entities.MarketEntity;
import be.hesest.tfe.entities.ProductEntity;
import be.hesest.tfe.managers.MarketsManager;
import be.hesest.tfe.managers.ProductsManager;
import be.hesest.tfe.repositories.MarketRepository;
import be.hesest.tfe.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TfeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TfeApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ProductRepository productRepository, MarketRepository marketRepository) {
		return args -> {
			for (ProductEntity product : productRepository.findAll()) {
				ProductsManager.getProducts().put(product.getId(), product);
			}
			for (MarketEntity market : marketRepository.findAll()) {
				MarketsManager.getMarkets().put(market.getId(), market);
			}
		};
	}

}
