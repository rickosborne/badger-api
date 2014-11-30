package org.rickosborne.api.badger.controller;

import org.rickosborne.api.badger.data.User;
import org.rickosborne.api.badger.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@Secured({"ROLE_USER"})
public class UsersController {

    private User ensureAdminOrRequestedUser (Long userId, Principal principal) {
        Authentication currentUser = (Authentication) principal;
        User user = userRepository.findByEmail(currentUser.getName());
        if (user == null) throw new AccessDeniedException("You aren't really here.");
        if (currentUser.getAuthorities().contains("ROLE_TRUSTED_USER") || (user.getId() == userId)) return user;
        throw new AccessDeniedException("Can't access another user.");
    }

    @Autowired
    private UserRepository userRepository;

//    @RequestMapping(method = RequestMethod.GET)
//    public @ResponseBody boolean yup() { return true; }

    @RequestMapping(method = RequestMethod.GET)
    @Secured({"ROLE_TRUSTED_USER"})
    public Iterable<User> index() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}/patients")
    public Iterable<User> patients (@PathVariable Long userId, Principal currentUser) {
        return ensureAdminOrRequestedUser(userId, currentUser).getPatients();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User read(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User create(@Valid @RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable("userId") Long userId, @Valid @RequestBody User user) {
        User original = userRepository.findById(userId);
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
