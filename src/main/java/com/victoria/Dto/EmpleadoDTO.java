package com.victoria.Dto;

import java.time.LocalDate;


public class EmpleadoDTO {
   private Integer dni;
   private String nombre;
   private String apellido;
   private String direccion;
   private String contacto;
   private LocalDate fechaInicio;

   public EmpleadoDTO(){

   };

   public EmpleadoDTO(Integer dni, String nombre, String apellido, String direcc, String contacto, LocalDate fechaInicio){
    this.dni= dni;
    this.nombre=nombre;
    this.apellido=apellido;
    this.direccion= direcc;
    this.contacto = contacto;
    this.fechaInicio=fechaInicio;
   }

   public String getContacto(){
      return contacto;
   }
   public void setContacto(String c){
      this.contacto=c;
   }
   public LocalDate getDia_de_alta(){
      return fechaInicio;
   }

   public void setDia_de_alta(LocalDate fecha) {
    this.fechaInicio = fecha;
   }


   public Integer getDni() {
    return dni;
   }

   public void setDni(Integer dni) {
    this.dni = dni;
   }

   public String getNombre() {
    return nombre;
   }

   public void setNombre(String nombre) {
    this.nombre = nombre;
   }

   public String getApellido() {
    return apellido;
   }

   public void setApellido(String apellido) {
    this.apellido = apellido;
   }

   public String getDireccion() {
    return direccion;
   }

   public void setDireccion(String direccion) {
    this.direccion = direccion;
   }

   public LocalDate getFechaInicio() {
    return fechaInicio;
   }

   public void setFechaInicio(LocalDate fechaInicio) {
    this.fechaInicio = fechaInicio;
   }
   public String getNombreCompleto() {
    return nombre + " " + apellido;
   }
   

}
