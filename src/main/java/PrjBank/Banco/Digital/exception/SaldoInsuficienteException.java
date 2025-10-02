package PrjBank.Banco.Digital.exception;

/**
 * Exceção customizada para representar uma falha de regra de negócio.
 * <p>
 * É lançada quando uma operação, como um saque ou transferência, não pode ser
 * concluída por falta de saldo na conta. O uso de uma exceção específica
 * torna o código mais claro e o tratamento de erros mais robusto, separando
 * erros de sistema de erros de lógica de negócio.
 *
 * @author Seu Nome Aqui
 */
// Ao herdar de 'Exception', esta se torna uma "checked exception".
// Isso obriga os desenvolvedores que chamam métodos que podem lançá-la
// a tratar o erro explicitamente (com try-catch ou throws), tornando o código mais seguro.
public class SaldoInsuficienteException extends Exception {

    /**
     * Construtor que recebe a mensagem de erro detalhada.
     *
     * @param message A mensagem que descreve o erro ocorrido. Esta mensagem será
     * útil para logs ou para informar o usuário sobre o problema.
     */
    public SaldoInsuficienteException(String message) {
        // A palavra-chave 'super' chama o construtor da classe pai (Exception),
        // passando a mensagem de erro para que ela seja armazenada e possa ser
        // recuperada posteriormente, por exemplo, pelo método getMessage().
        super(message);
    }
}