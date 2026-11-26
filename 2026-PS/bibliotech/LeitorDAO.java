import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeitorDAO {

    public boolean adicionarLeitor(String nome, String matricula, int limiteEmprestimos) {
        if (buscarLeitorPorMatricula(matricula) != null) {
            return false;
        }

        String sql = "INSERT INTO leitores (nome, matricula, limite_emprestimos) VALUES (?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, nome);
            comando.setString(2, matricula);
            comando.setInt(3, limiteEmprestimos);

            int linhas = comando.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível cadastrar o leitor.", e);
        }
    }

    public Leitor buscarLeitorPorMatricula(String matricula) {
        String sql = "SELECT id, nome, matricula, limite_emprestimos FROM leitores WHERE matricula = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, matricula);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return montar(resultado);
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível buscar o leitor por matrícula.", e);
        }

        return null;
    }

    public List<Leitor> listarLeitores() {
        List<Leitor> leitores = new ArrayList<>();
        String sql = "SELECT id, nome, matricula, limite_emprestimos FROM leitores ORDER BY nome";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                leitores.add(montar(resultado));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível listar os leitores.", e);
        }

        return leitores;
    }

    public boolean excluirLeitor(int id) {
        String sql = "DELETE FROM leitores WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);
            int linhas = comando.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível excluir o leitor.", e);
        }
    }

    private Leitor montar(ResultSet resultado) throws SQLException {
        int id = resultado.getInt("id");
        String nome = resultado.getString("nome");
        String matricula = resultado.getString("matricula");
        int limite = resultado.getInt("limite_emprestimos");
        return new Leitor(id, nome, matricula, limite);
    }
}
