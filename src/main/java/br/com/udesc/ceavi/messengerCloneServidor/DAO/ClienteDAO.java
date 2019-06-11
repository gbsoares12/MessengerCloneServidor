/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.DAO;

import br.com.udesc.ceavi.messengerCloneServidor.model.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Gabriel Soares
 */
public class ClienteDAO {

    public static Object ler(Class classe, String email) {
        Object object = null;
        EntityManagerFactory emf
                = javax.persistence.Persistence.createEntityManagerFactory("persistenciaMenssengerClone");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            object = em.find(classe, email);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return object;
    }

    public static void salvar(Object objeto) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenciaMenssengerClone");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        try {
            em.persist(objeto);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static Cliente update(Cliente cliente) {
        EntityManagerFactory emf
                = javax.persistence.Persistence.createEntityManagerFactory("persistenciaMenssengerClone");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            em.merge(cliente);
            em.getTransaction().commit();
            return cliente;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return null;
    }

    public static void excluir(Object objeto) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenciaMenssengerClone");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {

            em.remove(objeto);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {

            em.close();

        }
    }

    public static Cliente validarLogin(String email, String senha) throws Exception {
        EntityManagerFactory emf
                = javax.persistence.Persistence.createEntityManagerFactory("persistenciaMenssengerClone");
        EntityManager em = emf.createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT c FROM Cliente c WHERE c.email=:email and c.senha=:senha");
            consulta.setParameter("email", email);
            consulta.setParameter("senha", senha);
            Cliente clienteBuscado = (Cliente) consulta.getSingleResult();
            return clienteBuscado;
        } catch (NoResultException ex) {
            throw new Exception("Falha na autenticação!");
        }
    }

    public static Cliente buscaUser(String email) throws Exception {
        EntityManagerFactory emf
                = javax.persistence.Persistence.createEntityManagerFactory("persistenciaMenssengerClone");
        EntityManager em = emf.createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT c FROM Cliente c WHERE c.email=:email");
            consulta.setParameter("email", email);
            Cliente clienteBuscado = (Cliente) consulta.getSingleResult();
            return clienteBuscado;
        } catch (NoResultException ex) {
            throw new Exception("Falha na busca!");
        }
    }

}
