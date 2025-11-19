package com.jao.users.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jao.users.model.User;
import com.jao.users.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService svc;
    public UserController(UserService svc){ this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<User>> list(){ 
    	logger.info("UserController:::list");
    	return ResponseEntity.ok(svc.list()); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id){ 
    	logger.info("UserController:::get",id);
    	return ResponseEntity.ok(svc.get(id)); 
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User u){
    	logger.info("UserController:::create");
        User created = svc.create(u);
        logger.info("UserController:::create",created.getId());
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User u){
    	logger.info("UserController:::update");
        return ResponseEntity.ok(svc.update(id, u));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
    	logger.info("UserController:::delete");
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/statusUserTest")
    public String status(@RequestParam(defaultValue = "listen") String nombre) {
    	
    	logger.info("Se llamo a /status con el nombre: {}", nombre);
    	return "Status, " +nombre;
    }
    
}

