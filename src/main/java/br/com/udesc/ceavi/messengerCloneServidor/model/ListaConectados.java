/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel Soares
 */
public class ListaConectados {

    private static ListaConectados instance;
    private List<Cliente> listaUsuarios = new ArrayList<>();
            
    public static ListaConectados getInstance() {
        if (instance == null) {
            instance = new ListaConectados();
        }

        return instance;
    }

    public List<Cliente> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Cliente> listaClientes) {
        this.listaUsuarios = listaClientes;
    }

    public ListaConectados() {
    }

    public void addCliente(Cliente cli) {
        this.listaUsuarios.add(cli);
    }
    
    public void removerCliente(Cliente cli){
        this.listaUsuarios.remove(cli);
    }
}
