package com.victoria.Clases;

public class Ropa extends Producto{



/*
 * se puede manejar todos los productos como Producto gracias al polimorfismo, 
 * pero si quiero hacer algo específico con Ropa o Accesorio, podés usar instanceof o casting.
 * Los tipos de ropa pueden ser:
    Remeras 
    Musculosas
    Deportivo
    Pantalones 
    Buzos
    Pulovers
    Chombas
    Boxer
    Campera
    Malla
    Bermuda
    Camisa
    Zapatillas
 */
    public Ropa(String des, String talle, double precio, String color, String marca,String tipo) {
        super(des, talle, precio, color, marca,"Ropa",tipo);
        //TODO Auto-generated constructor stub
    }

    @Override
    public String getColor() {
        // TODO Auto-generated method stub
        return super.getColor();
    }

    @Override
    public String getDescripcion() {
        // TODO Auto-generated method stub
        return super.getDescripcion();
    }

   
    @Override
    public Integer getId_producto() {
        // TODO Auto-generated method stub
        return super.getId_producto();
    }

    @Override
    public String getMarca() {
        // TODO Auto-generated method stub
        return super.getMarca();
    }

    @Override
    public double getPrecio() {
        // TODO Auto-generated method stub
        return super.getPrecio();
    }

    @Override
    public String getTalle() {
        // TODO Auto-generated method stub
        return super.getTalle();
    }

    @Override
    public String getTipo() {
        // TODO Auto-generated method stub
        return super.getTipo();
    }

    @Override
    public String getTipoProducto() {
        // TODO Auto-generated method stub
        return super.getTipoProducto();
    }
    

}