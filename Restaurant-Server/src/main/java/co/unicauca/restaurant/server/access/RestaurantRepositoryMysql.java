package co.unicauca.restaurant.server.access;

import co.unicauca.restaurant.commons.domain.Component;
import co.unicauca.restaurant.commons.domain.ComponentType;
import co.unicauca.restaurant.commons.domain.Customer;
import co.unicauca.restaurant.commons.domain.DiaEnum;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;
import co.unicauca.restaurant.commons.infra.Utilities;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author braia
 */
public class RestaurantRepositoryMysql implements IRestaurantRepository {

    /**
     * Conección con Mysql
     */
    private Connection conn;

    public RestaurantRepositoryMysql() {

    }

    /**
     * Permite hacer la conexion con la base de datos
     *
     * @return
     */
    public int connect() {
        try {
            Class.forName(Utilities.loadProperty("server.db.driver"));
            //crea una instancia de la controlador de la base de datos
            //estos datos estan quemados en el archivo propertis, si la base de datos cambia propertis debe modificarse
            String url = Utilities.loadProperty("server.db.url");
            String username = Utilities.loadProperty("server.db.username"); //usuario de la base de datos
            String pwd = Utilities.loadProperty("server.db.password");//contraseña de usuario
            //se establece la coneccion con los datos previos
            conn = DriverManager.getConnection(url, username, pwd);
            if (conn == null) {
                System.out.println("coneccion fallida a la base de datos");
            } else {
                System.out.println("conecion exitosa a la base de datos");
            }
            return 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al consultar Customer de la base de datos", ex);
        }
        return -1;
    }

    /**
     * Cierra la conexion con la base de datos
     *
     */
    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.FINER, "Error al cerrar Connection", ex);
        }
    }

    /**
     * Guarda un componente mediante la consulta realizada en labase de datos
     * posteriormente se guarda en cada columna de la tabla Component el
     * atributo del componente llegado por parametro.
     *
     * @param component
     * @return
     */
    @Override
    public String saveComponent(Component component) {
        System.out.println("ingreso a guardar componente");
        try {
            this.connect();
            String sql = "INSERT INTO componentes(COMP_ID,COMP_NOMBRE,COMP_TIPO) VALUES (?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.valueOf(component.getCompID()));
            pstmt.setString(2, component.getCompNombre());
            pstmt.setString(3, String.valueOf(component.getCompTipo()));

            pstmt.executeUpdate();
//            se cierra
            pstmt.close();
//            se termina la coneccion
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al insertar el registro", ex);
        }
        return component.getCompNombre();

    }

    /**
     * Mediante la consulta se saca el componente con id que llega por parametro
     * y este es guardado en una variable de tipo Componente para ser convertido
     * en Json y ser enviado mediante el socket.
     *
     * @param id_component
     * @return
     */
    @Override
    public Component finComponent(String id_component) {
        Component component = null;

        this.connect();
        try {
            String sql = "SELECT * from componentes where COMP_ID=? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.valueOf(id_component));
            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                component = new Component();
                component.setCompID(res.getString("COMP_ID"));
                component.setCompNombre(res.getString("COMP_NOMBRE"));
                component.setCompTipo(ComponentType.valueOf(res.getString("COMP_TIPO")));

            }
            pstmt.close();
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al consultar Customer de la base de datos", ex);
        }
        return component;
    }

    /**
     * Lista los Componentes desde la consulta hecha a la base de datos y añade
     * las tuplas encontradas en una lista de Component y convierte la lista en
     * json para enviarla por el sockect devuelta al cliente
     *
     * @return
     */
    @Override
    public String listarComponentes() {
        List<Component> list = new ArrayList<>();
        String response = null;
        try {
            this.connect();
            String sql = "select * from componentes ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Component comp = new Component(rs.getString(1), rs.getString(2), ComponentType.valueOf(rs.getString(3)));
                list.add(comp);
            }
            response = listToCompoJson(list);
            //se cierra
            pstmt.close();
            //se termina la coneccion
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al listar Componentes", ex);
        }
        return response;
    }

    /**
     * Convierte una lista de tipo PlatoEjecutivo en un json
     *
     *
     * @param list
     * @return
     */
    private String listToCompoJson(List<Component> list) {
        Gson gson = new Gson();
        String response = gson.toJson(list);
        return response;
    }

    /**
     * Lista los restaurantes desde la consulta hecha a la base de datos y añade
     * las tuplas encontradas en una lista de Restaurant y convierte la lista en
     * json para enviarla por el sockect devuelta al cliente
     *
     * @return
     */
    @Override
    public String listarRestaurantes() {
        List<Restaurant> list = new ArrayList<>();
        String response = null;
        try {
            this.connect();
            String sql = "select * from restaurante";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCus_id(Integer.valueOf(rs.getString(2)));
                Restaurant restaurant = new Restaurant(Integer.valueOf(rs.getString(1)), customer, rs.getString(3), rs.getString(4));
                list.add(restaurant);
            }
            response = listToRestaurantJson(list);
            //se cierra
            pstmt.close();
            //se termina la coneccion
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al listar Restaurante", ex);
        }
        return response;
    }

    /**
     * Convierte una lista de tipo Restaurant en un json
     *
     * @param list
     * @return
     */
    private String listToRestaurantJson(List<Restaurant> list) {
        Gson gson = new Gson();
        String response = gson.toJson(list);
        return response;
    }

    /**
     * Con la consulta retorna cada tupla de la tabla Customer y posteriormente
     * para ser guardado en una lista de Customer y ser convertido en un Json y
     * ser enviado mediante el socket.
     *
     * @return
     */
    @Override
    public String findCustomer() {

        List<Customer> list = new ArrayList<>();
        String response = null;
        try {
            this.connect();
            String sql = "select * from cliente";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(Integer.valueOf(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                list.add(customer);
            }
            response = listToCustomerJson(list);
            //se cierra
            pstmt.close();
            //se termina la coneccion
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al buscar Cliente", ex);
        }
        return response;
    }

    /**
     * Convierte una lista de tipo Customer en un json
     *
     * @param list
     * @return
     */
    private String listToCustomerJson(List<Customer> list) {
        Gson gson = new Gson();
        String response = gson.toJson(list);
        return response;
    }

    /**
     * Mediante la consulta hecha en la base de datos actualia cada uno de lo
     * atributos del plato por los valores que llegan por parametro.
     *
     * @param IDplatoEJ id del plato ejecutivo
     * @param ResNIT id del restaurante al cual pertenece el plato.
     * @param diaSemana dia de la semana.
     * @param AlmEjeDia_comp1 componente de tipo entrada
     * @param AlmEjeDia_comp2 componente de tipo princicpio
     * @param AlmEjeDia_comp3 componente de tipo proteina
     * @param AlmEjeDia_comp4 componente de tipo bebida.
     * @param precio es costo del plato.
     * @return
     */
    @Override
    public String updatePlatoEjecutivo(String IDplatoEJ, String ResNIT, String diaSemana, String AlmEjeDia_comp1, String AlmEjeDia_comp2, String AlmEjeDia_comp3, String AlmEjeDia_comp4, String precio) {
        if (!this.findPlatoEjecutivo(IDplatoEJ)) {
            return "FALLO";
        }

        System.out.println("ATRIBUTOS: " + IDplatoEJ + "- " + ResNIT + "- " + diaSemana + "- " + AlmEjeDia_comp1 + "- " + AlmEjeDia_comp2 + "- " + AlmEjeDia_comp3 + "- " + AlmEjeDia_comp4 + "- " + precio);

        boolean componente_1 = actualizarAtributo("AlmEjeDia_comp1", ResNIT, AlmEjeDia_comp1);
        System.out.println("componente " + componente_1);
        boolean componente_2 = actualizarAtributo("AlmEjeDia_comp2", ResNIT, AlmEjeDia_comp2);
        System.out.println("componente " + componente_2);
        boolean componente_3 = actualizarAtributo("AlmEjeDia_comp3", ResNIT, AlmEjeDia_comp3);
        System.out.println("componente " + componente_3);
        boolean componente_4 = actualizarAtributo("AlmEjeDia_comp4", ResNIT, AlmEjeDia_comp4);
        System.out.println("componente " + componente_4);
        boolean diaEnum = actualizarAtributo("AlmEjeDia_DIASEM", ResNIT, diaSemana);
        System.out.println("componente " + diaEnum);
        boolean result_precio = actualizarAtributo("AlmEjeDia_precio", ResNIT, precio);
        System.out.println("componente " + result_precio);
        if (componente_1 && componente_2 && componente_3 && componente_4 && diaEnum && result_precio) {
            return IDplatoEJ;
        }

        return "FALLO LA ACTUALIZACION";
    }

    /**
     * Encuentra un PlatoEjecutivo en la tabla almuerzoejedia mediante la
     * consulta.
     *
     * @param plaEjeDia
     * @return un boolean true en caso de encontrar el plato ejecutivo o false
     * en caso contrario.
     */
    private boolean findPlatoEjecutivo(String plaEjeDia) {
        boolean resultado;
        try {
            this.connect();

            String sql = "select AlmEjeDia_ID from almuerzoejedia where AlmEjeDia_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(plaEjeDia));
            ResultSet rs = ps.executeQuery();
            resultado = rs.next();
            ps.close();
            this.disconnect();
            return resultado;
        } catch (SQLException ex) {
            System.out.println("revento excepcion encontrar plato_:" + ex.getMessage());
            return false;
        }
    }

    /**
     * Funcion que tiene la consulta que se usa en updatePlatoEjcutivo para
     * poder actualizar en la base de datos, en ella actualiza solo una columna
     * es decir solo un solo atributo del plato ejecutivo.
     *
     * @param columna nombre de la columna en la cual se quiere modificar.
     * @param res_nit id del restaurante al cual pertenece el plato.
     * @param valor , valor por el cual se reemplaza en la base de datos.
     * @return
     */
    private boolean actualizarAtributo(String columna, String res_nit, String valor) {
        try {
            this.connect();
            //String sql = "UPDATE platoejecutivo set "+atributo+" = "+valor+" WHERE PEJE_NOMBRE = "+clave;
//                         "UPDATE almuerzoejedia SET AlmEjeDia_DIASEM = ? WHERE RES_NIT = ?";
            String sql = "UPDATE almuerzoejedia SET " + columna + " = ? WHERE RES_NIT = ?";
//            UPDATE almuerzoejedia SET AlmEjeDia_comp1 = 1 where RES_NIT = 126777 ;
            System.out.println("SENTENCIA SQL UPDATE PLATO EJECUTIVO: " + sql);
            PreparedStatement pstmt = conn.prepareStatement(sql);

            if (columna.equals("AlmEjeDia_DIASEM")) {
                pstmt.setString(1, valor);
            } else {
                pstmt.setInt(1, Integer.valueOf(valor));
            }
            pstmt.setInt(2, Integer.parseInt(res_nit));
            pstmt.executeUpdate();
            pstmt.close();
            this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al insertar el registro", ex);
            return false;
        }

    }

    /**
     *
     * Lista los platos del menu mediante la consulta realizada a la base de
     * datos añade las tuplas encontradas en una lista de PlatoEjecutivo y
     * convierte la lista en json para enviarla por el sockect devuelta al
     * cliente.
     *
     */
    @Override
    public String listarAlmuerzoEjeDia() {

        List<PlatoEjecutivo> list = new ArrayList<>();
        String response = null;
        try {
            this.connect();
            String sql = "select * from almuerzoejedia";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Component component_1 = new Component();
                component_1.setCompID(rs.getString(4));
                Component component_2 = new Component();
                component_2.setCompID(rs.getString(5));
                Component component_3 = new Component();
                component_3.setCompID(rs.getString(6));
                Component component_4 = new Component();
                component_4.setCompID(rs.getString(7));
                PlatoEjecutivo platoEje = new PlatoEjecutivo(rs.getInt(1), rs.getInt(2), DiaEnum.valueOf(rs.getString(3)), component_1, component_2, component_3, component_4, rs.getInt(8));
                list.add(platoEje);
            }
            response = listToPlatoEjeJson(list);
            //se cierra
            pstmt.close();
            //se termina la coneccion
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantRepositoryMysql.class.getName()).log(Level.SEVERE, "Error al listar los almuerzos ejcutivos del dia", ex);
        }
        return response;
    }

    /**
     * Convierte una lista de tipo PlatoEjecutivo en un json
     *
     * @param list
     * @return
     */
    private String listToPlatoEjeJson(List<PlatoEjecutivo> list) {
        Gson gson = new Gson();
        String response = gson.toJson(list);
        return response;
    }

}
