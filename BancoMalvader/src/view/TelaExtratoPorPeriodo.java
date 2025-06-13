package view;

import javax.swing.*;
import dao.ContaDAO;
import model.Usuario;

public class TelaExtratoPorPeriodo extends JFrame {

    private JTextField txtInicio, txtFim;
    private JTextArea txtResultado;

    public TelaExtratoPorPeriodo(Usuario usuario) {
        setTitle("Extrato por Período");
        setSize(450, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblInicio = new JLabel("Data Início (YYYY-MM-DD):");
        lblInicio.setBounds(20, 20, 200, 20);
        add(lblInicio);

        txtInicio = new JTextField();
        txtInicio.setBounds(220, 20, 150, 20);
        add(txtInicio);

        JLabel lblFim = new JLabel("Data Fim (YYYY-MM-DD):");
        lblFim.setBounds(20, 50, 200, 20);
        add(lblFim);

        txtFim = new JTextField();
        txtFim.setBounds(220, 50, 150, 20);
        add(txtFim);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(150, 80, 100, 25);
        add(btnBuscar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBounds(20, 120, 390, 270);
        add(scroll);

        btnBuscar.addActionListener(e -> {
            String inicio = txtInicio.getText();
            String fim = txtFim.getText();

            ContaDAO dao = new ContaDAO();
            String extrato = dao.gerarExtratoPorPeriodo(usuario.getCpf(), inicio, fim);
            txtResultado.setText(extrato);
        });
    }
}
