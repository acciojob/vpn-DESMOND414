package com.driver.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public ServiceProvider(String name, Admin admin) {
        this.name = name;
        this.admin = admin;
    }

    @ManyToOne
    @JoinColumn
    private Admin admin;

    public ServiceProvider() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    @ManyToMany
    private List<User> users;

    @OneToMany(mappedBy = "serviceProvider")
    private List<Connection> connectionList;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.PERSIST)
    private List<Country> countryList;

}
