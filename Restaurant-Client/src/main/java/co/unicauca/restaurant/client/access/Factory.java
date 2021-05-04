/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client.access;

import co.unicauca.restaurant.commons.infra.Utilities;

/**
 *
 * @author braia
 */
public class Factory {

    private static Factory instance;

    private Factory() {

    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    /**
     * crea una clase concreta de ClientAccessSocket que realiza la comunicacion
     *
     * @return
     */
    public IClientAccess getClienteService() {
        IClientAccess result = null;
        String type = Utilities.loadProperty("customer.service");
        switch (type) {
            case "default":
                result = new ClientAccessSocket();
                break;
        }
        return result;
    }
}
