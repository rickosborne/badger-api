package org.rickosborne.api.badger.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nameFirst;
    private String nameLast;
    private String email;

    public User() {}

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNameFirst() { return nameFirst; }
    public void setNameFirst(String nameFirst) { this.nameFirst = nameFirst; }
    public String getNameLast() { return nameLast; }
    public void setNameLast(String nameLast) { this.nameLast = nameLast; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

}
