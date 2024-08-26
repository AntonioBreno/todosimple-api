package com.brenogomes.todosimple.repositores;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brenogomes.todosimple.models.Task;
import com.brenogomes.todosimple.models.projection.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<TaskProjection> findByUser_Id(Long id);
	
	//@Query(value = "SELEC t from Task t WHERE t.user.id = : id")
	//List<Task> findByUser_Id(@Param("id") Long id);
}
