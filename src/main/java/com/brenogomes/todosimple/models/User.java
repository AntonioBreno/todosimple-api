package com.brenogomes.todosimple.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = User.TABLE_NAME)
public class User {
	
	public interface CreateUser{}
	public interface UpdateUser{}

	public static final String TABLE_NAME  ="user";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id", unique = true)
	private Long id;
	
	
	@Column(name = "username", length = 100, nullable = false,unique = true)
	@NotNull(groups = CreateUser.class)
	@NotEmpty(groups = CreateUser.class)
	@Size(groups = CreateUser.class, min = 2, max = 100)
	private String username;
	
	
	@Column(name = "password", length = 60, nullable = false)
	@NotNull(groups = {CreateUser.class, UpdateUser.class})
	@NotEmpty(groups = {CreateUser.class, UpdateUser.class})
	@Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 60)
	private String password;


	public User(Long id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id) && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.id == null? 0 : this.id.hashCode());
		return result;
	}
	
	//private List<Task> tasks = new ArrayList<>();
	
	
}
