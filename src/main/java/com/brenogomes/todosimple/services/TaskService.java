package com.brenogomes.todosimple.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brenogomes.todosimple.models.Task;
import com.brenogomes.todosimple.models.User;
import com.brenogomes.todosimple.models.enus.ProfileEnum;
import com.brenogomes.todosimple.models.projection.TaskProjection;
import com.brenogomes.todosimple.repositores.TaskRepository;
import com.brenogomes.todosimple.security.UserSpringSecurity;
import com.brenogomes.todosimple.services.execptions.AuthorizationException;
import com.brenogomes.todosimple.services.execptions.DataBindingViolationException;
import com.brenogomes.todosimple.services.execptions.ObjectNotFoundException;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserService userService;
	
	public Task findById(Long id){
		Task task = this.taskRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException(
				"Tarefa não encontrada! Id: " + id + ", tipo: " + Task.class.getName()));
				
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		if(Objects.isNull(userSpringSecurity) 
				|| !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
			throw new AuthorizationException("Acesso negado!");
			
				return task;
	}
	
	public List<TaskProjection> findAllByUser(){
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		if(Objects.isNull(userSpringSecurity))
			throw new AuthorizationException("Acesso negado!");
		
		List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
		return tasks;
	}
	
	public Task create(Task obj){
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		if(Objects.isNull(userSpringSecurity))
			throw new AuthorizationException("Acesso negado!");
		
		 User user = userService.findById(userSpringSecurity.getId());
		 obj.setId(null);
		 obj.setUser(user);
		 obj = this.taskRepository.save(obj);
		 return obj;
	}
	
	public Task update(Task obj){
		Task newObj = findById(obj.getId());
		newObj.setDescription(obj.getDescription());
		return this.taskRepository.save(newObj);
	}
	
	public void delete(Long id) {
		findById(id);
		try {
			this.taskRepository.deleteById(id);
		} catch (Exception e) {
			throw new DataBindingViolationException("Não é possivel excluir pois há entidades relacionadas");
		}
	}
	
	private boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
		return task.getUser().getId().equals(userSpringSecurity.getId());
	}
	
}
