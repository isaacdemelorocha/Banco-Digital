// Define o pacote para os serviços da aplicação.
package PrjBank.Banco.Digital.service;

// Importações de bibliotecas do Google API, Spring e Java.
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import PrjBank.Banco.Digital.model.Cliente;
import PrjBank.Banco.Digital.model.Conta;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Service: Marca a classe como um serviço do Spring para gerenciar a lógica de negócio.
 * Este serviço é responsável pela comunicação com a API do Google Sheets.
 */
@Service
public class GoogleSheetsService {

    // Constantes para configurar a conexão com a API.
    private static final String APPLICATION_NAME = "Banco Digital GFT";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String SPREADSHEET_RANGE = "Contas!A2:E"; // Aba "Contas", da célula A2 até a coluna E.

    // Injeta o ID da planilha do seu arquivo 'application.properties'.
    @Value("${google.spreadsheet.id}")
    private String SPREADSHEET_ID;

    // Objeto principal para interagir com a API do Google Sheets.
    private Sheets sheetsService;

    /**
     * Construtor que inicializa a conexão com a API do Google Sheets
     * usando as credenciais do arquivo 'credentials.json'.
     */
    public GoogleSheetsService() {
        try {
            InputStream in = GoogleSheetsService.class.getResourceAsStream("/credentials.json");
            GoogleCredential credential = GoogleCredential.fromStream(in)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

            sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lê todas as linhas da planilha e as converte em uma lista de objetos 'Conta'.
     * @return Uma lista de contas.
     */
    public List<Conta> lerContas() throws IOException {
        // Executa a requisição para buscar os valores da planilha.
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, SPREADSHEET_RANGE)
                .execute();

        List<List<Object>> values = response.getValues();
        List<Conta> contas = new ArrayList<>();

        if (values == null || values.isEmpty()) {
            return contas; // Retorna lista vazia se a planilha não tiver dados.
        }

        // Itera sobre cada linha ('row') da planilha.
        for (List<Object> row : values) {
            try {
                if (row.isEmpty() || row.get(0).toString().isEmpty()) continue; // Pula linhas vazias.

                // Converte os dados de cada coluna para os tipos corretos.
                int numero = Integer.parseInt(row.get(0).toString());
                int agencia = Integer.parseInt(row.get(1).toString());
                // Trata o formato de moeda (ex: "R$ 1.234,56") para converter para double.
                double saldo = Double.parseDouble(row.get(2).toString().replaceAll("[^0-9,]", "").replace(",", "."));
                String nomeTitular = row.get(3).toString();
                String cpfTitular = row.get(4).toString();

                // Cria os objetos do modelo.
                Cliente titular = new Cliente(nomeTitular, cpfTitular);
                Conta conta = new Conta(titular); // OBS: Este construtor pode precisar de mais parâmetros.

                // TODO: Corrigir na classe 'Conta'. Os erros que você marcou indicam
                // que a classe 'Conta' provavelmente não tem esses métodos 'set'.
                // Uma solução é criar um construtor que aceite todos os parâmetros, como:
                // new Conta(titular, numero, agencia, saldo);

                conta.setSaldo(saldo);

                contas.add(conta);
            } catch (Exception e) {
                System.err.println("Aviso: Erro ao processar a linha da planilha: " + row);
            }
        }
        return contas;
    }

    /**
     * Adiciona os dados de um objeto 'Conta' como uma nova linha na planilha.
     * @param conta O objeto Conta a ser adicionado.
     */
    public void adicionarLinha(Conta conta) throws IOException {
        // Monta a lista de valores que formará a nova linha.
        List<Object> novaLinha = List.of(
                conta.getNumero(),
                conta.getAgencia(),
                String.format("R$ %.2f", conta.getSaldo()), // Formata o saldo como moeda.
                conta.getTitular().getNome(),
                conta.getTitular().getCpf()
        );

        ValueRange body = new ValueRange().setValues(List.of(novaLinha));

        // Executa a requisição 'append' para adicionar a linha no final da aba.
        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "Contas!A:E", body)
                .setValueInputOption("USER_ENTERED") // Garante que o Google Sheets interprete os dados corretamente.
                .execute();
    }
}