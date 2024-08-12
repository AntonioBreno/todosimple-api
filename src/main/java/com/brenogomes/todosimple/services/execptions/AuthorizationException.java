package com.brenogomes.todosimple.services.execptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthorizationException  extends AccessDeniedException{

	public AuthorizationException(String menssagem) {
		super(menssagem);
		
	}

	
}