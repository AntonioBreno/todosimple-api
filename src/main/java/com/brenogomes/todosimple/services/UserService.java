package com.brenogomes.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brenogomes.todosimple.models.User;
import com.brenogomes.todosimple.repositores.UserRepository;

@Service
public class UserService {

	// serve como se fosse um construtor prod server
	
	@Autowired  
	private UserRepository userRepository;
	

	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow( () -> new RuntimeException(
				"Usuario não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
				)); 
	}
	
	//Usar o "Transactional" para quando for inserir algo no bd ou atualizar
	
	@Transactional
	public User create(User obj) {
		obj.setId(null);
		obj = this.userRepository.save(obj);
		return obj;
	}
	
	@Transactional
	public User update(User obj) {
		User newObj = findById(obj.getId());
		newObj .setPassword(obj.getPassword());
		return this.userRepository.save(newObj);
	}
	
	public void delete(Long id) {
		findById(id);
		try {
			this.userRepository.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("Nao é possivel excluir pois há entidades relacionadas!"); 
		}
	}
}
