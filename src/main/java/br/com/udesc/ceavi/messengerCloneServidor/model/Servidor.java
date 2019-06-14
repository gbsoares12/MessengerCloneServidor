/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.model;

import br.com.udesc.ceavi.messengerCloneServidor.controller.ControllerServidor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel Soares
 */
public class Servidor {

    private ServerSocket server;

    public Servidor(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.server.setReuseAddress(true);

    }

    public void startServidor() throws IOException, InterruptedException {
        VerificadorConexao verificaConectados = VerificadorConexao.getInstance();
        verificaConectados.start();

        while (true) {
            System.out.println("~~~Usuário conectado esperando requisição do usuário~~~ ");
            Socket conn = server.accept();
            if (conn != null) {
                ControllerServidor cs = new ControllerServidor(conn);
                cs.start();
            }
        }
    }
}
