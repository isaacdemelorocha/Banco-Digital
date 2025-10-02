package PrjBank.Banco.Digital.model;

/**
 * Representa a entidade Cliente no sistema bancário, armazenando nome e CPF.
 */
public class Cliente {

    private String nome;
    private String cpf;

    /**
     * Construtor para criar uma nova instância de Cliente.
     *
     * @param nome Nome completo do cliente.
     * @param cpf CPF do cliente.
     */
    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    /**
     * Retorna o nome completo do cliente.
     * @return O nome do cliente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o CPF do cliente.
     * @return O CPF do cliente.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Retorna uma representação textual do objeto Cliente (nome e CPF).
     * @return String formatada com os dados do cliente.
     */
    @Override
    public String toString() {
        return "Cliente{" + "nome='" + nome + '\'' + ", cpf='" + cpf + '\'' + '}';
    }
}