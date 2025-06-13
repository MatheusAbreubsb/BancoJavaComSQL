package view;

import javax.swing.*;
import dao.ContaDAO;
import model.Usuario;

public class TelaSaldo extends JFrame {

    public TelaSaldo(Usuario usuario) {
        setTitle("Consulta de Saldo");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JTextArea txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBounds(20, 20, 340, 220);
        add(scroll);

        ContaDAO dao = new ContaDAO();
        String dados = dao.consultarSaldo(usuario.getCpf());
        txtResultado.setText(dados);
    }
}
