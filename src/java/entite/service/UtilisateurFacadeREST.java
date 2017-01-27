/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entite.service;

import entite.Utilisateur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
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
@Path("entite.utilisateur")
public class UtilisateurFacadeREST extends AbstractFacade<Utilisateur> {

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    public UtilisateurFacadeREST() {
        super(Utilisateur.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Utilisateur entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Utilisateur entity) {
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
    public Utilisateur find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Utilisateur> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Utilisateur> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("verifier/{login}/{password}")
    @Produces(MediaType.TEXT_PLAIN)
    public Boolean verifier(@PathParam("login") String login, @PathParam("password") String password) {
        
        ///////////////////////////////////////////////////////////////////////
        // équivalent de SELECT password FROM utilisateur WHERE login = :login;
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Utilisateur> v = cq.from(Utilisateur.class);
        cq.where(cb.equal(v.get("login"), login));
        cq.select(v.get("password"));
        String pwd = em.createQuery(cq).getSingleResult();
        ///////////////////////////////////////////////////////////////////////
        
        return password.equals(pwd);
    }
    
    @GET
    @Path("login/{login}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Utilisateur findByLogin(@PathParam("login") String login) {
        
        ///////////////////////////////////////////////////////////////////////
        // équivalent de SELECT * FROM utilisateur WHERE login = :login;
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Utilisateur> cq = cb.createQuery(Utilisateur.class);
        Root<Utilisateur> v = cq.from(Utilisateur.class);
        ParameterExpression<String> p = cb.parameter(String.class);
        cq.where(cb.equal(v.get("login"), login));
        cq.select(v);
        return em.createQuery(cq).getSingleResult();
        ///////////////////////////////////////////////////////////////////////
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
