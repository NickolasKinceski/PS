/*
 * Disciplina: 2026-PS
 * Projeto   : bibliotech
 * Arquivo   : TelaLeitores.java
 * Autor     : Nickolas Kinceski Martins
 * Descricao : a janela de leitores: cadastra, lista e exclui (Aula 48).
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaLeitores extends JFrame {

    private final LeitorDAO dao = new LeitorDAO();

    private final DefaultTableModel modelo;
    private final JTable tabela;

    private final JTextField campoNome = new JTextField(18);
    private final JTextField campoMatricula = new JTextField(10);
    private final JTextField campoLimite = new JTextField("3", 3);

    private final JLabel status = new JLabel("Pronto.");

    public TelaLeitores() {

        setTitle("BiblioTech - Leitores");
        setSize(860, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        modelo = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Matrícula", "Limite"}, 0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoCadastrar = new JButton("Cadastrar leitor");
        JButton botaoExcluir = new JButton("Excluir selecionado");

        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("Matrícula:"));
        formulario.add(campoMatricula);
        formulario.add(new JLabel("Limite:"));
        formulario.add(campoLimite);
        formulario.add(botaoCadastrar);
        formulario.add(botaoExcluir);

        add(formulario, BorderLayout.NORTH);
        add(status, BorderLayout.SOUTH);

        botaoCadastrar.addActionListener(e -> cadastrar());
        botaoExcluir.addActionListener(e -> excluirSelecionado());

        carregarLeitores();
    }

    private void carregarLeitores() {

        try {

            List<Leitor> leitores = dao.listarLeitores();

            modelo.setRowCount(0);

            for (Leitor leitor : leitores) {
                modelo.addRow(new Object[]{
                    leitor.getId(),
                    leitor.getNome(),
                    leitor.getMatricula(),
                    leitor.getLimiteEmprestimos()
                });
            }

            status.setText("Leitores cadastrados: " + leitores.size());

        } catch (PersistenciaException e) {
            avisarBancoFora();
        }
    }

    private void cadastrar() {

        status.setText("Pronto.");

        try {

            String nome = campoNome.getText().trim();
            String matricula = campoMatricula.getText().trim();
            int limite = Integer.parseInt(campoLimite.getText().trim());

            ValidadorLeitor.validar(nome, matricula, limite);

            boolean salvou = dao.adicionarLeitor(nome, matricula, limite);

            if (salvou) {

                campoNome.setText("");
                campoMatricula.setText("");
                carregarLeitores();
                status.setText("Leitor cadastrado: " + nome);

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Já existe um leitor com a matrícula " + matricula + "."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "O limite deve ser um número inteiro."
            );

        } catch (LeitorInvalidoException e) {

            JOptionPane.showMessageDialog(
                this,
                e.getMessage()
            );

        } catch (PersistenciaException e) {
            avisarBancoFora();
        }
    }

    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um leitor na tabela para excluir.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        String nome = (String) modelo.getValueAt(linha, 1);

        int opcao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir o leitor '" + nome + "'?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                if (dao.excluirLeitor(id)) {
                    carregarLeitores();
                    status.setText("Leitor excluído: " + nome);
                }
            } catch (PersistenciaException e) {
                avisarBancoFora();
            }
        }
    }

    private void avisarBancoFora() {

        status.setText("Erro ao consultar o banco.");

        JOptionPane.showMessageDialog(
            this,
            "Não foi possível falar com o banco. " +
            "Verifique a conexão com o MariaDB.",
            "Erro de conexão",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new TelaLeitores().setVisible(true);
        });
    }
}
