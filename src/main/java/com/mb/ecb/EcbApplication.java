package com.mb.ecb;

import com.mb.ecb.service.ExchangeMoneyService;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Milan Brankovic
 */
@SpringBootApplication
@AllArgsConstructor
public class EcbApplication implements CommandLineRunner {

	private final ExchangeMoneyService exchangeMoneyService;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EcbApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) {
		exchangeMoneyService.fetchHistoricalExchangeRates();
	}
}
