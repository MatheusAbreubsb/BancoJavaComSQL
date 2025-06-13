package view;

import javax.swing.*;
import dao.ContaDAO;
import java.awt.event.*;

public class TelaAberturaConta extends JFrame {

    private JTextField txtCpf, txtRendimento, txtLimite, txtVencimento, txtTaxa, txtValorMinimo;
    private JComboBox<String> cbTipoConta;
    private JButton btnAbrir;

    public TelaAberturaConta() {
        setTitle("Abertura de Conta");
        setSize(400, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblCpf = new JLabel("CPF do Cliente:");
        lblCpf.setBounds(20, 20, 120, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(150, 20, 200, 20);
        add(txtCpf);

        JLabel lblTipo = new JLabel("Tipo de Conta:");
        lblTipo.setBounds(20, 60, 120, 20);
        add(lblTipo);

        cbTipoConta = new JComboBox<>(new String[] { "POUPANCA", "CORRENTE", "INVESTIMENTO" });
        cbTipoConta.setBounds(150, 60, 200, 20);
        add(cbTipoConta);

        // Campos específicos
        txtRendimento = new JTextField(); // Poupança e Investimento
        txtLimite = new JTextField();     // Corrente
        txtVencimento = new JTextField(); // Corrente
        txtTaxa = new JTextField();       // Corrente
        txtValorMinimo = new JTextField();// Investimento

        JLabel lbl1 = new JLabel("Taxa Rendimento:");
        lbl1.setBounds(20, 100, 120, 20);
        add(lbl1);
        txtRendimento.setBounds(150, 100, 200, 20);
        add(txtRendimento);

        JLabel lbl2 = new JLabel("Limite (Corrente):");
        lbl2.setBounds(20, 130, 120, 20);
        add(lbl2);
        txtLimite.setBounds(150, 130, 200, 20);
        add(txtLimite);

        JLabel lbl3 = new JLabel("Vencimento:");
        lbl3.setBounds(20, 160, 120, 20);
        add(lbl3);
        txtVencimento.setBounds(150, 160, 200, 20);
        add(txtVencimento);

        JLabel lbl4 = new JLabel("Taxa Manutenção:");
        lbl4.setBounds(20, 190, 120, 20);
        add(lbl4);
        txtTaxa.setBounds(150, 190, 200, 20);
        add(txtTaxa);

        JLabel lbl5 = new JLabel("Valor Mínimo (Invest):");
        lbl5.setBounds(20, 220, 150, 20);
        add(lbl5);
        txtValorMinimo.setBounds(150, 220, 200, 20);
        add(txtValorMinimo);

        btnAbrir = new JButton("Abrir Conta");
        btnAbrir.setBounds(120, 260, 150, 30);
        add(btnAbrir);

        btnAbrir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContaDAO dao = new ContaDAO();
                boolean ok = dao.abrirConta(
                    txtCpf.getText(),
                    (String) cbTipoConta.getSelectedItem(),
                    txtRendimento.getText(),
                    txtLimite.getText(),
                    txtVencimento.getText(),
                    txtTaxa.getText(),
                    txtValorMinimo.getText()
                );

                if (ok) {
                    JOptionPane.showMessageDialog(null, "Conta criada com sucesso!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao criar conta.");
                }
            }
        });
    }
}
