package org.rickosborne.api.badger.controller;

import org.rickosborne.api.badger.data.User;
import org.rickosborne.api.badger.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> index() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User read(@PathVariable("userId") Long userId) {
        return userRepository.findByUserId(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User create(@Valid @RequestBody User user) {
        userRepository.saveAndFlush(user);
        return user;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable("userId") Long userId, @Valid @RequestBody User user) {
        User original = userRepository.findByUserId(userId);
        if (original != null) {
            // copy attributes
        }
        return original;
    }

    @RequestMapping(value = "?q={query}", method = RequestMethod.GET)
    public List<User> search(@RequestParam("query") String query) {
        return userRepository.findByNameFirstContainingOrNameLastContainingOrEmailContaining(query, query, query);
    }

}
