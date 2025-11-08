package com.jao.users.service;

import com.jao.users.model.User;
import com.jao.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){ this.repo = repo; }

    public User create(User u){ return repo.save(u); }
    public User update(Long id, User u){
       return repo.findById(id).map(existing -> {
           existing.setName(u.getName());
           existing.setEmail(u.getEmail());
           return repo.save(existing);
       }).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    public void delete(Long id){ repo.deleteById(id); }
    public User get(Long id){ return repo.findById(id).orElseThrow(); }
    public List<User> list(){ return repo.findAll(); }
}

