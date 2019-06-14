/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.model;

/**
 *
 * @author Gabriel Soares
 */
public class ClienteVerificador {

    private int countVerificadorCliente = 0;
    private final Cliente cliente;

    public ClienteVerificador(Cliente cliente, int countCliente) {
        this.countVerificadorCliente = countCliente;
        this.cliente = cliente;
    }

    public int getCountVerificadorCliente() {
        return countVerificadorCliente;
    }

    public void setCountVerificadorCliente(int countVerificadorCliente) {
        this.countVerificadorCliente = countVerificadorCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public String toString() {
        return "ClienteVerificador{" + "countVerificadorCliente=" + countVerificadorCliente + ", cliente=" + cliente + '}';
    }    
}
