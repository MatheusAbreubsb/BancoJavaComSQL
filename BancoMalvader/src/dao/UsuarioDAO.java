package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Usuario;
import util.Conexao;

public class UsuarioDAO {

    // Método para buscar um usuário pelo CPF e senha (login)
    public Usuario buscarPorCpfESenha(String cpf, String senhaHash) {
        Usuario usuario = null;

        String sql = "SELECT * FROM usuario WHERE cpf = ? AND senha_hash = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, senhaHash);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setDataNascimento(rs.getString("data_nascimento"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setTipo(rs.getString("tipo_usuario"));
                usuario.setSenhaHash(rs.getString("senha_hash"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return usuario;
    }
    
    public boolean inserirCliente(String nome, String cpf, String dataNasc, String telefone, String senhaHash) {
        String sqlUsuario = "INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash) VALUES (?, ?, ?, ?, 'CLIENTE', ?)";
        String sqlCliente = "INSERT INTO cliente (id_usuario) VALUES (LAST_INSERT_ID())";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
             PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {

            stmtUsuario.setString(1, nome);
            stmtUsuario.setString(2, cpf);
            stmtUsuario.setString(3, dataNasc);
            stmtUsuario.setString(4, telefone);
            stmtUsuario.setString(5, senhaHash);

            stmtUsuario.executeUpdate();
            stmtCliente.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
            return false;
        }
    }

}
