package com.victoria.Clases;


public class Tienda {
    String nombre;
    String direccion;
    String owner;
    String claveAdministrador;
   
    public Tienda(){
    nombre="Dharma.vip";
    direccion="General Paz 5786";
    owner="Mariano Bertero";
    claveAdministrador="$BerterO$";
    
    }
   
    //no vale la pena buscar empleados porque es una unica tienda asiq los empleados son los que estan en la tabla empleados
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
     public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getClaveAdministrador() {
        return claveAdministrador;
    }
    public void setClaveAdministrador(String claveAdministrador) {
        this.claveAdministrador = claveAdministrador;
    }
    
}
