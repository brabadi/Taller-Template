/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.server.infra;

import co.unicauca.restaurant.commons.domain.Component;
import co.unicauca.restaurant.commons.domain.ComponentType;
import co.unicauca.restaurant.commons.domain.Customer;
import co.unicauca.restaurant.commons.domain.DiaEnum;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;
import co.unicauca.restaurant.commons.infra.JsonError;
import co.unicauca.restaurant.commons.infra.Protocol;
import co.unicauca.restaurant.commons.infra.Utilities;
import co.unicauca.restaurant.server.access.FactoryRepository;
import co.unicauca.restaurant.server.domain.server.RestaurantService;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import co.unicauca.restaurant.server.access.IRestaurantRepository;
import co.unicauca.serversocket.serversockettemplate.infra.ServerSocketTemplate;

/**
 *
 * @author braia
 */
public class RestaurantServerSocket extends ServerSocketTemplate {

    /**
     * servicio del plato, contiene el mecanismo para comunicar Restaurante y la
     * datos y sus operaciones
     */
    private RestaurantService service; //se debe inicializar obligatoriamente
    /**
     * servicio para el socket, "orejita"
     */
    private static ServerSocket ssock;
    /**
     * socket por donde se hace la peticion/respuesta
     */
    private static Socket socket;
    /**
     * permite leer un flujo de datos del socket
     */
    private Scanner input;
    /**
     * permite escibir un flujo de datos en el socket
     */
    private PrintStream output;

    /**
     * constructor obtiene el servidor mediante la fabrica instancia platoE
     * servicio pasando el repositorio obtenido
     */
    public RestaurantServerSocket() {
        //inyeccion de dependencias par hacer la inyeccion
        IRestaurantRepository repository = FactoryRepository.getInstance().getRepository();
        service = new RestaurantService(repository);
    }

    /**
     * instancia el server socket y abre el puerto respectivo
     */
    private static void openPort() {
        try {
            ssock = new ServerSocket(PORT);
            Logger.getLogger("Socket").log(Level.INFO, "Socket conectado");
        } catch (IOException ex) {
            Logger.getLogger(RestaurantServerSocket.class.getName()).log(Level.SEVERE, "Error del server socket al abrir el puerto", ex);
        }
    }

    /**
     * espera que el cliente se conecte y devuelve un socket
     */
    private static void waitToClient() {
        try {
            //en este punto el socket espera platoE que accept reciba una conexion
            socket = ssock.accept();
            //informara si hubo conexion
            Logger.getLogger("Socket").log(Level.INFO, "Socket conectado");
        } catch (IOException ex) {
            Logger.getLogger(RestaurantServerSocket.class.getName()).log(Level.SEVERE, "Eror al abrir un socket", ex);
        }
    }

    /**
     * lanza el hilo, un hilo individual atendera platoE el cliente
     */
    private static void throwThread() {
        new Thread(new RestaurantServerSocket()).start();
    }

    /**
     * arranca el servidor y hace la estructura completa
     */
    public void start() {
        openPort();
        while (true) {
            waitToClient();
            throwThread();
        }
    }

    /**
     * hilo que atiende platoE un cliente
     */
    @Override
    public void run() {
        try {
            //crea el flujo de datos, inicializa input y output, entrada y salida de datos
            createStreams();
            //crea el flujo de datos para la lectura del socket
            readStream();
            //cierra flujo
            closeStream();
        } catch (IOException ex) {
            Logger.getLogger(RestaurantServerSocket.class.getName()).log(Level.SEVERE, "Eror al leer el flujo", ex);
        }
    }

    /**
     * crea o inicializa los atributos encargados de leer y escribir, flujos con
     * el socket
     *
     * @throws IOException
     */
    private void createStreams() throws IOException {
        output = new PrintStream(socket.getOutputStream());
        input = new Scanner(socket.getInputStream());
    }

    /**
     * lee el flujo del socket
     */
    private void readStream() {
        if (input.hasNextLine()) {//verifica si hay contenido en el flujo de entrada
            String request = input.nextLine();
            processRequest(request);
        } else {
            output.flush();
            String errorJson = generateErrorJson();
            output.println(errorJson);
        }
    }

    /**
     * Procesar la solicitud que proviene de la aplicación Restaurant-Client
     *
     * @param requestJson petición que proviene del cliente socket en formato
     * json que viene de esta manera:
     * "{"resource":"customer","action":"get","parameters":[{"name":"id","value":"1"}]}"
     *
     */
    @Override
    protected void processRequest(String requestJson) {
        // Convertir la solicitud platoE objeto Protocol para poderlo procesar
        Gson gson = new Gson();
        Protocol protocolRequest = gson.fromJson(requestJson, Protocol.class);
        //saca de request la persona que ha hecho la solicitud, en nuestro caso administrador o comprador
        switch (protocolRequest.getResource()) {
            case "administrador":
                //se verifica el tipo de solicitud y se llama al metodo responsable
                //post informacion para guardar
                //palabra clave post

                if (protocolRequest.getAction().equals("postComponente")) {
                    this.administradorSaveComponente(protocolRequest);
                }
                if (protocolRequest.getAction().equals("findComponent")) {
                    this.administradorFinComponent(protocolRequest);
                }

                if (protocolRequest.getAction().equals("findCustomer")) {
                    encontrarAdministrador();
                }
                
                if (protocolRequest.getAction().equals("updatePlatoEjecutivo")) {
                    administradorUpdatePlatoEjecutivo(protocolRequest);
                }
                
                if (protocolRequest.getAction().equals("listarComponentes")) {
                    this.administradorListarComponentes();
                }
                if (protocolRequest.getAction().equals("listarAlmuerzoEjeDia")) {
                    this.administradorListarAlmuerzoEjeDia();
                }

                break;
            //comprador solo tendra la opcion de visualizar, es decir un selec sobre la base de datos y enviarlos platoE cliente
            case "comprador":
                Logger.getLogger(RestaurantServerSocket.class.getName()).log(Level.INFO.SEVERE, "solicitud comprador recibida");
               if (protocolRequest.getAction().equals("listarRestaurantes")) {
                    this.clienteListarRestaurants();
                }

                break;

        }

    }

    
    
    private void administradorFinComponent(Protocol protocol) {
        String clave = protocol.getParameters().get(0).getValue();
        Component component = service.findComponent(clave);
        if (component == null) {
            String errorJson = generateNotFoundErrorJson();
            output.println(errorJson);
        } else {
            output.println(objectToJSONComp(component));
        }
        output.println(component);
    }
    
    
     /**
     * Procesa la solicitud de guardar un componente que ha enviado el
     * cliente
     *
     * @param protocolRequest Protocolo de la solicitud
     */
    private void administradorSaveComponente(Protocol protocol) {
        Component component = new Component();
        component.setCompID(protocol.getParameters().get(0).getValue());
        component.setCompNombre(protocol.getParameters().get(1).getValue());
        component.setCompTipo(ComponentType.valueOf(protocol.getParameters().get(2).getValue()));
        String response = null;
        response = service.saveComponent(component);
        output.println(response);
        Logger.getLogger(RestaurantServerSocket.class.getName()).log(Level.SEVERE, "response: " + response + " ID componente:" + component.getCompID() + " Tipo:" + component.getCompTipo());
    }

   
    /**
     * Recibe un protocolo con la informacion necesaria para modificar el plato
     * del dia en la base de datos.
     *
     * @param protocol protocolo en formato Json
     */
    private void administradorUpdatePlatoEjecutivo(Protocol protocol) {
        String response = null;

        String IDplatoEJ = protocol.getParameters().get(0).getValue();
        String ResNIT = protocol.getParameters().get(1).getValue();
        String diaSemana = protocol.getParameters().get(2).getValue();
        String AlmEjeDia_comp1 = protocol.getParameters().get(3).getValue();
        String AlmEjeDia_comp2 = protocol.getParameters().get(4).getValue();
        String AlmEjeDia_comp3 = protocol.getParameters().get(5).getValue();
        String AlmEjeDia_comp4 = protocol.getParameters().get(6).getValue();
        String precio = protocol.getParameters().get(7).getValue();

        response = service.updatePlatoEjecutivo(IDplatoEJ, ResNIT, diaSemana, AlmEjeDia_comp1, AlmEjeDia_comp2, AlmEjeDia_comp3, AlmEjeDia_comp4, precio);
        output.println(response);
        Logger.getLogger(RestaurantServerSocket.class.getName()).log(Level.SEVERE, "response: " + response + " ID Plato:" + IDplatoEJ + " NIT Restaurante:" + ResNIT + " Dia Semana: " + diaSemana + "Componente 1" + AlmEjeDia_comp1 + "Componente 2" + AlmEjeDia_comp2 + "Componente 3" + AlmEjeDia_comp3 + "Componente 4" + AlmEjeDia_comp4 + " precio: " + precio);
    }

  
    /**
     * Procesa la solicitud de listar componentes  ha enviado el
     * cliente el cual se procesa en el repositorio del servidor para
     * finalmente retornar la lista de componentes en formato json.
     *
     */
    private void administradorListarComponentes() {
        String response;
        response = service.listarComponents();
        output.println(response);
    }
    
    
    /**
     * Procesa la solicitud de listar los clientes  ha enviado el
     * cliente el cual se procesa en el repositorio del servidor para
     * finalmente retornar la lista de customer en formato json.
     *
     */
    private void encontrarAdministrador() {
        String response;
        response = service.findCustomer();
        output.println(response);
    }
    
    
    /**
     * Procesa la solicitud de listar almuerso ejecutivo del dia que ha enviado el
     * cliente el cual se procesa en el repositorio del servidor para
     * finalmente retornar la lista de platoEjcutivo en formato json.
     *
     */
    private void administradorListarAlmuerzoEjeDia() {
        String response;
        response = service.listarAlmuerzoEjeDia();
        output.println(response);
    }

  
    /**
     * Ejecuta la accion del cliente de listar los restaurantes en la base de
     * datos, reotrnando la lista en formato json.
     */
    private void clienteListarRestaurants() {
        String response;
        response = service.listarRestaurantes();
        output.println(response);
    }

    
    /**
     * Convierte el objeto Customer customer json para que el servidor lo
     * envie como respuesta por el socket
     *
     * @param customer cliente procesado
     * @return Customer en formato json
     */
     private String objectToJSONCusto(Customer customer) {
        Gson gson = new Gson();
        String strObject = gson.toJson(customer);
        return strObject;
    }

    
    /**
     * Convierte el objeto Component component json para que el servidor lo
     * envie como respuesta por el socket
     *
     * @param component componente procesado
     * @return Componente en formato json
     */
    private String objectToJSONComp(Component component) {
        Gson gson = new Gson();
        String strObject = gson.toJson(component);
        return strObject;
    }

    /**
     * Genera un ErrorJson genérico
     *
     * @return error en formato json
     */
    private String generateErrorJson() {
        List<JsonError> errors = new ArrayList<>();
        JsonError error = new JsonError();
        error.setCode("400");
        error.setError("BAD_REQUEST");
        error.setMessage("Error en la solicitud");
        errors.add(error);

        Gson gson = new Gson();
        String errorJson = gson.toJson(errors);

        return errorJson;
    }

    /**
     * Genera un ErrorJson de cliente no encontrado
     *
     * @return error en formato json
     */
    private String generateNotFoundErrorJson() {
        List<JsonError> errors = new ArrayList<>();
        JsonError error = new JsonError();
        error.setCode("404");
        error.setError("NOT_FOUND");
        error.setMessage("Cliente no encontrado. Cédula no existe");
        errors.add(error);

        Gson gson = new Gson();
        String errorsJson = gson.toJson(errors);

        return errorsJson;
    }

    /**
     * Cierra los flujos de entrada y salida
     *
     * @throws IOException
     */
    private void closeStream() throws IOException {
        output.close();
        input.close();
        socket.close();
    }
    
    public RestaurantService getService() {
        return service;
    }

    public void setService(RestaurantService service) {
        this.service = service;
    }

    @Override
    protected ServerSocketTemplate init() {
        PORT = Integer.parseInt(Utilities.loadProperty("server.port"));
        IRestaurantRepository repository = FactoryRepository.getInstance().getRepository();
        this.setService(new RestaurantService(repository));
        this.start();
        return this;
    }
}