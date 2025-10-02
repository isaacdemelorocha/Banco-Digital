package PrjBank.Banco.Digital.controller.dto;

/**
 * DTO para o valor em requisições de depósito ou saque.
 */
public record TransacaoRequest(double valor) {
}