package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import util.Conexao;

public class ContaDAO {

    private final int idAgenciaPadrao = 1; // usar a primeira agência cadastrada

    public boolean abrirConta(String cpfCliente, String tipoConta, String rendimento, String limite, String vencimento, String taxa, String valorMinimo) {
        try (Connection conn = Conexao.conectar()) {

            // 1. Buscar o ID do cliente
            String sqlBuscarCliente = "SELECT id_cliente FROM cliente c JOIN usuario u ON c.id_usuario = u.id_usuario WHERE u.cpf = ?";
            PreparedStatement stmtCliente = conn.prepareStatement(sqlBuscarCliente);
            stmtCliente.setString(1, cpfCliente);
            ResultSet rsCliente = stmtCliente.executeQuery();

            if (!rsCliente.next()) {
                System.out.println("Cliente não encontrado.");
                return false;
            }

            int idCliente = rsCliente.getInt("id_cliente");

            // 2. Inserir na tabela conta
            String numeroConta = gerarNumeroConta(); // número fictício simples
            String sqlConta = "INSERT INTO conta (numero_conta, id_agencia, saldo, tipo_conta, id_cliente) VALUES (?, ?, 0, ?, ?)";
            PreparedStatement stmtConta = conn.prepareStatement(sqlConta, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtConta.setString(1, numeroConta);
            stmtConta.setInt(2, idAgenciaPadrao);
            stmtConta.setString(3, tipoConta);
            stmtConta.setInt(4, idCliente);
            stmtConta.executeUpdate();

            ResultSet rsConta = stmtConta.getGeneratedKeys();
            rsConta.next();
            int idConta = rsConta.getInt(1);

            // 3. Inserir na subtabela correspondente
            if (tipoConta.equals("POUPANCA")) {
                String sql = "INSERT INTO conta_poupanca (id_conta, taxa_rendimento) VALUES (?, ?)";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1, idConta);
                st.setDouble(2, Double.parseDouble(rendimento));
                st.executeUpdate();
            } else if (tipoConta.equals("CORRENTE")) {
                String sql = "INSERT INTO conta_corrente (id_conta, limite, data_vencimento, taxa_manutencao) VALUES (?, ?, ?, ?)";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1, idConta);
                st.setDouble(2, Double.parseDouble(limite));
                st.setString(3, vencimento);
                st.setDouble(4, Double.parseDouble(taxa));
                st.executeUpdate();
            } else if (tipoConta.equals("INVESTIMENTO")) {
                String sql = "INSERT INTO conta_investimento (id_conta, perfil_risco, valor_minimo, taxa_rendimento_base) VALUES (?, 'MEDIO', ?, ?)";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1, idConta);
                st.setDouble(2, Double.parseDouble(valorMinimo));
                st.setDouble(3, Double.parseDouble(rendimento));
                st.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao abrir conta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            return false;
        }
    }

    private String gerarNumeroConta() {
        // Número de conta simples gerado por tempo atual (poderia ser mais sofisticado)
        return "ACC" + System.currentTimeMillis() % 1000000;
    }
    
    public String gerarExtrato(String cpf) {
        StringBuilder sb = new StringBuilder();
        String sql = """
            SELECT c.id_conta, c.numero_conta, c.saldo
            FROM conta c
            JOIN cliente cl ON c.id_cliente = cl.id_cliente
            JOIN usuario u ON cl.id_usuario = u.id_usuario
            WHERE u.cpf = ?
            LIMIT 1
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return "Nenhuma conta encontrada para este CPF.";
            }

            int idConta = rs.getInt("id_conta");
            String numeroConta = rs.getString("numero_conta");
            double saldo = rs.getDouble("saldo");

            sb.append("Conta: ").append(numeroConta).append("\n");
            sb.append("Saldo atual: R$ ").append(saldo).append("\n\n");
            sb.append("Extrato:\n");

            // Agora buscar as transações dessa conta (como origem ou destino)
            String sqlTransacoes = """
                SELECT tipo_transacao, valor, data_hora, descricao,
                       id_conta_origem, id_conta_destino
                FROM transacao
                WHERE id_conta_origem = ? OR id_conta_destino = ?
                ORDER BY data_hora DESC
            """;

            PreparedStatement stmtTrans = conn.prepareStatement(sqlTransacoes);
            stmtTrans.setInt(1, idConta);
            stmtTrans.setInt(2, idConta);

            ResultSet transacoes = stmtTrans.executeQuery();

            while (transacoes.next()) {
                String tipo = transacoes.getString("tipo_transacao");
                double valor = transacoes.getDouble("valor");
                String descricao = transacoes.getString("descricao");
                String data = transacoes.getString("data_hora");

                int origem = transacoes.getInt("id_conta_origem");
                int destino = transacoes.getInt("id_conta_destino");

                boolean recebeu = destino == idConta;
                boolean enviou = origem == idConta;

                String movimento;
                if (tipo.equals("TRANSFERENCIA")) {
                    movimento = recebeu ? "Recebido de outra conta" : "Enviado para outra conta";
                } else if (tipo.equals("DEPOSITO")) {
                    movimento = "Depósito";
                } else if (tipo.equals("SAQUE")) {
                    movimento = "Saque";
                } else {
                    movimento = tipo;
                }

                String sinal = recebeu ? "+" : "-";
                sb.append(data)
                  .append(" - ")
                  .append(movimento)
                  .append(" - ")
                  .append(sinal).append("R$ ").append(valor)
                  .append("\n")
                  .append("  ").append(descricao != null ? descricao : "")
                  .append("\n");
            }

            return sb.toString();

        } catch (SQLException e) {
            return "Erro ao gerar extrato: " + e.getMessage();
        }
    }
    public boolean realizarTransacao(String cpf, String tipo, String valorStr, String contaDestino, String descricao) {
        try (Connection conn = Conexao.conectar()) {
            double valor = Double.parseDouble(valorStr);

            // Buscar id_conta do cliente logado
            String sqlConta = """
                SELECT c.id_conta FROM conta c
                JOIN cliente cl ON c.id_cliente = cl.id_cliente
                JOIN usuario u ON cl.id_usuario = u.id_usuario
                WHERE u.cpf = ?
                LIMIT 1
            """;

            PreparedStatement stmtConta = conn.prepareStatement(sqlConta);
            stmtConta.setString(1, cpf);
            ResultSet rs = stmtConta.executeQuery();

            if (!rs.next()) return false;

            int idOrigem = rs.getInt("id_conta");
            int idDestino = 0;

            if (tipo.equals("TRANSFERENCIA")) {
                // Buscar id da conta destino
                String sqlDest = "SELECT id_conta FROM conta WHERE numero_conta = ?";
                PreparedStatement stmtDest = conn.prepareStatement(sqlDest);
                stmtDest.setString(1, contaDestino);
                ResultSet rsDest = stmtDest.executeQuery();
                if (!rsDest.next()) return false;
                idDestino = rsDest.getInt("id_conta");
            }

            // Agora apenas insere a transação.
            String sqlTrans = "INSERT INTO transacao (id_conta_origem, id_conta_destino, tipo_transacao, valor, descricao) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmtTrans = conn.prepareStatement(sqlTrans);
            stmtTrans.setInt(1, idOrigem);
            if (tipo.equals("TRANSFERENCIA")) {
                stmtTrans.setInt(2, idDestino);
            } else {
                stmtTrans.setNull(2, java.sql.Types.INTEGER);
            }
            stmtTrans.setString(3, tipo);
            stmtTrans.setDouble(4, valor);
            stmtTrans.setString(5, descricao);
            stmtTrans.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao realizar transação: " + e.getMessage());
            return false;
        }
    }
    public String gerarExtratoPorPeriodo(String cpf, String dataInicio, String dataFim) {
        StringBuilder sb = new StringBuilder();

        try (Connection conn = Conexao.conectar()) {
            // Buscar id da conta
            String sqlConta = """
                SELECT c.id_conta, c.numero_conta, c.saldo
                FROM conta c
                JOIN cliente cl ON c.id_cliente = cl.id_cliente
                JOIN usuario u ON cl.id_usuario = u.id_usuario
                WHERE u.cpf = ?
                LIMIT 1
            """;

            PreparedStatement stmtConta = conn.prepareStatement(sqlConta);
            stmtConta.setString(1, cpf);
            ResultSet rs = stmtConta.executeQuery();

            if (!rs.next()) {
                return "Conta não encontrada.";
            }

            int idConta = rs.getInt("id_conta");
            String numero = rs.getString("numero_conta");

            sb.append("Conta: ").append(numero).append("\n");
            sb.append("Período: ").append(dataInicio).append(" até ").append(dataFim).append("\n\n");

            // Buscar transações nesse período
            String sql = """
                SELECT tipo_transacao, valor, data_hora, descricao,
                       id_conta_origem, id_conta_destino
                FROM transacao
                WHERE (id_conta_origem = ? OR id_conta_destino = ?)
                  AND data_hora BETWEEN ? AND ?
                ORDER BY data_hora DESC
            """;

            PreparedStatement stmtTrans = conn.prepareStatement(sql);
            stmtTrans.setInt(1, idConta);
            stmtTrans.setInt(2, idConta);
            stmtTrans.setString(3, dataInicio + " 00:00:00");
            stmtTrans.setString(4, dataFim + " 23:59:59");

            ResultSet trans = stmtTrans.executeQuery();

            boolean encontrou = false;
            while (trans.next()) {
                encontrou = true;
                String tipo = trans.getString("tipo_transacao");
                String data = trans.getString("data_hora");
                double valor = trans.getDouble("valor");
                String desc = trans.getString("descricao");

                int origem = trans.getInt("id_conta_origem");
                int destino = trans.getInt("id_conta_destino");

                boolean recebeu = destino == idConta;

                String sinal = recebeu ? "+" : "-";
                sb.append(data).append(" - ").append(tipo).append(" - ")
                  .append(sinal).append("R$ ").append(valor).append("\n")
                  .append("  ").append(desc).append("\n");
            }

            if (!encontrou) {
                sb.append("Nenhuma transação no período.");
            }

        } catch (SQLException e) {
            return "Erro ao buscar extrato: " + e.getMessage();
        }

        return sb.toString();
    }
    
    public String gerarRelatorioCliente(String cpf) {
        StringBuilder sb = new StringBuilder();

        String sqlConta = """
            SELECT u.nome, c.id_conta, c.numero_conta
            FROM usuario u
            JOIN cliente cl ON u.id_usuario = cl.id_usuario
            JOIN conta c ON cl.id_cliente = c.id_cliente
            WHERE u.cpf = ?
            LIMIT 1
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmtConta = conn.prepareStatement(sqlConta)) {

            stmtConta.setString(1, cpf);
            ResultSet rsConta = stmtConta.executeQuery();

            if (!rsConta.next()) {
                return "Cliente não encontrado.";
            }

            String nomeCliente = rsConta.getString("nome");
            int idContaCliente = rsConta.getInt("id_conta");
            String numeroContaCliente = rsConta.getString("numero_conta");

            sb.append("Cliente: ").append(nomeCliente).append("\n");
            sb.append("Conta: ").append(numeroContaCliente).append("\n\n");
            sb.append("Transações:\n");

            String sqlTrans = """
                SELECT t.tipo_transacao, t.valor, t.data_hora, t.descricao,
                       t.id_conta_origem, t.id_conta_destino,
                       co.numero_conta AS conta_origem_num, uo.nome AS nome_origem,
                       cd.numero_conta AS conta_destino_num, ud.nome AS nome_destino
                FROM transacao t
                LEFT JOIN conta co ON t.id_conta_origem = co.id_conta
                LEFT JOIN cliente clo ON co.id_cliente = clo.id_cliente
                LEFT JOIN usuario uo ON clo.id_usuario = uo.id_usuario
                LEFT JOIN conta cd ON t.id_conta_destino = cd.id_conta
                LEFT JOIN cliente cld ON cd.id_cliente = cld.id_cliente
                LEFT JOIN usuario ud ON cld.id_usuario = ud.id_usuario
                WHERE t.id_conta_origem = ? OR t.id_conta_destino = ?
                ORDER BY t.data_hora DESC
            """;

            PreparedStatement stmtTrans = conn.prepareStatement(sqlTrans);
            stmtTrans.setInt(1, idContaCliente);
            stmtTrans.setInt(2, idContaCliente);
            ResultSet rsTrans = stmtTrans.executeQuery();

            boolean encontrou = false;

            while (rsTrans.next()) {
                encontrou = true;
                String tipo = rsTrans.getString("tipo_transacao");
                double valor = rsTrans.getDouble("valor");
                String data = rsTrans.getString("data_hora");
                String desc = rsTrans.getString("descricao");

                int origem = rsTrans.getInt("id_conta_origem");
                int destino = rsTrans.getInt("id_conta_destino");

                String linha = data + " - " + tipo + " - R$ " + valor + "\n";

                if (tipo.equals("TRANSFERENCIA")) {
                    if (origem == idContaCliente) {
                        String nomeDest = rsTrans.getString("nome_destino");
                        String contaDest = rsTrans.getString("conta_destino_num");
                        linha = data + " - TRANSFERENCIA Enviada para \n" + nomeDest + " (Conta: " + contaDest + ") - R$ " + valor + "\n";
                    } else {
                        String nomeOrig = rsTrans.getString("nome_origem");
                        String contaOrig = rsTrans.getString("conta_origem_num");
                        linha = data + " - TRANSFERENCIA Recebida de " + nomeOrig + " (Conta: " + contaOrig + ") - R$ " + valor + "\n";
                    }
                }

                sb.append(linha);
                sb.append("  ").append(desc != null ? desc : "").append("\n");
            }

            if (!encontrou) {
                sb.append("Nenhuma transação encontrada.");
            }

        } catch (SQLException e) {
            return "Erro ao gerar relatório: " + e.getMessage();
        }

        return sb.toString();
    }

    public String consultarSaldo(String cpf) {
        StringBuilder sb = new StringBuilder();

        String sql = """
            SELECT c.numero_conta, c.saldo, c.tipo_conta, c.status
            FROM conta c
            JOIN cliente cl ON c.id_cliente = cl.id_cliente
            JOIN usuario u ON cl.id_usuario = u.id_usuario
            WHERE u.cpf = ?
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                sb.append("Conta: ").append(rs.getString("numero_conta")).append("\n");
                sb.append("Tipo: ").append(rs.getString("tipo_conta")).append("\n");
                sb.append("Status: ").append(rs.getString("status")).append("\n");
                sb.append("Saldo: R$ ").append(rs.getDouble("saldo")).append("\n\n");
            }

            if (!encontrou) {
                return "Nenhuma conta encontrada para este CPF.";
            }

        } catch (SQLException e) {
            return "Erro ao consultar saldo: " + e.getMessage();
        }

        return sb.toString();
    }

    public String gerarOtp(String cpf) {
        String sql = """
            SELECT u.id_usuario
            FROM usuario u
            WHERE u.cpf = ?
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");

                CallableStatement call = conn.prepareCall("CALL gerar_otp(?)");
                call.setInt(1, idUsuario);
                ResultSet rsOtp = call.executeQuery();

                if (rsOtp.next()) {
                    return rsOtp.getString("otp_gerado");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar OTP: " + e.getMessage());
        }

        return null;
    }

    public boolean encerrarConta(String numeroConta, String senha, String otpDigitado, String motivo, String cpfFuncionario) {
        try (Connection conn = Conexao.conectar()) {

            // Validar senha e OTP do funcionário
            String sql = """
                SELECT id_usuario, senha_hash, otp_ativo, otp_expiracao
                FROM usuario
                WHERE cpf = ? AND tipo_usuario = 'FUNCIONARIO'
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpfFuncionario);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return false;

            String hash = rs.getString("senha_hash");
            String otp = rs.getString("otp_ativo");
            Timestamp expiracao = rs.getTimestamp("otp_expiracao");

            String senhaHash = util.HashUtil.md5(senha); // função hash padrão

            if (!senhaHash.equals(hash)) return false;
            if (!otp.equals(otpDigitado)) return false;
            if (expiracao.before(new Timestamp(System.currentTimeMillis()))) return false;

            // Obter ID da conta
            PreparedStatement stConta = conn.prepareStatement("SELECT id_conta FROM conta WHERE numero_conta = ?");
            stConta.setString(1, numeroConta);
            ResultSet rsConta = stConta.executeQuery();
            if (!rsConta.next()) return false;

            int idConta = rsConta.getInt("id_conta");

            // Chamar procedure de encerramento
            CallableStatement call = conn.prepareCall("CALL encerrar_conta(?, ?)");
            call.setInt(1, idConta);
            call.setString(2, motivo);
            call.execute();

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao encerrar conta: " + e.getMessage());
            return false;
        }
    }

    



    
}
