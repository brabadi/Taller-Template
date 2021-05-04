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
public class PlatoEjecutivo {
    
    private int IDplatoEJ;
    
    private int ResNIT;

    private DiaEnum diaSemana;
    /**
     * una preparacion de tipo entrada
     */
    private Component entrada;
    /**
     * una preparacion de tipo principio
     */
    private Component principio;
    /**
     * una preparacion de tipo carne, pollo, res etc
     */
    private Component proteina;
    /**
     * una preparacion de tipo bebida
     */
    private Component bebida;

    private int precio;

    public PlatoEjecutivo() {
    }

    public PlatoEjecutivo(int IDplatoEJ, int ResNIT, DiaEnum diaSemana, Component entrada, Component principio, Component proteina, Component bebida, int precio) {
        this.IDplatoEJ = IDplatoEJ;
        this.ResNIT = ResNIT;
        this.diaSemana = diaSemana;
        this.entrada = entrada;
        this.principio = principio;
        this.proteina = proteina;
        this.bebida = bebida;
        this.precio = precio;
    }

    public int getIDplatoEJ() {
        return IDplatoEJ;
    }

    public void setIDplatoEJ(int IDplatoEJ) {
        this.IDplatoEJ = IDplatoEJ;
    }

    public int getResNIT() {
        return ResNIT;
    }

    public void setResNIT(int ResNIT) {
        this.ResNIT = ResNIT;
    }

    public DiaEnum getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaEnum diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Component getEntrada() {
        return entrada;
    }

    public void setEntrada(Component entrada) {
        this.entrada = entrada;
    }

    public Component getPrincipio() {
        return principio;
    }

    public void setPrincipio(Component principio) {
        this.principio = principio;
    }

    public Component getProteina() {
        return proteina;
    }

    public void setProteina(Component proteina) {
        this.proteina = proteina;
    }

    public Component getBebida() {
        return bebida;
    }

    public void setBebida(Component bebida) {
        this.bebida = bebida;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
