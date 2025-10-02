package PrjBank.Banco.Digital.model;

import PrjBank.Banco.Digital.exception.SaldoInsuficienteException;

/**
 * Representa a entidade Conta bancária.
 */
public class Conta {

    // Constantes e sequencial
    private static final int AGENCIA_PADRAO = 1;
    private static int SEQUENCIAL = 1;

    // Atributos da conta
    protected int agencia;
    protected int numero;
    protected double saldo;
    protected Cliente titular;

    // Construtor
    public Conta(Cliente titular) {
        this.agencia = AGENCIA_PADRAO;
        this.numero = SEQUENCIAL++;
        this.titular = titular;
        this.saldo = 0.0;
    }

    // Getters
    public int getAgencia() {
        return agencia;
    }

    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public Cliente getTitular() {
        return titular;
    }

    // Setter para saldo (necessário para depósito/saque direto no atributo)
    // Embora possa ser evitado usando apenas os métodos depositar/sacar
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    /**
     * Adiciona um valor positivo ao saldo da conta.
     * @param valor Valor a ser depositado.
     */
    public void depositar(double valor) {
        if (valor <= 0) {
            System.err.println("Valor de depósito deve ser positivo.");
            return;
        }
        this.saldo += valor;
        System.out.printf("Depósito de R$ %.2f realizado com sucesso.%n", valor);
    }

    /**
     * Remove um valor positivo do saldo da conta.
     * @param valor Valor a ser sacado.
     * @throws SaldoInsuficienteException Se o saldo for menor que o valor.
     */
    public void sacar(double valor) throws SaldoInsuficienteException {
        if (valor <= 0) {
            System.err.println("Valor de saque deve ser positivo.");
            return;
        }

        if (this.saldo < valor) {
            throw new SaldoInsuficienteException(String.format(
                    "Saldo insuficiente. Saldo atual: R$ %.2f, Tentativa de saque: R$ %.2f", this.saldo, valor));
        }

        this.saldo -= valor;
        System.out.printf("Saque de R$ %.2f realizado com sucesso.%n", valor);
    }

    /**
     * Transfere um valor para outra conta, realizando saque na conta atual e depósito na conta de destino.
     * @param valor Valor a ser transferido.
     * @param contaDestino Conta para onde o valor será transferido.
     * @throws SaldoInsuficienteException Se o saldo for menor que o valor.
     */
    public void transferir(double valor, Conta contaDestino) throws SaldoInsuficienteException {
        this.sacar(valor);
        contaDestino.depositar(valor);
        System.out.printf("Transferência de R$ %.2f para %s realizada com sucesso.%n",
                valor, contaDestino.getTitular().getNome());
    }

    /**
     * Imprime o extrato detalhado da conta no console.
     */
    public void imprimirExtrato() {
        System.out.println("=== Extrato da Conta ===");
        System.out.println(String.format("Titular: %s", this.titular.getNome()));
        System.out.println(String.format("Agência: %d", this.agencia));
        System.out.println(String.format("Número: %d", this.numero));
        System.out.println(String.format("Saldo: R$ %.2f", this.saldo));
    }
}