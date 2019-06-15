/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.model;

import br.com.udesc.ceavi.messengerCloneServidor.DAO.ClienteDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel Soares
 */
public class VerificadorConexao extends Thread {

    private int countVerificador = 0;
    private static VerificadorConexao instance;//Padrão Singletonz

    public synchronized static VerificadorConexao getInstance() {//Padrão Singleton
        if (instance == null) {
            instance = new VerificadorConexao();
        }
        return instance;
    }

    public int getCountVerificador() {
        return countVerificador;
    }

    @Override
    public void run() {
        while (true) {

            ListaConectados listaConectados = ListaConectados.getInstance();
            if (listaConectados.getListaUsuarios().size() > 0) {
                // Caso haja alguém conectado o numero de msg de atualização tem que ser a mesma do countVerificador.
                this.countVerificador++;
                System.out.println("O count do servidor de tempo de verificação está em: " + this.countVerificador);
                verificarConexao();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(VerificadorConexao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void verificarConexao() {
        ListaClienteVerificador listaClienteComCount = ListaClienteVerificador.getInstance();
        boolean desconectou = false;
        ClienteVerificador clienteComCount = null;
        if (!listaClienteComCount.getListaClientesComVerificadorConectados().isEmpty()) {
            for (ClienteVerificador clienteComVerificadorConectado : listaClienteComCount.getListaClientesComVerificadorConectados()) {
                if ((clienteComVerificadorConectado.getCountVerificadorCliente()+1) != this.countVerificador) {
                    desconectou = desconectarCliente(clienteComVerificadorConectado.getCliente());
                    clienteComCount = clienteComVerificadorConectado;
                }
            }
            if (desconectou) {
                listaClienteComCount.getListaClientesComVerificadorConectados().remove(clienteComCount);
            }
        }
    }

    public boolean desconectarCliente(Cliente cliente) {
        try {
            ListaConectados listaCli = ListaConectados.getInstance();
            listaCli.getListaUsuarios().remove(cliente);
            cliente.setIp(null);
            cliente.setPorta(0);
            cliente.setStatus(false);
            ClienteDAO.update(cliente);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
