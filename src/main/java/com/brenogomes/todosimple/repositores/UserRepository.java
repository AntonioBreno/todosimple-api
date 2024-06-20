package com.brenogomes.todosimple.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brenogomes.todosimple.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	
}
