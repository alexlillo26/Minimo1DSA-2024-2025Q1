package edu.upc.dsa.models;

import java.time.LocalDate;

public class User {
    private String id;
    private String nombre;
    private String apellidos;
    private String email;
    private LocalDate nacimiento;

    public User(String id, String nombre, String apellidos, String email, LocalDate nacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nacimiento = nacimiento;
    }

    public User(String nombre, String apellidos, String email, LocalDate nacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nacimiento = nacimiento;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento = nacimiento;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email + ", nacimiento=" + nacimiento + "]";
    }
}