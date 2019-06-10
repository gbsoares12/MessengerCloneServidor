/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 *
 * @author 42519630833
 */
public class Desconectar {

    private Desconectar() {
    }

    static public void fechar(Reader in, Writer out, Socket conn) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (IOException e) {
            System.out.println("Error on closing input stream, output stream or socket");
            e.printStackTrace();
        }
    }

    static public void fecharInConn(Reader in, Socket conn) {
        try {
            if (in != null) {
                in.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (IOException e) {
            System.out.println("Error on closing input stream or socket");
            e.printStackTrace();
        }
    }
}
