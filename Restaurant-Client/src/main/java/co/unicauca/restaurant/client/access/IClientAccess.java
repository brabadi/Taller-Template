/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client.access;

import co.unicauca.restaurant.commons.domain.Component;
import co.unicauca.restaurant.commons.domain.Customer;
import co.unicauca.restaurant.commons.domain.DiaEnum;
import java.util.List;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;

/**
 *
 * @author braia
 * Se usa IClienteAccess para hacer solicitudes al servidor el cual
 * trabajara la base de datos.
 */

public interface IClientAccess {

 
    

    /**
     * El administrador solicita registrar un componente el cual sera enviado a
     * la aplicacion Restaurant-Server
     *
     * @param component componente que se quiere se envia para el almacenamiento
     * @return el identificador del componente que se guardo o FALLO en caso
     * contrario.
     * @throws Exception
     */
    public String saveComponent(Component component) throws Exception;

    /**
     * El cliente solicita un componente especifico.
     *
     * @param id_component identifiacdor del componente del cual se solicita
     * @return un objeto Component.
     * @throws Exception
     */
    public Component findComponent(String id_component) throws Exception;

    /**
     * El administrador solicita listar todos los componentes registrados en su
     * restaurante.
     *
     * @return lista de componentes
     * @throws Exception
     */
    public List<Component> listarComponents() throws Exception;

    /**
     * EL cliente(comprador) solicita listar todos los restaurantes disponibles
     * en la aplicacion
     *
     * @return lista de restaurantes
     * @throws Exception
     */
    public List<Restaurant> listarRestaurants() throws Exception;

    /**
     * Se solicita la encontrar un cliente para validar su login administrador o
     * comprador
     *
     * @return lista de clientes
     * @throws Exception
     */
    public List<Customer> findCustomer() throws Exception;

    /**
     * El administrador solcita actualizar un plato ejecutivo
     *
     * @param IDplatoEJ id del plato ejecutivo
     * @param ResNIT id del restaurante al cual pertenece el plato
     * @param diaSemana dia de la semana en el que se brinda el plato
     * @param entrada componente entrada
     * @param principio componente principio
     * @param proteina componente proteina
     * @param bebida componente bebida
     * @param precio costo del plato ejecutivo
     * @return true o false deacuerddo al exito de actuazlizacion.
     * @throws Exception
     */
    public boolean updateAlmuerzoEjecutivoDia(String IDplatoEJ, String ResNIT, String diaSemana, String entrada, String principio, String proteina, String bebida, String precio) throws Exception;

    /**
     * El cliente solicita listar el almuerzo ejecutivo del dia
     *
     * @return una lista de los platos ejecutivos.
     * @throws Exception
     */
    public List<PlatoEjecutivo> listarAlmuerzoEjeDia() throws Exception;

}
