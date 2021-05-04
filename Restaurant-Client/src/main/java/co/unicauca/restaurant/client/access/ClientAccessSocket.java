/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client.access;

import co.unicauca.restaurant.client.infra.RestaurantSocket;
import co.unicauca.restaurant.commons.domain.Component;
import co.unicauca.restaurant.commons.domain.Customer;
import co.unicauca.restaurant.commons.domain.DiaEnum;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;
import co.unicauca.restaurant.commons.infra.JsonError;
import co.unicauca.restaurant.commons.infra.Protocol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author braia
 */
public class ClientAccessSocket implements IClientAccess {

    /**
     * Uso de un socket para comunicarse con el servidor
     */
    private RestaurantSocket mySocket;

    //el costructor crea el socket para poder comunicarse con el servidor
    public ClientAccessSocket() {
        mySocket = new RestaurantSocket();
    }

    /**
     * establece la conexion con el servidor por parametro
     *
     * @param requestJson solicitud al servidor
     * @return verdadero si la solicitud es exitosa, falsa de lo contrario
     * @throws Exception
     */
    private String processConnection(String requestJson) throws Exception {
        String jsonResponse = null;
        try {
            //se establece la conexion
            mySocket.connect();
            //se envia la solicitud y se recibe una respuesta
            jsonResponse = mySocket.sendStream(requestJson);
            mySocket.closeStream();
            mySocket.disconnect();
            if (jsonResponse.equals("FALLO")) {
                return "FALLO";
            } else {
                System.out.println("Inició");
            }
        } catch (IOException ex) {
            ex.getMessage();
        }
        if (jsonResponse == null) {
            throw new Exception("error al conectarse con el servidor");
        } else {
            if (jsonResponse.contains("error")) {
                //Devolvió error, realizar login
                System.out.println("hubo error");
                throw new Exception(this.extractMessages(jsonResponse));
            } else {
                //Devuelve la respuesta del servidor
                return jsonResponse;
            }
        }
    }

    /**
     * Extra los mensajes de la lista de errores
     *
     * @param jsonResponse lista de mensajes json
     * @return Mensajes de error
     */
    private String extractMessages(String jsonResponse) {
        JsonError[] errors = jsonToErrors(jsonResponse);
        String msjs = "";
        for (JsonError error : errors) {
            msjs += error.getMessage();
        }
        return msjs;
    }

    /**
     * Convierte el jsonError a un array de objetos jsonError
     *
     * @param jsonError
     * @return objeto MyError
     */
    private JsonError[] jsonToErrors(String jsonError) {
        Gson gson = new Gson();
        JsonError[] error = gson.fromJson(jsonError, JsonError[].class);
        return error;
    }

    /**
     * Envia la solicitud al servidor para guardar un Componente
     *
     * @param instance la instancia a guardar
     * @return Nombre del componente el cual se ha hecho la solicutd de guardado
     * @throws Exception
     */
    @Override
    public String saveComponent(Component instance) throws Exception {
        //devuelve un string en formato Json que lo que se enviara
        String requestJson = createComponentJson(instance);
        if ((this.processConnection(requestJson)).equals("FALLO")) {
            return null;
        }
        return instance.getCompNombre();
    }

    /**
     * Crea el Componente recibido en un json para el envio por el sockect al
     * servidor
     *
     * @param instance
     * @return
     */
    private String createComponentJson(Component instance) {
        Protocol protocol = new Protocol();
        //el orden debe ser respetado
        protocol.setResource("administrador");
        protocol.setAction("postComponente");
        protocol.addParameter("compID", instance.getCompID());
        protocol.addParameter("compNombre", instance.getCompNombre());
        protocol.addParameter("compTipo", String.valueOf(instance.getCompTipo()));

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    /**
     * Realiza el envio de un id y retorno de un objeto Component al servidor
     *
     * @param id_component identificador del component
     * @return objeto de tipo Component
     * @throws Exception
     */
    @Override
    public Component findComponent(String id_component) throws Exception {
        String respJson = findComponentJson(id_component);
        Component component = null;
        if (this.processConnection(respJson).equals("FALLO")) {
            throw new Exception("No se pudo conectar con el servidor. Revise la red o que el servidor esté escuchando. ");
        }
        component = jsonToComponent(respJson);

        return component;
    }

    /**
     * Hace el uso de los protocolos para poder procesar la fuente, la accion y
     * los parametros que se le envian de la solicitud al servidor mediante
     * Gson.
     *
     * @param id identificador del componente el cual se solicita.
     * @return
     */
    public String findComponentJson(String id) {
        Protocol protocol = new Protocol();
        protocol.setResource("administrador");
        protocol.setAction("findComponent");
        protocol.addParameter("id", "" + id);

        Gson gson = new Gson();
        String requestJson = null;
        requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);

        return requestJson;
    }

    /**
     * Uso del Gson para procesar el envio de las solicitudes al servidor.
     *
     * @param jsonListarComponent
     * @return objeto de tipo Component.
     */
    private Component jsonToComponent(String jsonComponent) {

        Gson gson = new Gson();
        Component component = gson.fromJson(jsonComponent, Component.class);

        return component;

    }

    /**
     * Envia la fuente y la accion y resive la lista llegada desde el servidor
     * el cual transforma el json recibido desde este en una lista de Component.
     *
     * @return lista de tipo Component.
     * @throws Exception
     */
    @Override
    public List<Component> listarComponents() throws Exception {
        String resource = "administrador";
        String accion = "listarComponentes";
        String requestJson = createlistComponent(resource, accion);
        String response = procesarConexion(requestJson);
        return jsonListarComponent(response);
    }

    /**
     * Hace el uso de los protocolos para poder procesar el envio de la
     * solicitud al servidor. Crea el componente recibido en un json para el
     * envio por el sockect al servidor
     *
     * @param resource
     * @param accion
     * @return
     */
    private String createlistComponent(String resource, String accion) {
        Protocol protocol = new Protocol();
        protocol.setResource(resource);
        protocol.setAction(accion);

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    /**
     * Uso del Gson para procesar el envio de las solicitudes al servidor.
     *
     * @param jsonListarCComponent
     * @return lista de tipo Customer.
     */
    private List<Component> jsonListarComponent(String jsonListarComponent) {
        Gson gson = new Gson();
        Type list = new TypeToken<List<Component>>() {
        }.getType();
        return gson.fromJson(jsonListarComponent, list);
    }

    /**
     * Envia la fuente y la accion y resive la lista llegada desde el servidor
     * el cual transforma el json recibido desde este en una lista de Customer.
     *
     * @return lista de tipo Restaruant
     * @throws Exception
     */
    @Override
    public List<Customer> findCustomer() throws Exception {
        String resource = "administrador";
        String accion = "findCustomer";
        String requestJson = createlistCustomer(resource, accion);
        String response = procesarConexion(requestJson);
        return jsonListarCustomer(response);
    }

    /**
     * Crea el Customer recibido en un json para el envio por el sockect al
     * servidor
     *
     * @param resource
     * @param accion
     * @return
     */
    private String createlistCustomer(String resource, String accion) {
        Protocol protocol = new Protocol();
        protocol.setResource(resource);
        protocol.setAction(accion);

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    /**
     * Uso del Gson para procesar el envio de las solicitudes al servidor
     *
     * @param jsonListarCustomer
     * @return lista de tipo Customer.
     */
    private List<Customer> jsonListarCustomer(String jsonListarCustomer) {
        Gson gson = new Gson();
        Type list = new TypeToken<List<Customer>>() {
        }.getType();
        return gson.fromJson(jsonListarCustomer, list);
    }

// Fin lista Clientes
    private String procesarConexion(String requestJson) throws Exception {
        String jsonResponse = null;
        try {
            //se establece la conexion
            mySocket.connect();
            //se envia la solicitud y se recibe una respuesta,
            //(CREO)AQUI VALIDAR SI SE DIO CON EXITO LA OPERACION, SEGUN LA REPUESTA DEL SERVIDOR
            jsonResponse = mySocket.sendStream(requestJson);
            mySocket.closeStream();
            mySocket.disconnect();
            if (jsonResponse.equals("FALLO")) {
                return "FALLO";
            } else {
                System.out.println("todo normal");
            }
        } catch (IOException ex) {
            ex.getMessage();
        }
        if (jsonResponse == null) {
            throw new Exception("no se pudo conectar al servidor");
        } else {
            if (jsonResponse.contains("error")) {
                //Devolvió algún erroR, usar mejor login
                System.out.println("hubo algun tipo de error");
                throw new Exception(this.extractMessages(jsonResponse));
            } else {
                //Devuelve la respuesta del servidor
                return jsonResponse;
            }
        }
    }

    /**
     * Realiza el proceso de conexion entre el cliente y el servidor, y
     * deacuerdo su estado se obtendra el exito o fallo de la solicitud de
     * obtencion de los restaurantes.
     *
     * @return lista de tipo Restaruant
     * @throws Exception
     */
    @Override
    public List<Restaurant> listarRestaurants() throws Exception {
        String resource = "comprador";
        String accion = "listarRestaurantes";
        String requestJson = createlistComponent(resource, accion);
        String response = procesarConexion(requestJson);
        return jsonListarRestaurants(response);
    }

    /**
     * Crea el Restaurnate recibido en un json para el envio por el sockect al
     * servidor
     *
     * @param resource fuente (quien hace la socitud)
     * @param accion accion a ejecutar en servidor.
     * @return
     */
    private String createlistRestaurants(String resource, String accion) {
        Protocol protocol = new Protocol();
        protocol.setResource(resource);
        protocol.setAction(accion);

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    /**
     * Obtiene la lista de tipo Restaurant resiviendo el estado de la conexion
     * para su posterior envio al servidor.
     *
     * @param jsonListarRestaurant la accion que se esta solicitando
     * @return lista de tipo Restaurant.
     */
    private List<Restaurant> jsonListarRestaurants(String jsonListarRestaurant) {
        Gson gson = new Gson();
        Type list = new TypeToken<List<Restaurant>>() {
        }.getType();
        return gson.fromJson(jsonListarRestaurant, list);
    }

    /**
     * Realiza el proceso de conexion entre el cliente y el servidor, y
     * deacuerdo su estado se obtendra el exito o fallo de la solicitud de
     * actualizacion del plato ejecutivo.
     *
     * @param IDplatoEJ id del plato ejecutivo
     * @param ResNIT id del restaurante al cual pertenece el plato
     * @param diaSemana dia de la semana en el que se brinda el plato
     * @param entrada componente entrada
     * @param principio componente principio
     * @param proteina componente proteina
     * @param bebida componente bebida
     * @param precio costo del plato ejecutivo
     * @return true en caso de extio o false en caso contrario.
     * @throws Exception
     */
    @Override
    public boolean updateAlmuerzoEjecutivoDia(String IDplatoEJ, String ResNIT, String diaSemana, String entrada, String principio, String proteina, String bebida, String precio) throws Exception {

        String requestJson = updatePlatoEjecutivoJson(IDplatoEJ, ResNIT, diaSemana, entrada, principio, proteina, bebida, precio);
        if (processConnection(requestJson).equals("FALLO")) {
            return false;
        }
        return true;
    }

    /**
     * Crea el AlmuerzoEjecutivo recibido en un json para el envio por el sockect al
     * servidor
     * @param IDplatoEJ id del plato ejecutivo
     * @param ResNIT id del restaurante al cual pertenece el plato
     * @param diaSemana dia de la semana en el que se brinda el plato
     * @param entrada componente entrada
     * @param principio componente principio
     * @param proteina componente proteina
     * @param bebida componente bebida
     * @param precio costo del plato ejecutivo
     * @return
     */
    private String updatePlatoEjecutivoJson(String IDplatoEJ, String ResNIT, String diaSemana, String entrada, String principio, String proteina, String bebida, String precio) {
        Protocol protocol = new Protocol();
        //el orden debe ser respetado
        protocol.setResource("administrador");
        protocol.setAction("updatePlatoEjecutivo");
        protocol.addParameter("IDplatoEJ", IDplatoEJ);
        protocol.addParameter("ResNIT", ResNIT);
        protocol.addParameter("diaSemana", diaSemana);
        protocol.addParameter("entrada", entrada);
        protocol.addParameter("principio", principio);
        protocol.addParameter("proteina", proteina);
        protocol.addParameter("bebida", bebida);
        protocol.addParameter("precio", precio);

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json enviado: " + requestJson);
        return requestJson;
    }

    /**
     * Funcion que lista los platos ejecutivos declarando quien y cual es la
     * accion que se quiere ejecutar en este caso listarAlmuerzoEejcutivoDia.
     *
     * @return lista de PlatoEjecutivo.
     * @throws Exception
     */
    @Override
    public List<PlatoEjecutivo> listarAlmuerzoEjeDia() throws Exception {
        String resource = "administrador";
        String accion = "listarAlmuerzoEjeDia";
        String requestJson = createlistAlmuerzoEjeDia(resource, accion);
        String response = procesarConexion(requestJson);
        return jsonListarAlmuerzoEjeDia(response);
    }

    /**
     * Crea el PlatoEjecutivo recibido en un json para el envio por el sockect al
     * servidor. Haciendo uso de los protocolos
     *
     * @param resource quien solicita la accion
     * @param accion lo que se quiere ejecutar en el servidor.
     * @return
     */
    private String createlistAlmuerzoEjeDia(String resource, String accion) {
        Protocol protocol = new Protocol();
        protocol.setResource(resource);
        protocol.setAction(accion);

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    /**
     * Obtiene la lista de tipo PLatoEjecutivo resiviendo el estado de la
     * conexion para su posteror envio al servidor
     *
     * @param jsonListarAlEjeDiat la accion que se esta solicitando
     * @return lista de tipo PLatoEjecutivo.
     */
    private List<PlatoEjecutivo> jsonListarAlmuerzoEjeDia(String jsonListarAlEjeDiat) {
        Gson gson = new Gson();
        Type list = new TypeToken<List<PlatoEjecutivo>>() {
        }.getType();
        return gson.fromJson(jsonListarAlEjeDiat, list);
    }

}
