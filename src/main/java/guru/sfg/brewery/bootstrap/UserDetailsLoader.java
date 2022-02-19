package guru.sfg.brewery.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class UserDetailsLoader implements CommandLineRunner{

	private UserRepository userRepository;
	private AuthorityRepository authorityRepository;
	private PasswordEncoder passwordEncoder;	
	
	@Override
	public void run(String... args) throws Exception {
		if(authorityRepository.count() == 0) {
			loadUserDetails();
		}
	}

	private void loadUserDetails() {
		Authority admin = Authority.builder().role("ADMIN").build();
		Authority userAuthority = Authority.builder().role("USER").build();
		Authority customer = Authority.builder().role("CUSTOMER").build();
		
		userRepository.save(User.builder()
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.authority(admin)
				.build());
		
		userRepository.save(User.builder()
				.username("user")
				.password(passwordEncoder.encode("password"))
				.authority(userAuthority)
				.build());
		
		userRepository.save(User.builder()
				.username("scott")
				.password(passwordEncoder.encode("tiger"))
				.authority(customer)
				.build());
		
		log.debug("Users loaded Successfully - "+userRepository.count());
	}

}
