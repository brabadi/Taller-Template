
package co.unicauca.restaurant.commons.domain;

/**
 *
 * @author braia
 */
public class Component {
    private String compID;
    private String compNombre;
    private ComponentType compTipo;

    public Component() {
    }

    public Component(String compID, String compNombre, ComponentType compTipo) {
        this.compID = compID;
        this.compNombre = compNombre;
        this.compTipo = compTipo;
    }

    public String getCompID() {
        return compID;
    }

    public void setCompID(String compID) {
        this.compID = compID;
    }

    public String getCompNombre() {
        return compNombre;
    }

    public void setCompNombre(String compNombre) {
        this.compNombre = compNombre;
    }

    public ComponentType getCompTipo() {
        return compTipo;
    }

    public void setCompTipo(ComponentType compTipo) {
        this.compTipo = compTipo;
    }
}
