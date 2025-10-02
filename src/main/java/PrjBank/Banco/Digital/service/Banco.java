// Define o pacote para os serviços da aplicação.
package PrjBank.Banco.Digital.service;

// Importações dos modelos, do framework Spring e de utilitários do Java.
import PrjBank.Banco.Digital.model.Conta;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Service: Gerencia as operações e a lógica de negócio relacionadas às contas do banco.
 * Centraliza o acesso e a manipulação da lista de contas.
 */
@Service
public class Banco {

    // Nome da instituição bancária.
    private String nome;

    // Lista que armazena todas as contas gerenciadas pelo banco.
    private List<Conta> contas;

    /**
     * Construtor: Inicializa o banco com um nome padrão e uma lista de contas vazia.
     */
    public Banco() {
        this.nome = "Banco Digital";
        this.contas = new ArrayList<>();
    }

    /**
     * Adiciona uma nova conta à lista do banco.
     * @param conta O objeto Conta a ser adicionado.
     */
    public void adicionarConta(Conta conta) {
        this.contas.add(conta);
        System.out.printf("Conta de %s adicionada com sucesso.%n", conta.getTitular().getNome());
    }

    /**
     * Busca uma conta específica pelo número.
     * @param numero O número da conta a ser encontrada.
     * @return Um Optional contendo a conta se encontrada, ou vazio caso contrário.
     */
    public Optional<Conta> buscarConta(int numero) {
        // Usa a Stream API para uma busca mais eficiente e declarativa.
        return this.contas.stream()
                .filter(conta -> conta.getNumero() == numero)
                .findFirst();
    }

    /**
     * Retorna a lista completa de contas do banco.
     * @return A lista de contas.
     */
    public List<Conta> getContas() {
        return this.contas;
    }

    /**
     * Imprime no console um relatório com as informações de todas as contas cadastradas.
     */
    public void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada no banco.");
            return;
        }
        System.out.println("\n=== Lista de Contas do Banco " + this.nome + " ===");
        for (Conta conta : contas) {
            // Imprime os detalhes formatados de cada conta.
            System.out.printf("Ag: %d | CC: %d | Titular: %s | Saldo: R$ %.2f%n",
                    conta.getAgencia(), conta.getNumero(), conta.getTitular().getNome(), conta.getSaldo());
        }
        System.out.println("=========================================");
    }
}