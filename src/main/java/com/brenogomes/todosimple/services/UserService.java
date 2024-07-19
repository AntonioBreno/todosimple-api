package com.brenogomes.todosimple.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brenogomes.todosimple.models.User;
import com.brenogomes.todosimple.models.enus.ProfileEnum;
import com.brenogomes.todosimple.repositores.UserRepository;
import com.brenogomes.todosimple.services.execptions.DataBindingViolationException;
import com.brenogomes.todosimple.services.execptions.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired 
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// serve como se fosse um construtor prod server
	@Autowired  
	private UserRepository userRepository;
	

	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow( () -> new ObjectNotFoundException(
				"Usuario não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
				)); 
	}
	
	//Usar o "Transactional" para quando for inserir algo no bd ou atualizar
	
	@Transactional
	public User create(User obj) {
		obj.setId(null);
		obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
		obj = this.userRepository.save(obj);
		return obj;
	}
	
	@Transactional
	public User update(User obj) {
		User newObj = findById(obj.getId());
		newObj .setPassword(obj.getPassword());
		newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		return this.userRepository.save(newObj);
	}
	
	public void delete(Long id) {
		findById(id);
		try {
			this.userRepository.deleteById(id);
		} catch (Exception e) {
			throw new DataBindingViolationException("Nao é possivel excluir pois há entidades relacionadas!"); 
		}
	}
}
