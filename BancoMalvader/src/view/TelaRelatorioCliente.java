package view;

import javax.swing.*;
import dao.ContaDAO;

public class TelaRelatorioCliente extends JFrame {

    private JTextField txtCpf;
    private JTextArea txtResultado;

    public TelaRelatorioCliente() {
        setTitle("Relatório de Movimentações por Cliente");
        setSize(450, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblCpf = new JLabel("CPF do Cliente:");
        lblCpf.setBounds(20, 20, 120, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(140, 20, 180, 20);
        add(txtCpf);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(330, 20, 80, 20);
        add(btnBuscar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBounds(20, 60, 390, 280);
        add(scroll);

        btnBuscar.addActionListener(e -> {
            String cpf = txtCpf.getText();
            ContaDAO dao = new ContaDAO();
            String relatorio = dao.gerarRelatorioCliente(cpf);
            txtResultado.setText(relatorio);
        });
    }
}
