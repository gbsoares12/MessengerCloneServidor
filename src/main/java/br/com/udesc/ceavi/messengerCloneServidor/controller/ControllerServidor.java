/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.messengerCloneServidor.controller;

import br.com.udesc.ceavi.messengerCloneServidor.DAO.ClienteDAO;
import br.com.udesc.ceavi.messengerCloneServidor.model.Cliente;
import br.com.udesc.ceavi.messengerCloneServidor.model.ClienteVerificador;
import br.com.udesc.ceavi.messengerCloneServidor.model.ListaClienteVerificador;
import br.com.udesc.ceavi.messengerCloneServidor.model.ListaConectados;
import br.com.udesc.ceavi.messengerCloneServidor.model.VerificadorConexao;
import br.com.udesc.ceavi.messengerCloneServidor.utils.Desconectar;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

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
                atualizarConexao(in);
                break;
            case "RemoverUser":
                removerContato(in);
                break;
            case "EditarUser":
                editarUser(in);
                break;
            case "Sair":
                sair(in);
                break;
        }
    }

    public void manterConexao(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();
        ListaConectados listaCli = ListaConectados.getInstance();
        ListaClienteVerificador listaCliVerificador = ListaClienteVerificador.getInstance();
        try {
            int porta;

            if (listaCli.getListaUsuarios().isEmpty()) {
                porta = 56001;
            } else {
                porta = listaCli.getListaUsuarios().get(listaCli.getListaUsuarios().size() - 1).getPorta() + 1;
            }
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            while (!(line = in.readLine()).equalsIgnoreCase("fimlogin")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                Cliente cliValidacao = ClienteDAO.validarLogin(cli.getEmail(), cli.getSenha());
                out.println(gson.toJson(cliValidacao));
                //Merge com a respectiva porta no servidor e o ip do cliente.
                cliValidacao.setPorta(porta);
                String[] ipFormatado = cli.getIp().split("/");
                cliValidacao.setIp(ipFormatado[1]);
                cliValidacao.setStatus(true);
                ClienteDAO.update(cliValidacao);

                VerificadorConexao verificador = VerificadorConexao.getInstance();
                listaCli.getListaUsuarios().add(cliValidacao);// Lista de cliente conectados

                //logica dos clientes com verificador conectados
                ClienteVerificador cliVerificador = new ClienteVerificador(cliValidacao, verificador.getCountVerificador());
                listaCliVerificador.addClienteVerificador(cliVerificador);

                out.println(gson.toJson(cliValidacao));
                out.println("countVerificadorServidor");
                out.println(verificador.getCountVerificador());
                out.println("fimverificador");
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

    public void adicionarContato(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();

        try {
            List<ClienteVerificador> listaClientesConectados = ListaClienteVerificador.getInstance().getListaClientesComVerificadorConectados();
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            while (!(line = in.readLine()).equalsIgnoreCase("fimadd")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                cli = ClienteDAO.update(cli);
                out.println(gson.toJson(cli));
                for (ClienteVerificador clienteConectado : listaClientesConectados) {
                    if (clienteConectado.getCliente().getEmail().equalsIgnoreCase(cli.getEmail())) {
                        clienteConectado.setCliente(cli);
                    }
                }
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
        PrintWriter out = null;
        Gson gson = new Gson();
        try {
            List<ClienteVerificador> listaClientesConectados = ListaClienteVerificador.getInstance().getListaClientesComVerificadorConectados();
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            while (!(line = in.readLine()).equalsIgnoreCase("fimeditar")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                cli = ClienteDAO.update(cli);
                out.println(gson.toJson(cli));
                for (ClienteVerificador clienteConectado : listaClientesConectados) {
                    if (clienteConectado.getCliente().getEmail().equalsIgnoreCase(cli.getEmail())) {
                        clienteConectado.setCliente(cli);
                    }
                }
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

    public void removerContato(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();
        try {
            List<ClienteVerificador> listaClientesConectados = ListaClienteVerificador.getInstance().getListaClientesComVerificadorConectados();
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            while (!(line = in.readLine()).equalsIgnoreCase("fimremover")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                cli = ClienteDAO.update(cli);
                out.println(gson.toJson(cli));
                for (ClienteVerificador clienteConectado : listaClientesConectados) {
                    if (clienteConectado.getCliente().getEmail().equalsIgnoreCase(cli.getEmail())) {
                        clienteConectado.setCliente(cli);
                    }
                }
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

    public void atualizarConexao(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();
        ListaClienteVerificador listaCliVerificador = ListaClienteVerificador.getInstance();
        int countAtualCliente = 1;
        try {
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            Cliente cliTentadoAtualizar = null;
            while (!(line = in.readLine()).equalsIgnoreCase("fimcountcliente")) {
                if (line.equalsIgnoreCase("objCliente")) {
                    cliTentadoAtualizar = gson.fromJson(in.readLine(), Cliente.class);
                }
                if (line.equalsIgnoreCase("countVerificadorCliente")) {
                    countAtualCliente = Integer.parseInt(in.readLine());
                }
            }
            if (cliTentadoAtualizar != null) {
                for (ClienteVerificador cliVerificador : listaCliVerificador.getListaClientesComVerificadorConectados()) {
                    if (cliTentadoAtualizar.getEmail().equalsIgnoreCase(cliVerificador.getCliente().getEmail())) {
                        cliVerificador.setCountVerificadorCliente(countAtualCliente);
                    }
                }
            }
            out.println(gson.toJson(ClienteDAO.buscaUser(cliTentadoAtualizar.getEmail())));
            out.println("200");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            out.println("500");
            ex.printStackTrace();
        } finally {
            Desconectar.fechar(in, out, conn);
        }
    }

    public void sair(BufferedReader in) {
        PrintWriter out = null;
        Gson gson = new Gson();
        try {
            out = new PrintWriter(conn.getOutputStream(), true);
            String line = "";
            while (!(line = in.readLine()).equalsIgnoreCase("fimsair")) {
                Cliente cli = gson.fromJson(line, Cliente.class);
                desconectarCliente(cli);
            }
            out.println("200");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            out.println("500");
            ex.printStackTrace();
        } finally {
            Desconectar.fechar(in, out, conn);
        }
    }

    public void desconectarCliente(Cliente cliente) {
        ListaConectados listaCli = ListaConectados.getInstance();
        listaCli.getListaUsuarios().remove(cliente);
        cliente.setIp(null);
        cliente.setPorta(0);
        cliente.setStatus(false);
        ClienteDAO.update(cliente);
    }

}
