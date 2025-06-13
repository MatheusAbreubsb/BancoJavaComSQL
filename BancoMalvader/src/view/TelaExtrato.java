package view;

import javax.swing.*;
import dao.ContaDAO;
import model.Usuario;

public class TelaExtrato extends JFrame {

    private JTextArea txtExtrato;

    public TelaExtrato(Usuario usuario) {
        setTitle("Extrato e Saldo");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        txtExtrato = new JTextArea();
        txtExtrato.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtExtrato);
        scroll.setBounds(20, 20, 340, 300);
        add(scroll);

        ContaDAO dao = new ContaDAO();
        String conteudo = dao.gerarExtrato(usuario.getCpf());
        txtExtrato.setText(conteudo);
    }
}
