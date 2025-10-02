package PrjBank.Banco.Digital.controller;

import PrjBank.Banco.Digital.controller.dto.CriarContaRequest;
import PrjBank.Banco.Digital.controller.dto.TransacaoRequest;
import PrjBank.Banco.Digital.controller.dto.TransferenciaRequest;
import PrjBank.Banco.Digital.exception.SaldoInsuficienteException;
import PrjBank.Banco.Digital.model.Cliente;
import PrjBank.Banco.Digital.model.Conta;
import PrjBank.Banco.Digital.service.Banco;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para operações de Contas bancárias.
 */
@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final Banco banco;

    /**
     * Construtor injeta o serviço Banco.
     * @param banco Serviço que gerencia a lógica de negócio do banco.
     */
    public ContaController(Banco banco) {
        this.banco = banco;
    }

    /**
     * Lista todas as contas existentes.
     * Mapeia para GET /api/contas
     * @return Lista de todas as contas.
     */
    @GetMapping
    public List<Conta> listarTodasAsContas() {
        return banco.getContas();
    }

    /**
     * Busca uma conta específica pelo número.
     * Mapeia para GET /api/contas/{numeroConta}
     * @param numeroConta O número da conta na URL.
     * @return 200 (OK) com a conta ou 404 (Not Found).
     */
    @GetMapping("/{numeroConta}")
    public ResponseEntity<Conta> buscarContaPorNumero(@PathVariable int numeroConta) {
        Optional<Conta> contaOpt = banco.buscarConta(numeroConta);
        return contaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova conta com o cliente fornecido.
     * Mapeia para POST /api/contas
     * @param request Dados do cliente (nome e CPF).
     * @return A Conta recém-criada.
     */
    @PostMapping
    public Conta criarConta(@RequestBody CriarContaRequest request) {
        Cliente novoCliente = new Cliente(request.nome(), request.cpf());
        Conta novaConta = new Conta(novoCliente);
        banco.adicionarConta(novaConta);
        return novaConta;
    }

    /**
     * Realiza um depósito na conta especificada.
     * Mapeia para POST /api/contas/{numeroConta}/deposito
     * @param numeroConta O número da conta na URL.
     * @param request Valor a ser depositado.
     * @return 200 (OK) em caso de sucesso ou 404 (Not Found).
     */
    @PostMapping("/{numeroConta}/deposito")
    public ResponseEntity<String> depositar(@PathVariable int numeroConta, @RequestBody TransacaoRequest request) {
        Optional<Conta> contaOpt = banco.buscarConta(numeroConta);
        if (contaOpt.isPresent()) {
            Conta conta = contaOpt.get();
            conta.depositar(request.valor());
            String mensagemSucesso = String.format("Depósito de R$ %.2f realizado com sucesso na conta %d.", request.valor(), numeroConta);
            return ResponseEntity.ok(mensagemSucesso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Realiza um saque na conta especificada.
     * Mapeia para POST /api/contas/{numeroConta}/saque
     * @param numeroConta O número da conta na URL.
     * @param request Valor a ser sacado.
     * @return 200 (OK), 400 (Bad Request) se saldo insuficiente ou 404 (Not Found).
     */
    @PostMapping("/{numeroConta}/saque")
    public ResponseEntity<String> sacar(@PathVariable int numeroConta, @RequestBody TransacaoRequest request) {
        Optional<Conta> contaOpt = banco.buscarConta(numeroConta);
        if (contaOpt.isPresent()) {
            Conta conta = contaOpt.get();
            try {
                conta.sacar(request.valor());
                String mensagemSucesso = String.format("Saque de R$ %.2f realizado com sucesso na conta %d.", request.valor(), numeroConta);
                return ResponseEntity.ok(mensagemSucesso);
            } catch (SaldoInsuficienteException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Realiza uma transferência entre duas contas.
     * Mapeia para POST /api/contas/{numeroOrigem}/transferencia
     * @param numeroOrigem O número da conta de origem.
     * @param request Dados da transferência (valor e conta de destino).
     * @return 200 (OK), 400 (Bad Request) ou 404 (Not Found).
     */
    @PostMapping("/{numeroOrigem}/transferencia")
    public ResponseEntity<String> transferir(@PathVariable int numeroOrigem, @RequestBody TransferenciaRequest request) {
        Optional<Conta> contaOrigemOpt = banco.buscarConta(numeroOrigem);
        Optional<Conta> contaDestinoOpt = banco.buscarConta(request.numeroDestino());

        if (contaOrigemOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Conta de origem não encontrada.");
        }
        if (contaDestinoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Conta de destino não encontrada.");
        }

        try {
            Conta contaOrigem = contaOrigemOpt.get();
            Conta contaDestino = contaDestinoOpt.get();
            contaOrigem.transferir(request.valor(), contaDestino);
            String mensagemSucesso = String.format("Transferência de R$ %.2f da conta %d para a conta %d realizada com sucesso.",
                    request.valor(), numeroOrigem, request.numeroDestino());
            return ResponseEntity.ok(mensagemSucesso);
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}