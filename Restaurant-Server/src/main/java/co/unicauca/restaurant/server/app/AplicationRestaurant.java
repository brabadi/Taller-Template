package co.unicauca.restaurant.server.app;

import co.unicauca.restaurant.server.infra.RestaurantServerSocket;
import co.unicauca.serversocket.serversockettemplate.infra.ServerSocketTemplate;

/**
 *
 * @author braia
 */
public class AplicationRestaurant {

    /**
     * En la funcion main se crea el socket.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //se crea el socket
        ServerSocketTemplate server = new RestaurantServerSocket();
        //se inicia
        server.startServer();
    }
}