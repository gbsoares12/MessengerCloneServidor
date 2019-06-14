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
public class ListaClienteVerificador {

    List<ClienteVerificador> listaClientesComVerificadorConectados = new ArrayList<>();
    private static ListaClienteVerificador instance;//Padrão Singletonz

    public synchronized static ListaClienteVerificador getInstance() {//Padrão Singleton
        if (instance == null) {
            instance = new ListaClienteVerificador();
        }
        return instance;
    }

    public List<ClienteVerificador> getListaClientesComVerificadorConectados() {
        return listaClientesComVerificadorConectados;
    }
    
    public void addClienteVerificador(ClienteVerificador cliente){
        this.listaClientesComVerificadorConectados.add(cliente);
    }
    
    public void removeClienteVerificador(ClienteVerificador cliente){
        this.listaClientesComVerificadorConectados.remove(cliente);
    }
}
