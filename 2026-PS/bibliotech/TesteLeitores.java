/*
 * Disciplina: 2026-PS
 * Projeto   : bibliotech
 * Arquivo   : TesteLeitores.java
 * Autor     : Nickolas Kinceski Martins
 * Descricao : confere, contra o banco, o cadastro de leitores (Aula 48).
 */
import java.util.List;

public class TesteLeitores {

    private static int passaram = 0;
    private static int falharam = 0;

    private static void verificar(String requisito, boolean condicao) {
        if (condicao) {
            passaram = passaram + 1;
            System.out.println("OK      " + requisito);
        } else {
            falharam = falharam + 1;
            System.out.println("FALHOU  " + requisito);
        }
    }

    public static void main(String[] args) {
        LeitorDAO dao = new LeitorDAO();
        String matricula = "TESTE-48";

        Leitor sobra = dao.buscarLeitorPorMatricula(matricula);
        if (sobra != null) {
            dao.excluirLeitor(sobra.getId());
        }

        // RF02: o cadastro chega ao banco e volta de la como objeto.
        verificar("RF02 cadastro de leitor e salvo",
            dao.adicionarLeitor("Leitor de Teste", matricula, 2));

        Leitor doBanco = dao.buscarLeitorPorMatricula(matricula);
        verificar("RF02 leitor cadastrado aparece na busca por matricula",
            doBanco != null);
        verificar("RF02 leitor vem do banco com o id que o banco deu",
            doBanco != null && doBanco.getId() > 0);
        verificar("RF02 leitor que vem do banco pode pegar livro",
            doBanco != null && doBanco.podePegarEmprestado());

        // A regra: uma matricula, um leitor.
        verificar("RF02 matricula repetida e recusada",
            !dao.adicionarLeitor("Outra Pessoa", matricula, 3));

        boolean naLista = false;
        List<Leitor> leitores = dao.listarLeitores();
        for (Leitor leitor : leitores) {
            if (leitor.getMatricula().equals(matricula)) {
                naLista = true;
            }
        }
        verificar("RF02 leitor cadastrado aparece na listagem", naLista);

        // O teste apaga o que criou.
        if (doBanco != null) {
            dao.excluirLeitor(doBanco.getId());
        }
        verificar("o teste limpou o que criou",
            dao.buscarLeitorPorMatricula(matricula) == null);

        // Nível B: testes do ValidadorLeitor
        String erroNome = "";
        try {
            ValidadorLeitor.validar("", "2026999", 3);
        } catch (LeitorInvalidoException e) {
            erroNome = e.getMessage();
        }
        verificar("Validador recusa nome vazio", "O nome é obrigatório.".equals(erroNome));

        String erroMatricula = "";
        try {
            ValidadorLeitor.validar("Ana Silva", "", 3);
        } catch (LeitorInvalidoException e) {
            erroMatricula = e.getMessage();
        }
        verificar("Validador recusa matricula vazia", "A matrícula é obrigatória.".equals(erroMatricula));

        String erroLimite = "";
        try {
            ValidadorLeitor.validar("Ana Silva", "2026999", 9);
        } catch (LeitorInvalidoException e) {
            erroLimite = e.getMessage();
        }
        verificar("Validador recusa limite fora de 1 a 5", "O limite deve ficar entre 1 e 5 livros.".equals(erroLimite));

        System.out.println();
        System.out.println(passaram + " passaram, " + falharam + " falharam.");
    }
}
