/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.principal;
import br.com.udesc.ceavi.messengerCloneServidor.model.Servidor;
import java.io.IOException;

/**
 *
 * @author Gabriel Soares
 */
public class Main {

    public static void main(String[] args) {

        int porta = 56000;
        Servidor server;

        try {
            server = new Servidor(porta);
            server.startServidor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
