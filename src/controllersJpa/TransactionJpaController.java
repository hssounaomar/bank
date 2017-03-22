/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllersJpa;

import bank.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entites.Compte;
import entites.Transaction;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ideal-Info
 */
public class TransactionJpaController implements Serializable {

    public TransactionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaction transaction) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compte numE = transaction.getNumE();
            if (numE != null) {
                numE = em.getReference(numE.getClass(), numE.getId());
                transaction.setNumE(numE);
            }
            Compte numR = transaction.getNumR();
            if (numR != null) {
                numR = em.getReference(numR.getClass(), numR.getId());
                transaction.setNumR(numR);
            }
            em.persist(transaction);
            if (numE != null) {
                numE.getTransactionList().add(transaction);
                numE = em.merge(numE);
            }
            if (numR != null) {
                numR.getTransactionList().add(transaction);
                numR = em.merge(numR);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaction transaction) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaction persistentTransaction = em.find(Transaction.class, transaction.getId());
            Compte numEOld = persistentTransaction.getNumE();
            Compte numENew = transaction.getNumE();
            Compte numROld = persistentTransaction.getNumR();
            Compte numRNew = transaction.getNumR();
            if (numENew != null) {
                numENew = em.getReference(numENew.getClass(), numENew.getId());
                transaction.setNumE(numENew);
            }
            if (numRNew != null) {
                numRNew = em.getReference(numRNew.getClass(), numRNew.getId());
                transaction.setNumR(numRNew);
            }
            transaction = em.merge(transaction);
            if (numEOld != null && !numEOld.equals(numENew)) {
                numEOld.getTransactionList().remove(transaction);
                numEOld = em.merge(numEOld);
            }
            if (numENew != null && !numENew.equals(numEOld)) {
                numENew.getTransactionList().add(transaction);
                numENew = em.merge(numENew);
            }
            if (numROld != null && !numROld.equals(numRNew)) {
                numROld.getTransactionList().remove(transaction);
                numROld = em.merge(numROld);
            }
            if (numRNew != null && !numRNew.equals(numROld)) {
                numRNew.getTransactionList().add(transaction);
                numRNew = em.merge(numRNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaction.getId();
                if (findTransaction(id) == null) {
                    throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaction transaction;
            try {
                transaction = em.getReference(Transaction.class, id);
                transaction.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.", enfe);
            }
            Compte numE = transaction.getNumE();
            if (numE != null) {
                numE.getTransactionList().remove(transaction);
                numE = em.merge(numE);
            }
            Compte numR = transaction.getNumR();
            if (numR != null) {
                numR.getTransactionList().remove(transaction);
                numR = em.merge(numR);
            }
            em.remove(transaction);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaction> findTransactionEntities() {
        return findTransactionEntities(true, -1, -1);
    }

    public List<Transaction> findTransactionEntities(int maxResults, int firstResult) {
        return findTransactionEntities(false, maxResults, firstResult);
    }

    private List<Transaction> findTransactionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaction.class));
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

    public Transaction findTransaction(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaction.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransactionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaction> rt = cq.from(Transaction.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
