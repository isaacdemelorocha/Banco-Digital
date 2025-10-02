// URL base da nossa API. Facilita a manutenção se a URL mudar.
const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Função principal para buscar as contas da API e atualizar a tabela no HTML.
 * Esta é a função que faz as atualizações aparecerem na tela.
 */
async function carregarContas() {
    try {
        const response = await fetch(`${API_BASE_URL}/contas`);
        if (!response.ok) {
            throw new Error('Falha ao carregar contas da API.');
        }
        const contas = await response.json();

        const corpoTabela = document.getElementById('corpo-tabela-contas');
        corpoTabela.innerHTML = ''; // Limpa a tabela antes de preencher com os dados novos.

        if (contas.length === 0) {
            corpoTabela.innerHTML = `<tr><td colspan="3">Nenhuma conta cadastrada ainda.</td></tr>`;
            return;
        }

        contas.forEach(conta => {
            const linha = `
                <tr>
                    <td>${conta.numero}</td>
                    <td>${conta.titular.nome}</td>
                    <td>${conta.saldo.toFixed(2)}</td>
                </tr>
            `;
            corpoTabela.innerHTML += linha;
        });

    } catch (error) {
        console.error("Erro ao carregar contas:", error);
        alert('Não foi possível carregar as contas.');
    }
}

/**
 * Função para lidar com a criação de uma nova conta.
 */
async function criarConta() {
    const nome = document.getElementById('input-nome-cliente').value;
    const cpf = document.getElementById('input-cpf-cliente').value;

    if (!nome || !cpf) return alert("Por favor, preencha nome e CPF.");

    try {
        const response = await fetch(`${API_BASE_URL}/contas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, cpf })
        });

        if (response.ok) {
            alert('Conta criada com sucesso!');
            carregarContas(); // <-- PONTO CHAVE: Atualiza a tabela após a operação.
        } else {
            alert('Falha ao criar conta.');
        }
    } catch (error) {
        console.error("Erro ao criar conta:", error);
        alert('Ocorreu um erro na comunicação com o servidor.');
    }
}

/**
 * Função para lidar com depósitos.
 */
async function depositar() {
    const numero = document.getElementById('input-deposito-conta').value;
    const valor = document.getElementById('input-deposito-valor').value;

    if (!numero || !valor) return alert("Preencha o número da conta e o valor.");

    try {
        const response = await fetch(`${API_BASE_URL}/contas/${numero}/deposito`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ valor: parseFloat(valor) })
        });

        const resultado = await response.text();
        alert(resultado);

        if (response.ok) carregarContas(); // <-- PONTO CHAVE: Atualiza a tabela.
    } catch (error) {
        console.error("Erro ao depositar:", error);
        alert('Ocorreu um erro na comunicação com o servidor.');
    }
}

/**
 * Função para lidar com saques.
 */
async function sacar() {
    const numero = document.getElementById('input-saque-conta').value;
    const valor = document.getElementById('input-saque-valor').value;

    if (!numero || !valor) return alert("Preencha o número da conta e o valor.");

    try {
        const response = await fetch(`${API_BASE_URL}/contas/${numero}/saque`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ valor: parseFloat(valor) })
        });

        const resultado = await response.text();
        alert(resultado);

        if (response.ok) carregarContas(); // <-- PONTO CHAVE: Atualiza a tabela.
    } catch (error) {
        console.error("Erro ao sacar:", error);
        alert('Ocorreu um erro na comunicação com o servidor.');
    }
}

/**
 * Função para lidar com transferências.
 */
async function transferir() {
    const origem = document.getElementById('input-transf-origem').value;
    const destino = document.getElementById('input-transf-destino').value;
    const valor = document.getElementById('input-transf-valor').value;

    if (!origem || !destino || !valor) {
        return alert("Preencha todos os campos para a transferência.");
    }

    try {
        const response = await fetch(`${API_BASE_URL}/contas/${origem}/transferencia`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                valor: parseFloat(valor),
                numeroDestino: parseInt(destino)
            })
        });

        const resultado = await response.text();
        alert(resultado);

        if (response.ok) carregarContas(); // <-- PONTO CHAVE: Atualiza a tabela.

    } catch (error) {
        console.error("Erro ao transferir:", error);
        alert('Ocorreu um erro na comunicação com o servidor.');
    }
}

/**
 * Função para buscar e exibir o extrato de uma conta.
 */
async function emitirExtrato() {
    const numero = document.getElementById('input-extrato-conta').value;
    if (!numero) {
        return alert("Por favor, digite o número da conta.");
    }

    try {
        const response = await fetch(`${API_BASE_URL}/contas/${numero}`);

        if (response.ok) {
            const conta = await response.json();
            const extratoFormatado = `
                --- Extrato da Conta ---
                Número: ${conta.numero}
                Agência: ${conta.agencia}
                Titular: ${conta.titular.nome}
                CPF: ${conta.titular.cpf}
                Saldo: R$ ${conta.saldo.toFixed(2)}
            `;
            alert(extratoFormatado);
        } else {
            alert('Conta não encontrada.');
        }

    } catch (error) {
        console.error("Erro ao emitir extrato:", error);
        alert('Ocorreu um erro ao buscar o extrato.');
    }
}


// --- Event Listeners ---
// Conecta todas as nossas funções aos botões do HTML.

document.addEventListener('DOMContentLoaded', carregarContas);
document.getElementById('btn-criar-conta').addEventListener('click', criarConta);
document.getElementById('btn-depositar').addEventListener('click', depositar);
document.getElementById('btn-sacar').addEventListener('click', sacar);
document.getElementById('btn-transferir').addEventListener('click', transferir);
document.getElementById('btn-extrato').addEventListener('click', emitirExtrato);