
package co.unicauca.restaurant.commons.domain;

/**
 *
 * @author Rivera
 */
public class Customer {
    private int cus_id;
    private String cus_nombre;
    private String cus_direccion;
    private String cus_password;
    private String cus_Telefono;
    private String cus_tipo;

    public Customer() {
    }

    public Customer(int cus_id, String cus_nombre, String cus_direccion, String cus_password, String cus_Telefono, String cus_tipo) {
        this.cus_id = cus_id;
        this.cus_nombre = cus_nombre;
        this.cus_direccion = cus_direccion;
        this.cus_password = cus_password;
        this.cus_tipo = cus_tipo;
        this.cus_Telefono = cus_Telefono;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public String getCus_nombre() {
        return cus_nombre;
    }

    public void setCus_nombre(String cus_nombre) {
        this.cus_nombre = cus_nombre;
    }

    public String getCus_direccion() {
        return cus_direccion;
    }

    public void setCus_direccion(String cus_direccion) {
        this.cus_direccion = cus_direccion;
    }

    public String getCus_password() {
        return cus_password;
    }

    public void setCus_password(String cus_password) {
        this.cus_password = cus_password;
    }

    public String getCus_tipo() {
        return cus_tipo;
    }

    public void setCus_tipo(String cus_tipo) {
        this.cus_tipo = cus_tipo;
    }

    public String getCus_Telefono() {
        return cus_Telefono;
    }

    public void setCus_Telefono(String cus_Telefono) {
        this.cus_Telefono = cus_Telefono;
    }
    
    
    
    
    
}
