/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Bosko
 */
@Stateless
public class TakmicarFacade extends AbstractFacade<Takmicar> {

    @PersistenceContext(unitName = "CS230-Projekat-Bosko-Markovic-4003PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TakmicarFacade() {
        super(Takmicar.class);
    }
    
}
