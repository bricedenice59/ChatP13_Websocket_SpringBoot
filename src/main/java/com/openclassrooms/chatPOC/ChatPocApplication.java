package com.openclassrooms.chatPOC;

import com.openclassrooms.chatPOC.models.Role;
import com.openclassrooms.chatPOC.models.User;
import com.openclassrooms.chatPOC.repositories.RoleRepository;
import com.openclassrooms.chatPOC.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class ChatPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatPocApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner (RoleRepository roleRepository,
									 UserRepository userRepository,
									 PasswordEncoder passwordEncoder) {
		return args -> {
			var userRole = Role.builder().name("USER").build();
			if(roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(userRole);
			}

			var agent = User.builder()
					.name("customer-service-brice")
					.email("customer-service-brice@cie.com")
					.password(passwordEncoder.encode("cie123Service&"))
					.roles(List.of(userRole))
					.build();

			var customer = User.builder()
					.name("brice")
					.email("brice@test.com")
					.password(passwordEncoder.encode("brice123&"))
					.roles(List.of(userRole))
					.build();

			userRepository.save(customer);
			userRepository.save(agent);
		};
	}

}
