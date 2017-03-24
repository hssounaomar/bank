/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bank.exceptions.IllegalOrphanException;
import bank.exceptions.NonexistentEntityException;
import controllersJpa.CompteJpaController;
import entites.Compte;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ideal-Info
 */
public class CompteDAO {
      private  EntityManagerFactory emf;
private CompteJpaController jpaController;
    public CompteDAO() {
        emf=Persistence.createEntityManagerFactory("BankPU2");
        jpaController=new CompteJpaController(emf);
    }
    public List<Compte> getComptes(){
        return jpaController.findCompteEntities();
         
    }
    public void addCompte(Compte comp){
        jpaController.create(comp);
    }
    public void updateCompte(Compte comp) throws NonexistentEntityException, Exception{
        jpaController.edit(comp);
    }
    public void deleteCompte(int id) throws IllegalOrphanException, NonexistentEntityException{
        jpaController.destroy(new Integer(id));
    }
}
