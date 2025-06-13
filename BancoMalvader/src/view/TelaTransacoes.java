package view;

import javax.swing.*;
import dao.ContaDAO;
import model.Usuario;

public class TelaTransacoes extends JFrame {

    private JTextField txtValor, txtDestino, txtDescricao;
    private JComboBox<String> cbTipo;
    private JButton btnConfirmar;

    public TelaTransacoes(Usuario usuario) {
        setTitle("Transações Bancárias");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 20, 100, 20);
        add(lblTipo);

        cbTipo = new JComboBox<>(new String[] {"DEPOSITO", "SAQUE", "TRANSFERENCIA"});
        cbTipo.setBounds(120, 20, 200, 20);
        add(cbTipo);

        JLabel lblValor = new JLabel("Valor:");
        lblValor.setBounds(20, 60, 100, 20);
        add(lblValor);

        txtValor = new JTextField();
        txtValor.setBounds(120, 60, 200, 20);
        add(txtValor);

        JLabel lblDestino = new JLabel("Conta destino:");
        lblDestino.setBounds(20, 100, 100, 20);
        add(lblDestino);

        txtDestino = new JTextField();
        txtDestino.setBounds(120, 100, 200, 20);
        add(txtDestino);

        JLabel lblDesc = new JLabel("Descrição:");
        lblDesc.setBounds(20, 140, 100, 20);
        add(lblDesc);

        txtDescricao = new JTextField();
        txtDescricao.setBounds(120, 140, 200, 20);
        add(txtDescricao);

        btnConfirmar = new JButton("Executar");
        btnConfirmar.setBounds(120, 180, 120, 30);
        add(btnConfirmar);

        // Evento do botão
        btnConfirmar.addActionListener(e -> {
            String tipo = (String) cbTipo.getSelectedItem();
            String valor = txtValor.getText();
            String destino = txtDestino.getText();
            String descricao = txtDescricao.getText();

            ContaDAO dao = new ContaDAO();
            boolean ok = dao.realizarTransacao(usuario.getCpf(), tipo, valor, destino, descricao);

            if (ok) {
                JOptionPane.showMessageDialog(null, "Transação realizada com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao realizar transação.");
            }
        });

        // Esconder campo "destino" se não for transferência
        cbTipo.addActionListener(e -> {
            boolean transf = cbTipo.getSelectedItem().equals("TRANSFERENCIA");
            txtDestino.setEnabled(transf);
        });

        txtDestino.setEnabled(false);
    }
}
