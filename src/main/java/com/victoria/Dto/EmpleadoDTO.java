package com.victoria.Dto;

import java.time.LocalDate;


public class EmpleadoDTO {
   private Integer dni;
   private String nombre;
   private String apellido;
   private String direccion;
   private LocalDate fechaInicio;

   public EmpleadoDTO(){

   };

   public EmpleadoDTO(Integer dni, String nombre, String apellido, String direcc, LocalDate fechaInicio){
    this.dni= dni;
    this.nombre=nombre;
    this.apellido=apellido;
    this.direccion= direcc;
    this.fechaInicio=fechaInicio;
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
   

}
