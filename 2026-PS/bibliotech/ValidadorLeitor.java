public class ValidadorLeitor {

    public static void validar(String nome, String matricula, int limite) throws LeitorInvalidoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new LeitorInvalidoException("O nome é obrigatório.");
        }
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new LeitorInvalidoException("A matrícula é obrigatória.");
        }
        if (limite < 1 || limite > 5) {
            throw new LeitorInvalidoException("O limite deve ficar entre 1 e 5 livros.");
        }
    }
}
