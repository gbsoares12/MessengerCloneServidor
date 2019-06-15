package br.com.udesc.ceavi.messengerCloneServidor.model;

import br.com.udesc.ceavi.messengerCloneServidor.model.Cliente;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-14T21:04:25")
@StaticMetamodel(Cliente.class)
public class Cliente_ { 

    public static volatile SingularAttribute<Cliente, String> senha;
    public static volatile ListAttribute<Cliente, Cliente> listaContatos;
    public static volatile SingularAttribute<Cliente, String> ip;
    public static volatile SingularAttribute<Cliente, String> nome;
    public static volatile SingularAttribute<Cliente, Long> id;
    public static volatile SingularAttribute<Cliente, String> email;
    public static volatile SingularAttribute<Cliente, Integer> porta;
    public static volatile SingularAttribute<Cliente, Boolean> status;

}