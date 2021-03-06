/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllersJpa;


import bank.exceptions.IllegalOrphanException;
import bank.exceptions.NonexistentEntityException;
import entites.Compte;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entites.Transaction;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ideal-Info
 */
public class CompteJpaController implements Serializable {

    public CompteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compte compte) {
        if (compte.getTransactionList() == null) {
            compte.setTransactionList(new ArrayList<Transaction>());
        }
        if (compte.getTransactionList1() == null) {
            compte.setTransactionList1(new ArrayList<Transaction>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Transaction> attachedTransactionList = new ArrayList<Transaction>();
            for (Transaction transactionListTransactionToAttach : compte.getTransactionList()) {
                transactionListTransactionToAttach = em.getReference(transactionListTransactionToAttach.getClass(), transactionListTransactionToAttach.getId());
                attachedTransactionList.add(transactionListTransactionToAttach);
            }
            compte.setTransactionList(attachedTransactionList);
            List<Transaction> attachedTransactionList1 = new ArrayList<Transaction>();
            for (Transaction transactionList1TransactionToAttach : compte.getTransactionList1()) {
                transactionList1TransactionToAttach = em.getReference(transactionList1TransactionToAttach.getClass(), transactionList1TransactionToAttach.getId());
                attachedTransactionList1.add(transactionList1TransactionToAttach);
            }
            compte.setTransactionList1(attachedTransactionList1);
            em.persist(compte);
            for (Transaction transactionListTransaction : compte.getTransactionList()) {
                Compte oldNumEOfTransactionListTransaction = transactionListTransaction.getNumE();
                transactionListTransaction.setNumE(compte);
                transactionListTransaction = em.merge(transactionListTransaction);
                if (oldNumEOfTransactionListTransaction != null) {
                    oldNumEOfTransactionListTransaction.getTransactionList().remove(transactionListTransaction);
                    oldNumEOfTransactionListTransaction = em.merge(oldNumEOfTransactionListTransaction);
                }
            }
            for (Transaction transactionList1Transaction : compte.getTransactionList1()) {
                Compte oldNumROfTransactionList1Transaction = transactionList1Transaction.getNumR();
                transactionList1Transaction.setNumR(compte);
                transactionList1Transaction = em.merge(transactionList1Transaction);
                if (oldNumROfTransactionList1Transaction != null) {
                    oldNumROfTransactionList1Transaction.getTransactionList1().remove(transactionList1Transaction);
                    oldNumROfTransactionList1Transaction = em.merge(oldNumROfTransactionList1Transaction);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compte compte) throws IllegalOrphanException, NonexistentEntityException, Exception {
       EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            compte = em.merge(compte);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compte.getId();
                if (findCompte(id) == null) {
                    throw new NonexistentEntityException("The Compte with Num " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compte compte;
            try {
                compte = em.getReference(Compte.class, id);
                compte.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compte with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Transaction> transactionListOrphanCheck = compte.getTransactionList();
            for (Transaction transactionListOrphanCheckTransaction : transactionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compte (" + compte + ") cannot be destroyed since the Transaction " + transactionListOrphanCheckTransaction + " in its transactionList field has a non-nullable numE field.");
            }
            List<Transaction> transactionList1OrphanCheck = compte.getTransactionList1();
            for (Transaction transactionList1OrphanCheckTransaction : transactionList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compte (" + compte + ") cannot be destroyed since the Transaction " + transactionList1OrphanCheckTransaction + " in its transactionList1 field has a non-nullable numR field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(compte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compte> findCompteEntities() {
        return findCompteEntities(true, -1, -1);
    }

    public List<Compte> findCompteEntities(int maxResults, int firstResult) {
        return findCompteEntities(false, maxResults, firstResult);
    }

    private List<Compte> findCompteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compte.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Compte findCompte(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compte.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compte> rt = cq.from(Compte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
