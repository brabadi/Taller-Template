/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.commons.domain;

/**
 *
 * @author braia
 */
public class Restaurant {
    
    private int res_NIT;
    private Customer cli_Id;
    private String res_Nombre;
    private String res_Direccion;
    

    public Restaurant() {
    }

    public Restaurant(int nit, Customer cli_Id, String nombre, String res_Direccion) {
        this.res_NIT = nit;
        this.cli_Id = cli_Id;
        this.res_Nombre= nombre;
        this.res_Direccion = res_Direccion;
    }

    public String getNombre() {
        return res_Nombre;
    }

    public void setNombre(String nombre) {
        this.res_Nombre = nombre;
    }

    public int getNit() {
        return res_NIT;
    }

    public void setNit(int nit) {
        this.res_NIT = nit;
    }

    public String getRes_Direccion() {
        return res_Direccion;
    }

    public void setRes_Direccion(String res_Direccion) {
        this.res_Direccion = res_Direccion;
    }

    public Customer getCli_Id() {
        return cli_Id;
    }

    public void setCli_Id(Customer cli_Id) {
        this.cli_Id = cli_Id;
    }
    
}
