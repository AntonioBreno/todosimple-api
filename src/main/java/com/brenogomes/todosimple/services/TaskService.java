package com.brenogomes.todosimple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brenogomes.todosimple.models.Task;
import com.brenogomes.todosimple.models.User;
import com.brenogomes.todosimple.repositores.TaskRepository;

import exceptions.DataBindingViolationException;
import exceptions.ObjectNotFoundException;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserService userService;
	
	public Task findById(Long id){
		Optional<Task> task = this.taskRepository.findById(id);
		return task.orElseThrow(()-> new ObjectNotFoundException(
				"Tarefa não encontrada! Id: " + id + ", tipo: " + Task.class.getName()
				));  
	}
	
	public List<Task> findAllByUserId(Long userId){
		List<Task> tasks = this.taskRepository.findByUser_Id(userId);
		return tasks;
	}
	
	public Task create(Task obj){
		 User user = userService.findById(obj.getUser().getId());
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
}
