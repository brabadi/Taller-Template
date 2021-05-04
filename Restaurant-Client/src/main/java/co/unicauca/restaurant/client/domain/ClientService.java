/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client.domain;

import co.unicauca.restaurant.client.access.IClientAccess;
import co.unicauca.restaurant.commons.domain.Component;
import co.unicauca.restaurant.commons.domain.Customer;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;
import java.util.List;

/**
 *
 * @author braia
 */
public class ClientService {

    private final IClientAccess service;

    /**
     * inyeccion de dependencias
     *
     * @param service un clase concreta que implementa la interfaz de acceso, se
     * instancia con una fabrica
     */
    public ClientService(IClientAccess service) {
        this.service = service;
    }
    
    /**
     * Mascara para solicitar la lista de clientes, esta solicitud se envia al acceso
     * @return lista de tipo Customer
     * @throws Exception
     */
    public List<Customer> findCustomer() throws Exception {
        return service.findCustomer();
    }
    
     /**
     * Mascara para solicitar un componente de clientes, esta solicitud se envia al acceso
     * @param idComp identificador del componente.
     * @return
     * @throws Exception
     */
    public Component findComponent(String idComp) throws Exception {
        return service.findComponent(idComp);
    }
    
     /**
     * Mascara para guardar un componente, esta solicitud se envia al acceso
     * @param  component objeto a guardar
     * @return
     * @throws Exception
     */
    public String saveComponent(Component component) throws Exception{
        return service.saveComponent(component);
    }
    
     /**
     * Mascara para solicitar la lista de Componenetes, esta solicitud se envia al acceso
     * @return lista de tipo Component
     * @throws Exception
     */
    public List<Component> listarComponents() throws Exception{
        return service.listarComponents();
    }
    
     /**
     * Mascara para solicitar la lista de restaurantes, esta solicitud se envia al acceso
     * @return lista de tipo Restaurant.
     * @throws Exception
     */
    public List<Restaurant> listarRestaurants() throws Exception{
        return service.listarRestaurants();
    }
    
    /**
     * El cliente solicta la modificacion de un parametro en la base de datos para 
     * el plato ejecutivo (PLatoEjecutivo).
     * @param IDplatoEJ id del plato ejecutivo
     * @param ResNIT id del restaurante al cual pertenece el plato
     * @param diaSemana dia de la semana en el que se brinda el plato
     * @param entrada componente entrada
     * @param principio componente principio
     * @param proteina componente proteina
     * @param bebida componente bebida
     * @param precio costo del plato ejecutivo
     * @return true o false deacuerdo ala exito o fracaso de la accion.
     * @throws Exception 
     */
    public boolean updateAlmuerzoEjecutivoDia(String IDplatoEJ, String ResNIT, String diaSemana, String entrada, String principio, String proteina, String bebida, String precio)  throws Exception{
        //validaciones
        return service.updateAlmuerzoEjecutivoDia(IDplatoEJ, ResNIT, diaSemana, entrada, principio, proteina, bebida, precio);
    }
    
    
    /**
     * Mascara para solicitar la lista de platos ejecutivos, esta solicitud se envia al acceso
     * @return lista de tipo PlatoEjecutivo
     * @throws Exception
     */
    public List<PlatoEjecutivo> listarAlmuerzoEjeDia() throws Exception{
         return service.listarAlmuerzoEjeDia();
    }
}
