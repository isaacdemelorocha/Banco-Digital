package PrjBank.Banco.Digital.controller.dto;

/**
 * DTO para dados de requisição de criação de conta.
 * Contém o nome e CPF do novo cliente.
 */
public record CriarContaRequest(String nome, String cpf) {
}