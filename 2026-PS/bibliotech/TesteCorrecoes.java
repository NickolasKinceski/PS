import java.util.List;

public class TesteCorrecoes {

    private static int passaram = 0;
    private static int falharam = 0;

    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            passaram++;
            System.out.println("OK      " + descricao);
        } else {
            falharam++;
            System.out.println("FALHOU  " + descricao);
        }
    }

    public static void main(String[] args) {
        LivroDAO dao = new LivroDAO();

        // P1: livro que vem do banco nasce disponível
        List<Livro> lista = dao.listarLivros();
        verificar("P1 livro da listagem nasce disponivel", !lista.isEmpty() && lista.get(0).estaDisponivel());

        int idPrimeiro = lista.get(0).getId();
        Livro porId = dao.buscarLivroPorId(idPrimeiro);
        verificar("P1 livro da busca por id nasce disponivel", porId != null && porId.estaDisponivel());

        // P3: cadastro válido, busca por título e limpeza
        String tituloTeste = "Livro de teste da Aula 47";
        boolean inseriu = dao.adicionarLivro(tituloTeste, "Autor Teste", 2024);
        verificar("P3 cadastro valido e salvo", inseriu);

        List<Livro> encontrados = dao.buscarLivrosPorTitulo(tituloTeste);
        verificar("P3 livro cadastrado aparece na busca por titulo", !encontrados.isEmpty());

        boolean removeu = false;
        if (!encontrados.isEmpty()) {
            removeu = dao.excluirLivro(encontrados.get(0).getId());
        }
        verificar("P3 o teste limpou o que criou", removeu);

        // Nível A (P5): escrita em ID inexistente retorna false (sem lançar exceção de conexão)
        boolean atualizouInexistente = dao.atualizarLivro(-999, "Titulo", "Autor", 2020);
        boolean excluiuInexistente = dao.excluirLivro(-999);
        verificar("P5 escrita em registro inexistente retorna false", !atualizouInexistente && !excluiuInexistente);

        System.out.println();
        System.out.println(passaram + " passaram, " + falharam + " falharam.");
    }
}
