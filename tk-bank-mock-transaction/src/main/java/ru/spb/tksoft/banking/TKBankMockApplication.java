package ru.spb.tksoft.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@SpringBootApplication
@EnableScheduling
public class TKBankMockApplication {

	/**
	 * Run application.
	 * 
	 * @param args Аргументы командной строки.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TKBankMockApplication.class, args);
	}

}
