/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entite.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author merguez
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(entite.service.ContactFacadeREST.class);
        resources.add(entite.service.ContratFacadeREST.class);
        resources.add(entite.service.FormuleFacadeREST.class);
        resources.add(entite.service.FrequenceFacadeREST.class);
        resources.add(entite.service.KmsFacadeREST.class);
        resources.add(entite.service.TypeDemandeFacadeREST.class);
        resources.add(entite.service.UtilisateurFacadeREST.class);
        resources.add(entite.service.UtilisationFacadeREST.class);
        resources.add(entite.service.VehiculeFacadeREST.class);
    }
    
}
