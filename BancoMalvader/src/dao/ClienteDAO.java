package dao;

import util.Conexao;

import java.sql.*;

public class ClienteDAO {

    public String consultarDadosCliente(String cpf) {
        StringBuilder sb = new StringBuilder();

        String sql = """
            SELECT u.nome, u.cpf, u.data_nascimento, u.telefone,
                   c.id_cliente, c.score_credito,
                   e.cep, e.local, e.numero_casa, e.bairro, e.cidade, e.estado
            FROM usuario u
            JOIN cliente c ON u.id_usuario = c.id_usuario
            LEFT JOIN endereco e ON u.id_usuario = e.id_usuario
            WHERE u.cpf = ?
        """;

        try (Connection conn = Conexao.conectar()) {

            // Buscar e exibir dados pessoais + score
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return "Cliente não encontrado.";
            }

            int idCliente = rs.getInt("id_cliente");

            // Atualiza o score usando a procedure
            CallableStatement call = conn.prepareCall("CALL calcular_score_credito(?)");
            call.setInt(1, idCliente);
            call.execute();

            sb.append("Nome: ").append(rs.getString("nome")).append("\n");
            sb.append("CPF: ").append(rs.getString("cpf")).append("\n");
            sb.append("Nascimento: ").append(rs.getDate("data_nascimento")).append("\n");
            sb.append("Telefone: ").append(rs.getString("telefone")).append("\n");
            sb.append("Score de Crédito: ").append(rs.getDouble("score_credito")).append("\n\n");

            sb.append("Endereço:\n");
            sb.append(rs.getString("local")).append(", Nº ").append(rs.getInt("numero_casa")).append("\n");
            sb.append(rs.getString("bairro")).append(" - ").append(rs.getString("cidade"))
              .append(" / ").append(rs.getString("estado")).append("\nCEP: ").append(rs.getString("cep")).append("\n\n");

            // Buscar contas
            String sqlContas = """
                SELECT numero_conta, tipo_conta, status
                FROM conta
                WHERE id_cliente = ?
            """;

            PreparedStatement stmtContas = conn.prepareStatement(sqlContas);
            stmtContas.setInt(1, idCliente);
            ResultSet contas = stmtContas.executeQuery();

            sb.append("Contas:\n");
            while (contas.next()) {
                sb.append("- ").append(contas.getString("numero_conta"))
                  .append(" [").append(contas.getString("tipo_conta"))
                  .append("] - ").append(contas.getString("status")).append("\n");
            }

        } catch (SQLException e) {
            return "Erro ao consultar dados do cliente: " + e.getMessage();
        }

        return sb.toString();
    }
}
