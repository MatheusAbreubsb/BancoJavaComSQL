package view;

import javax.swing.*;
import dao.UsuarioDAO;
import java.awt.event.*;
import java.security.MessageDigest;

public class TelaCadastroCliente extends JFrame {

    private JTextField txtNome, txtCpf, txtDataNasc, txtTelefone;
    private JPasswordField txtSenha;

    public TelaCadastroCliente() {
        setTitle("Cadastro de Cliente");
        setSize(350, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 20, 80, 20);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(100, 20, 200, 20);
        add(txtNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(20, 50, 80, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(100, 50, 200, 20);
        add(txtCpf);

        JLabel lblData = new JLabel("Nascimento:");
        lblData.setBounds(20, 80, 100, 20);
        add(lblData);

        txtDataNasc = new JTextField("yyyy-mm-dd");
        txtDataNasc.setBounds(100, 80, 200, 20);
        add(txtDataNasc);

        JLabel lblTel = new JLabel("Telefone:");
        lblTel.setBounds(20, 110, 80, 20);
        add(lblTel);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(100, 110, 200, 20);
        add(txtTelefone);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(20, 140, 80, 20);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(100, 140, 200, 20);
        add(txtSenha);

        JButton btnSalvar = new JButton("Cadastrar");
        btnSalvar.setBounds(100, 190, 120, 30);
        add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });
    }

    private void cadastrarCliente() {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String dataNasc = txtDataNasc.getText();
        String telefone = txtTelefone.getText();
        String senha = new String(txtSenha.getPassword());
        String senhaHash = gerarHashMD5(senha);

        UsuarioDAO dao = new UsuarioDAO();
        boolean sucesso = dao.inserirCliente(nome, cpf, dataNasc, telefone, senhaHash);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            dispose(); // fecha a tela
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente.");
        }
    }

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
}
