package view;

import javax.swing.*;
import dao.ClienteDAO;
import model.Usuario;

public class TelaConsultaCliente extends JFrame {

    private JTextArea txtResultado;

    public TelaConsultaCliente(Usuario usuario) {
        setTitle("Consulta de Dados do Cliente");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBounds(20, 20, 440, 300);
        add(scroll);

        ClienteDAO dao = new ClienteDAO();
        String dados = dao.consultarDadosCliente(usuario.getCpf());
        txtResultado.setText(dados);
    }
    
    
    
}
