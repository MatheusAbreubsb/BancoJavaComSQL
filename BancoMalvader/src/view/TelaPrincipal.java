package view;

import javax.swing.*;
import model.Usuario;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal(Usuario usuario) {
        setTitle("Banco Malvader - Painel Principal");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblBemVindo = new JLabel("Bem-vindo(a), " + usuario.getNome());
        lblBemVindo.setBounds(20, 20, 300, 25);
        add(lblBemVindo);

        JLabel lblTipo = new JLabel("Tipo de usuário: " + usuario.getTipo());
        lblTipo.setBounds(20, 50, 300, 25);
        add(lblTipo);

        // Aqui você pode adicionar botões com base no tipo
        if (usuario.getTipo().equals("CLIENTE")) {
        	JButton btnSaldo = new JButton("Ver Saldo");
        	btnSaldo.setBounds(20, 90, 150, 25);
        	btnSaldo.addActionListener(e -> new TelaSaldo(usuario).setVisible(true));
        	add(btnSaldo);
            JButton btnExtrato = new JButton("Ver Extrato");
            btnExtrato.setBounds(20, 130, 180, 25);
            btnExtrato.addActionListener(e -> new TelaExtrato(usuario).setVisible(true));
            add(btnExtrato);
            JButton btnTransacao = new JButton("Transações");
            btnTransacao.setBounds(20, 170, 180, 25);
            btnTransacao.addActionListener(e -> new TelaTransacoes(usuario).setVisible(true));
            add(btnTransacao);
            JButton btnPeriodo = new JButton("Extrato por Período");
            btnPeriodo.setBounds(20, 210, 180, 25);
            btnPeriodo.addActionListener(e -> new TelaExtratoPorPeriodo(usuario).setVisible(true));
            add(btnPeriodo);
            JButton btnMeusDados = new JButton("Meus Dados");
            btnMeusDados.setBounds(20, 250, 180, 25);
            btnMeusDados.addActionListener(e -> new TelaConsultaCliente(usuario).setVisible(true));
            add(btnMeusDados);





        } else if (usuario.getTipo().equals("FUNCIONARIO")) {
        	JButton btnCadastro = new JButton("Cadastrar Cliente");
        	btnCadastro.setBounds(20, 90, 180, 25);
        	btnCadastro.addActionListener(e -> new TelaCadastroCliente().setVisible(true));
        	add(btnCadastro);
        	JButton btnAbrirConta = new JButton("Abrir Conta");
        	btnAbrirConta.setBounds(20, 130, 180, 25);
        	btnAbrirConta.addActionListener(e -> new TelaAberturaConta().setVisible(true));
        	add(btnAbrirConta);
            JButton btnRelatorio = new JButton("Relatório por Cliente");
            btnRelatorio.setBounds(220, 90, 180, 25);
            btnRelatorio.addActionListener(e -> new TelaRelatorioCliente().setVisible(true));
            add(btnRelatorio);
            JButton btnConsulta = new JButton("Consultar Cliente");
            btnConsulta.setBounds(220, 130, 180, 25);
            btnConsulta.addActionListener(e -> {
                String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente:");
                if (cpf != null && !cpf.isEmpty()) {
                    Usuario usuarioTemp = new Usuario();
                    usuarioTemp.setCpf(cpf);
                    new TelaConsultaCliente(usuarioTemp).setVisible(true);
                }
            });
            add(btnConsulta);
            JButton btnEncerrar = new JButton("Encerrar Conta");
            btnEncerrar.setBounds(220, 170, 180, 25);
            btnEncerrar.addActionListener(e -> new TelaEncerramentoConta(usuario).setVisible(true));
            add(btnEncerrar);


        	

        }
    }
}
