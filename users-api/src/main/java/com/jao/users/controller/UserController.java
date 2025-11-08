package com.jao.users.controller;

import com.jao.users.model.User;
import com.jao.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc){ this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<User>> list(){ return ResponseEntity.ok(svc.list()); }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id){ return ResponseEntity.ok(svc.get(id)); }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User u){
        User created = svc.create(u);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User u){
        return ResponseEntity.ok(svc.update(id, u));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}

