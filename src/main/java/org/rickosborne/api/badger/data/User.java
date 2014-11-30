package org.rickosborne.api.badger.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = {"isAdmin", "password", "authorities"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull private String nameFirst;
    @NotNull private String nameLast;
    @NotNull private String email;
    @NotNull private String password;
    @NotNull @JsonIgnore private boolean isAdmin = false;
    @NotNull private boolean isEnabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_patients")
    private Set<User> patients;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "userId")
    private Set<CheckIn> checkIns;

    public static User create(String email, String password, String... authorities) {
        return new User(email, password, authorities);
    }

    public User() {}

    public User (String email, String password, String[] authorities) {
        setEmail(email);
        setPassword(password);
        for (String authority : authorities) {
            if (authority.equals("ADMIN")) setIsAdmin(true);
        }
    }

    public User (String email, String password) {
        this(email, password, new String[0]);
    }

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNameFirst() { return nameFirst; }
    public void setNameFirst(String nameFirst) { this.nameFirst = nameFirst; }
    public String getNameLast() { return nameLast; }
    public void setNameLast(String nameLast) { this.nameLast = nameLast; }
    public String getEmail() { return email; }
    public void setIsEnabled(boolean isEnabled) { this.isEnabled = isEnabled; }
    @JsonIgnore public boolean getIsEnabled() { return isEnabled; }
    @JsonIgnore public boolean getIsAdmin() { return isAdmin; }
    public void setEmail(String email) { this.email = email.toLowerCase(); }
    public void setPassword(String password) { this.password = password; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    @Override @JsonIgnore public String getPassword() { return password; }
    @Override @JsonIgnore public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isAdmin && isEnabled) return AuthorityUtils.createAuthorityList("ADMIN", "USER");
        if (isEnabled) return AuthorityUtils.createAuthorityList("USER");
        return AuthorityUtils.createAuthorityList();
    }
    @Override @JsonIgnore public String getUsername() { return email; }
    @Override @JsonIgnore public boolean isAccountNonExpired() { return isEnabled; }
    @Override @JsonIgnore public boolean isAccountNonLocked() { return isEnabled; }
    @Override @JsonIgnore public boolean isCredentialsNonExpired() { return isEnabled; }
    @Override public boolean isEnabled() { return isEnabled; }

    @JsonIgnore public Set<User> getPatients() { return patients; }
    @JsonIgnore public Set<CheckIn> getCheckIns() { return checkIns; }
}
