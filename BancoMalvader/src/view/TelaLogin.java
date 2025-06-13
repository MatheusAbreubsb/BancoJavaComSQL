package view;

import javax.swing.*;
import java.awt.event.*;
import dao.UsuarioDAO;
import model.Usuario;
import java.security.MessageDigest;

public class TelaLogin extends JFrame {

    private JTextField txtCpf;
    private JPasswordField txtSenha;

    public TelaLogin() {
        setTitle("Login - Banco Malvader");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 30, 80, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(100, 30, 150, 20);
        add(txtCpf);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 60, 80, 20);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(100, 60, 150, 20);
        add(txtSenha);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(100, 100, 100, 25);
        add(btnEntrar);

        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }

    private void realizarLogin() {
        String cpf = txtCpf.getText();
        String senha = new String(txtSenha.getPassword());
        String senhaHash = gerarHashMD5(senha);

        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.buscarPorCpfESenha(cpf, senhaHash);

        if (u != null) {
        	new TelaPrincipal(u).setVisible(true);
        	dispose(); // fecha a tela de login
        } else {
            JOptionPane.showMessageDialog(this, "CPF ou senha incorretos.");
        }
    }

    // Simulando o uso de senha hash MD5
    private String gerarHashMD5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
