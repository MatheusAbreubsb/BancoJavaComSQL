package view;

import javax.swing.*;
import dao.ContaDAO;
import model.Usuario;

public class TelaEncerramentoConta extends JFrame {

    private JTextField txtNumeroConta, txtOtp, txtMotivo;
    private JPasswordField txtSenha;
    private JButton btnEncerrar;

    public TelaEncerramentoConta(Usuario funcionario) {
        setTitle("Encerramento de Conta");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblConta = new JLabel("Número da Conta:");
        lblConta.setBounds(20, 20, 120, 20);
        add(lblConta);

        txtNumeroConta = new JTextField();
        txtNumeroConta.setBounds(150, 20, 200, 20);
        add(txtNumeroConta);

        JLabel lblSenha = new JLabel("Senha do funcionário:");
        lblSenha.setBounds(20, 60, 150, 20);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(180, 60, 170, 20);
        add(txtSenha);

        JLabel lblOtp = new JLabel("Código OTP:");
        lblOtp.setBounds(20, 100, 120, 20);
        add(lblOtp);

        txtOtp = new JTextField();
        txtOtp.setBounds(150, 100, 200, 20);
        add(txtOtp);

        JLabel lblMotivo = new JLabel("Motivo:");
        lblMotivo.setBounds(20, 140, 120, 20);
        add(lblMotivo);

        txtMotivo = new JTextField();
        txtMotivo.setBounds(150, 140, 200, 20);
        add(txtMotivo);

        btnEncerrar = new JButton("Encerrar Conta");
        btnEncerrar.setBounds(120, 190, 150, 30);
        add(btnEncerrar);

        // Gera OTP ao abrir a tela
        ContaDAO dao = new ContaDAO();
        String otpGerado = dao.gerarOtp(funcionario.getCpf());
        JOptionPane.showMessageDialog(this, "OTP gerado: " + otpGerado);

        btnEncerrar.addActionListener(e -> {
            String numero = txtNumeroConta.getText();
            String senha = new String(txtSenha.getPassword());
            String otp = txtOtp.getText();
            String motivo = txtMotivo.getText();

            boolean sucesso = dao.encerrarConta(numero, senha, otp, motivo, funcionario.getCpf());

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Conta encerrada com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao encerrar conta. Verifique os dados.");
            }
        });
    }
}
