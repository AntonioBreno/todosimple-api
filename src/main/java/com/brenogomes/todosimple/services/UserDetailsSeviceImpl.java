package com.brenogomes.todosimple.services;

import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.brenogomes.todosimple.models.User;
import com.brenogomes.todosimple.repositores.UserRepository;
import com.brenogomes.todosimple.security.UserSpringSecurity;

@Service
public class UserDetailsSeviceImpl implements UserDetailsService{

	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
		if(Objects.isNull(user))
			throw new UsernameNotFoundException("Usuario Nao encontrado: " + username);
		return new UserSpringSecurity(user.getId(), user.getUsername(), user.getPassword(), user.getProfiles());
	}

}
