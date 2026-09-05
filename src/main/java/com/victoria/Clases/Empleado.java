package com.victoria.Clases;

import java.time.LocalDate;

public class Empleado {
    String nombre;
    String apellido;
    String direccion;
    Integer dni;
    String contacto;
    String rol;
    private LocalDate dia_de_alta;

    public Empleado(Integer d, String n, String a, String direccion, String contacto) {
        this(d, n, a, direccion, contacto, "Empleado");
    }

    public Empleado(Integer d, String n, String a, String direccion, String contacto, String rol) {
        this.dni = d;
        this.nombre = n;
        this.apellido = a;
        this.direccion = direccion;
        this.contacto = contacto;
        this.rol = (rol != null) ? rol : "Empleado";
        this.dia_de_alta = LocalDate.now();
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getContacto(){ return contacto; }
    public void setContacto(String c){ this.contacto = c; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public Integer getDni() { return dni; }
    public void setDni(Integer dni) { this.dni = dni; }
    public LocalDate getDia_de_alta() { return dia_de_alta; }
    public void setDia_de_alta(LocalDate dia_de_alta) { this.dia_de_alta = dia_de_alta; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Empleado otro = (Empleado) obj;
        return dni != null && dni.equals(otro.dni);
    }

    @Override
    public int hashCode() {
        return dni != null ? dni.hashCode() : 0;
    }
}