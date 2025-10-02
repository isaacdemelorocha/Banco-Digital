package PrjBank.Banco.Digital.controller.dto;

/**
 * DTO para dados de requisição de transferência.
 * Contém o valor a ser transferido e o número da conta de destino.
 */
public record TransferenciaRequest(double valor, int numeroDestino) {
}