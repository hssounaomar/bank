/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controllersJpa.AdminJpaController;
import entites.Admin;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ideal-Info
 */
public class AdminDAO {
     private  EntityManagerFactory emf;
private AdminJpaController jpaController;
    public AdminDAO() {
        emf=Persistence.createEntityManagerFactory("BankPU2");
        jpaController=new AdminJpaController(emf);
    }
     public Admin login(String email,String password){
       return jpaController.login(email, password);
    }
}
