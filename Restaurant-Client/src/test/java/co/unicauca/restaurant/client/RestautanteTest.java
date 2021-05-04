/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client;

import co.unicauca.restaurant.client.access.ClientAccessSocket;
import co.unicauca.restaurant.client.access.IClientAccess;
import co.unicauca.restaurant.client.domain.ClientService;
import co.unicauca.restaurant.commons.domain.Component;
import co.unicauca.restaurant.commons.domain.ComponentType;
import co.unicauca.restaurant.commons.domain.Customer;
import co.unicauca.restaurant.commons.domain.DiaEnum;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author juan jose
 */
public class RestautanteTest {

    public RestautanteTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
     /**
     * Test que solicita el servicio de listar los
     * componenetes registrados, en caso de que el tamaño 
     * de la lista que contiene los componenetes es cero 
     * entonces el metodo no pasa la prueba
     * @throws Exception 
     */
    @Test
    public void testListarComponentes() throws Exception {

        Boolean encontrado = false;

        IClientAccess service = new ClientAccessSocket();
        ClientService cliente = new ClientService(service);

        List<Component> componentes = cliente.listarComponents();
        if (componentes.size() > 0) {
            encontrado = true;
        }
        assertEquals(true, encontrado);
    }
    
     /**
     * Test que solicita el servicio de listar los
     * restaurantes de la ciudad, en caso de que el tamaño 
     * de la lista que contiene los restaurantes es cero 
     * entonces el metodo no pasa la prueba
     * @throws Exception 
     */
    @Test
    public void testListarRestarants() throws Exception {

        Boolean encontrado = false;

        IClientAccess service = new ClientAccessSocket();
        ClientService cliente = new ClientService(service);

        List<Restaurant> restaurants = cliente.listarRestaurants();
        if (restaurants.size() > 0) {
            encontrado = true;
        }
        assertEquals(true, encontrado);
    }
    
    
    /**
     * Test de Guardar un nuevo componente, se crea un objeto de tipo componente
     * con los respectivos valores de sus atributos y se envia por medio de los 
     * servicios de los sockets, donde retornara el id del componente en caso de 
     * enviado se encuentre en la base de datos la prueba se  ejecutara.
     * Se debe eliminar manualmente el componente que se inserta en esta prueba 
     * para que funcione ya que no se ha implementado el metodo de eliminar componente
    */
    @Test
    public void testAgregarComponente() throws Exception {

        String id;
        String nombre;
        String tipo;
        Boolean encontrado = false;

        id = "100";
        nombre = "prueba";
        tipo = "ENTRADA";

        IClientAccess service = new ClientAccessSocket();
        ClientService cliente = new ClientService(service);

        Component component = new Component(id, nombre, ComponentType.valueOf(tipo));

        try {
            String resultComponent = service.saveComponent(component);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "El Componente Ingresado ya esta registrado" + e.getMessage());
        }

        List<Component> componentes = cliente.listarComponents();
        if (componentes.size() > 0) {
            for (Component ls : componentes) {
                if (String.valueOf(ls.getCompID()).equals(id)) {
                    encontrado = true;
                }
            }
        }
        assertEquals(true, encontrado);
    }
    
    /**
     * Test de actualizar unplato ejecutivo 
     * Se crea un objeto de tipo plato ejecutivo con los respectivos
     * valores del objeto, el metodo de la app retorna un boolean
     * en caso de true entonces se actualiza correctamente el plato ejecutivo
     * en caso contrario no y la prueba ejecutiva no pasa.
     * @throws Exception 
     */
    @Test
    public void testUpdatePlato() throws Exception {

        Boolean encontrado = false;

        IClientAccess service = new ClientAccessSocket();
        ClientService cliente = new ClientService(service);

        encontrado = cliente.updateAlmuerzoEjecutivoDia("102", "1001", "JUEVES", "5", "6", "7", "8", "6000");

        assertEquals(true, encontrado);
    }

    /**
     * Test que solicita el servicio de listar los
     * almuerzos ejecutivos del dia, en caso de que el tamaño 
     * de la lista que contiene los almuerzos ejecutivos es cero 
     * entonces el metodo no pasa la prueba
     * @throws Exception 
     */
    @Test
    public void testlistarAlmuerzoEjeDia() throws Exception {

        Boolean encontrado = false;

        IClientAccess service = new ClientAccessSocket();
        ClientService cliente = new ClientService(service);

        List<PlatoEjecutivo> platosEjecutivos = cliente.listarAlmuerzoEjeDia();
        if (platosEjecutivos.size() > 0) {
            encontrado = true;
        }
        assertEquals(true, encontrado);
    }
    
    
    /**
     * Test de prueba del metodo de la interfaz IclienteAcces
     * se compara un id ya registrado en la base de datos con el 
     * valor devuelto por metodo que solicita el servicio por medio de
     *los sockets al servidor.
     * @throws Exception 
     */
    @Test
    public void testFindCustomer() throws Exception {

        int encontrar = 12345670;
        int encontrado = 0;

        IClientAccess service = new ClientAccessSocket();
        ClientService cliente = new ClientService(service);

        List<Customer> cus = cliente.findCustomer();
        if (cus.size() > 0) {
            for (Iterator<Customer> iterator = cus.iterator(); iterator.hasNext();) {
                Customer next = iterator.next();
                // System.out.println(next.getCus_id() + " - " + next.getCus_nombre());

                if (next.getCus_id() == 12345670) {
                    encontrado = next.getCus_id();

                }
            }
        }
        assertEquals(encontrar, encontrado);
    }

}
