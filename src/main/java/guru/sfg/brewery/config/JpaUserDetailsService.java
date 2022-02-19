package guru.sfg.brewery.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> {
			return new UsernameNotFoundException("Username " + username + " not found");
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getEnabled(), user.getAccountNonExpired(), user.getCredentialsNonExpired(),
				user.getAccountNonLocked(), getAuthorities(user.getAuthorities()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Set<Authority> authorities) {
		if (authorities != null && !authorities.isEmpty())
			return authorities.stream().map(Authority::getRole).map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
		else
			return new HashSet<>();
	}

}
