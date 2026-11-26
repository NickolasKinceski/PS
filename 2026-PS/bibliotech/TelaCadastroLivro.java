import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaCadastroLivro extends JDialog {

    private final LivroDAO dao = new LivroDAO();

    private final JTextField campoTitulo = new JTextField(22);
    private final JTextField campoAutor = new JTextField(22);
    private final JTextField campoAno = new JTextField(8);

    public TelaCadastroLivro(Frame owner) {
        super(owner, "Novo Livro", true);
        setSize(420, 240);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new EmptyBorder(14, 16, 14, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painel.add(campoTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painel.add(campoAutor, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painel.add(campoAno, gbc);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnSalvar = new JButton("Salvar");

        botoes.add(btnCancelar);
        botoes.add(btnSalvar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(botoes, gbc);

        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> salvar());

        setContentPane(painel);
    }

    private void salvar() {
        try {
            String titulo = campoTitulo.getText().trim();
            String autor = campoAutor.getText().trim();
            String anoTexto = campoAno.getText().trim();

            if (anoTexto.isEmpty()) {
                throw new NumberFormatException();
            }

            int ano = Integer.parseInt(anoTexto);

            ValidadorLivro.validar(titulo, autor, ano);

            if (dao.existeLivro(titulo, autor)) {
                throw new LivroInvalidoException("Já existe um livro cadastrado com este título e autor.");
            }

            if (dao.adicionarLivro(titulo, autor, ano)) {
                JOptionPane.showMessageDialog(this, "Livro cadastrado.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o livro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "O ano deve ser um número inteiro.",
                "Entrada inválida",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (LivroInvalidoException e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Dados inválidos",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (PersistenciaException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Erro de conexão ao salvar o livro no banco de dados.\nVerifique o MariaDB.",
                "Erro de conexão",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
