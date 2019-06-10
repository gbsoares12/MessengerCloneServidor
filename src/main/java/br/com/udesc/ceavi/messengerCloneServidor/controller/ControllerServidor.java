/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.controller;

import br.com.udesc.ceavi.messengerCloneServidor.DAO.ClienteDAO;
import br.com.udesc.ceavi.messengerCloneServidor.model.Cliente;
import br.com.udesc.ceavi.messengerCloneServidor.utils.Desconectar;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel Soares
 */
public class ControllerServidor extends Thread {

    private Socket conn;

    public ControllerServidor(Socket conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        String opcao = null;

        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            opcao = in.readLine();
            System.out.println("Requisição: " + opcao + " | Usuario: " + conn.getInetAddress());
        } catch (IOException e) {
            System.out.println("Erro na criação do socket!");
            e.printStackTrace();
        }

        switch (opcao) {
            case "Registrar":
                registrarCliente(in);
                break;
            case "Entrar":
                manterConexao(in);
                break;
            case "BuscaUser":
                buscarUser(in);
                break;
            case "AddUser":
                adicionarContato(in);
                break;
            case "Atualizar":
                atualizarConexao();
                break;
            case "RemoverUser":
                removerContato(in);
                break;
            case "EditarUser":
                editarUser(in);
                break;
        }
    }

    public void manterConexao(BufferedReader in) {
        PrintWriter out = null;
        String dadosLogin[] = null;
        Gson gson = new Gson();
        try {
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";

            while (!(line = in.readLine()).equalsIgnoreCase("fimlogin")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                Cliente cliValidacao = ClienteDAO.validarLogin(cli.getEmail(), cli.getSenha());
                out.println(gson.toJson(cliValidacao));
            }
            out.println("201");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            out.println("404");
            ex.printStackTrace();
        } finally {
            Desconectar.fechar(in, out, conn);
        }
    }

    public void registrarCliente(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();
        try {
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            while (!(line = in.readLine()).equalsIgnoreCase("fimcliente")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                ClienteDAO.salvar(cli);
            }
            out.println("200");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            out.println("500");
        } finally {
            Desconectar.fecharInConn(in, conn);
        }
    }

    public void buscarUser(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();
        try {
            String line = "";
            out = new PrintWriter(conn.getOutputStream(), true);
            Cliente cli = null;
            while (!(line = in.readLine()).equalsIgnoreCase("fimbusca")) {
                cli = gson.fromJson(line, Cliente.class);
                cli = ClienteDAO.buscaUser(cli.getEmail());
            }
            out.println(gson.toJson(cli));
            out.println("202");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            out.println("404");
            ex.printStackTrace();
        } finally {
            Desconectar.fechar(in, out, conn);
        }
    }

    //DESENVOLVER PARA DAR MERGE NO CLIENTE
    public void adicionarContato(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();
        try {
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            while (!(line = in.readLine()).equalsIgnoreCase("fimadd")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                ClienteDAO.update(cli);
            }
            out.println("200");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            out.println("500");
        } finally {
            Desconectar.fecharInConn(in, conn);
        }
    }

    public void editarUser(BufferedReader in) {

    }

    public void removerContato(BufferedReader in) {

    }

    public void atualizarConexao() {

    }

}
