/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entite.service;

import entite.Formule;
import entite.Frequence;
import entite.Kms;
import entite.Vehicule;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author merguez
 */
@Stateless
@Path("entite.vehicule")
public class VehiculeFacadeREST extends AbstractFacade<Vehicule> {

    @EJB
    private FormuleFacadeREST formuleFacadeREST;

    @EJB
    private KmsFacadeREST kmsFacadeREST;

    @EJB
    private FrequenceFacadeREST frequenceFacadeREST;

    private static final Double BASE = 60.0; // tarif de base servant au calcul de la prime
    private static final Double TRAVAIL = 1.2; // coefficient travail

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    public VehiculeFacadeREST() {
        super(Vehicule.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Vehicule entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Vehicule entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Vehicule find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehicule> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehicule> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("marques")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehicule> findAllMarques() {

        //////////////////////////////////////////////////////
        // équivalent de SELECT DISTINCT marque FROM vehicule;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Vehicule> v = cq.from(Vehicule.class);
        cq.select(v.get("marque")).distinct(true);
        List<String> marques = em.createQuery(cq).getResultList();

        //////////////////////////////////////////////////////
        // Création de la liste des marques à envoyer
        List<Vehicule> vehicules = new ArrayList<>();
        for (String marque : marques) {
            Vehicule vehicule = new Vehicule();
            vehicule.setMarque(marque);
            vehicules.add(vehicule);
        }

        return vehicules;
    }

    @GET
    @Path("{marque}/modeles")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehicule> findAllModelesByMarque(@PathParam("marque") String marque) {

        /////////////////////////////////////////////////////////////////////////////
        // équivalent de SELECT DISTINCT modele FROM vehicule WHERE marque = :marque;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Vehicule> v = cq.from(Vehicule.class);
        cq.where(cb.equal(v.get("marque"), marque));
        cq.select(v.get("modele")).distinct(true);
        List<String> modeles = em.createQuery(cq).getResultList();

        /////////////////////////////////////////////////////////////////////////////
        // Création de la liste des modèles à envoyer
        List<Vehicule> vehicules = new ArrayList<>();
        for (String modele : modeles) {
            Vehicule vehicule = new Vehicule();
            vehicule.setModele(modele);
            vehicules.add(vehicule);
        }

        return vehicules;
    }

    @GET
    @Path("{marque}/{modele}/versions")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehicule> findAllVersionByModele(@PathParam("marque") String marque, @PathParam("modele") String modele) {

        ////////////////////////////////////////////////////////////////////////////////////
        // équivalent de SELECT * FROM vehicule WHERE marque = :marque AND modele = :modele;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Vehicule> cq = cb.createQuery(Vehicule.class);
        Root<Vehicule> v = cq.from(Vehicule.class);
        cq.where(cb.and(cb.equal(v.get("marque"), marque), cb.equal(v.get("modele"), modele)));
        return em.createQuery(cq).getResultList();
    }

    @GET
    @Path("prime/{idVehicule}/{frequence}/{kms}/{travail}/{formule}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double calculPrime(@PathParam("idVehicule") String idVehicule,
            @PathParam("frequence") String freq,
            @PathParam("kms") String kms,
            @PathParam("travail") String travail,
            @PathParam("formule") String formule) {

        /////////////////////////////////////////////////////////////////
        // équivalent de SELECT coefficient FROM vehicule WHERE id = :id;
        Vehicule v = find(Long.parseLong(idVehicule));
        Double coefVehicule = v.getCoefficient();
        
        /////////////////////////////////////////////////////////////////
        // équivalent de SELECT coefficient FROM frequence WHERE id = :id;
        Frequence f = frequenceFacadeREST.find(Long.parseLong(freq));
        Double coefFrequence = f.getCoefficient();
        
        /////////////////////////////////////////////////////////////////
        // équivalent de SELECT coefficient FROM kms WHERE id = :id;
        Kms k = kmsFacadeREST.find(Long.parseLong(kms));
        Double coefKms = k.getCoefficient();
        
        /////////////////////////////////////////////////////////////////
        // équivalent de SELECT coefficient FROM formule WHERE id = :id;
        Formule form = formuleFacadeREST.find(Long.parseLong(formule));
        Double coefForm = form.getCoefficient();
        /////////////////////////////////////////////////////////////////

        return BASE * coefVehicule * coefFrequence * coefKms * (Boolean.parseBoolean(travail) ? TRAVAIL : 1) * coefForm;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
