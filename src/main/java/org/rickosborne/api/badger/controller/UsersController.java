package org.rickosborne.api.badger.controller;

import org.rickosborne.api.badger.data.CheckIn;
import org.rickosborne.api.badger.data.CheckInRepository;
import org.rickosborne.api.badger.data.User;
import org.rickosborne.api.badger.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
@Secured({"ROLE_USER"})
public class UsersController {

    private static final int MAX_CHECKINS = 30;

    private boolean hasRole(Authentication user, String role) {
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.toString().equals(role)) return true;
        }
        return false;
    }

    private User ensureAdminOrRequestedUser (Long userId, Principal principal) {
        Authentication currentUser = (Authentication) principal;
        User user = userRepository.findById(userId);
        if (user == null) throw new AccessDeniedException("You aren't really here.");
        if (hasRole(currentUser, "ROLE_TRUSTED_USER") || (user.getUsername().equals(currentUser.getName()))) return user;
        throw new AccessDeniedException("Can't access another user.");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    private final static Sort sortNames = new Sort(new Sort.Order(Sort.Direction.ASC, "nameLast"), new Sort.Order(Sort.Direction.ASC, "nameFirst"));

//    @RequestMapping(method = RequestMethod.GET)
//    public @ResponseBody boolean yup() { return true; }

    @RequestMapping(method = RequestMethod.GET)
    @Secured({"ROLE_TRUSTED_USER"})
    public Iterable<User> index(@RequestParam(value = "q", required = false) String query) {
        if ((query != null) && !query.isEmpty()) return userRepository.findByNameFirstContainingOrNameLastContainingOrEmailContaining(query, query, query, sortNames);
        return userRepository.findAll(sortNames);
    }

    @RequestMapping(value = "/{userId}/patients")
    public Iterable<User> patients (@PathVariable Long userId, Principal currentUser) {
        return ensureAdminOrRequestedUser(userId, currentUser).getPatients();
    }

    @RequestMapping(value = "/{userId}")
    public User findUser(@PathVariable("userId") Long userId, Principal currentUser) {
        return ensureAdminOrRequestedUser(userId, currentUser);
    }

    @RequestMapping(method = RequestMethod.POST)
    @Secured({"ROLE_TRUSTED_USER"})
    public User createUser(@Valid @RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @RequestMapping(value = "/{userId}/checkIns")
    @Transactional
    public Iterable<CheckIn> checkIns (@PathVariable("userId") Long userId, Principal currentUser) {
        return checkInRepository.findByUserOrderByDateSubmittedDesc(ensureAdminOrRequestedUser(userId, currentUser),
                new PageRequest(0, MAX_CHECKINS));
    }

    @RequestMapping(value = "/{userId}/checkIns", method = RequestMethod.POST)
    public CheckIn checkIn (@PathVariable("userId") Long userId, @Valid @RequestBody CheckIn checkIn, Principal currentUser) {
        checkIn.setUser(ensureAdminOrRequestedUser(userId, currentUser));
        checkIn.setDateSubmitted(new Date());
        checkInRepository.save(checkIn);
        return checkIn;
    }

}
