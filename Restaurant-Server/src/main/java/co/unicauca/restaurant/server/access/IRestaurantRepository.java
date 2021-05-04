package co.unicauca.restaurant.server.access;

import co.unicauca.restaurant.commons.domain.Component;
import co.unicauca.restaurant.commons.domain.Customer;
import co.unicauca.restaurant.commons.domain.DiaEnum;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;

/**
 *
 * @author braia
 */
public interface IRestaurantRepository {

    
    
    /**
     * Encuentra un cliente en la tabla cliente
     * @return el valor del id del clienteen caso de exito
     */
    public String findCustomer();

    /**
     * Funcion que guardar un componente en la base de datos
     * @param component es el objeto de tipo Componente que se guarda
     * @return el Id del componente del cual se ha gardado
     */
    public String saveComponent(Component component);

    /**
     * Funcion que encuentra un componente en especifico en la tabla Componente
     * @param id_component es el identificador o id del componente.
     * @return el objeto componente encontrado o null de lo contrario.
     */
    public Component finComponent(String id_component);

    /**
     * Funcion que guarda en una lista todos los componentes registrados de la tabla Component.
     * @return una cadena de exito o fallo al invocar el JSon del la lista de componentes.
     */
    public String listarComponentes();

    
    /**
     * Funcion que guarda en una lista todos los restaurnates registrados de la tabla Restaurant.
     * @return una cadena de exito o fallo al invocar el JSon del la lista de Component.
     */
    public String listarRestaurantes();

    /**
     *  Realiza la actualizacion de un plato ejecutivo en la tabla almuerzoejedia
     * 
     * @param IDplatoEJ id del plato ejecutivo
     * @param ResNIT id del restaurante al cual pertenece el plato.
     * @param diaSemana dia de la semana.
     * @param AlmEjeDia_comp1 componente de tipo entrada
     * @param AlmEjeDia_comp2 componente de tipo princicpio
     * @param AlmEjeDia_comp3 componente de tipo proteina
     * @param AlmEjeDia_comp4 componente de tipo bebida.
     * @param precio es costo del plato.
     * @return  mensaje de fallo o exito de la actualizacion.
     */
    public String updatePlatoEjecutivo(String IDplatoEJ, String ResNIT, String diaSemana, String AlmEjeDia_comp1, String AlmEjeDia_comp2, String AlmEjeDia_comp3, String AlmEjeDia_comp4, String precio);

    public String listarAlmuerzoEjeDia();
}
