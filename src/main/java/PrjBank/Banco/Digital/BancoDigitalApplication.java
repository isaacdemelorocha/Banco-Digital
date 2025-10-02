// Define o pacote que organiza as classes do projeto.
package PrjBank.Banco.Digital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication: Anotação principal que ativa a autoconfiguração,
 * busca por componentes e define configurações do Spring Boot.
 */
@SpringBootApplication
public class BancoDigitalApplication {

    /**
     * método main: Ponto de entrada padrão para a execução de uma aplicação Java.
     * É aqui que tudo começa.
     *
     * @param args Argumentos que podem ser passados via linha de comando.
     */
    public static void main(String[] args) {
        // SpringApplication.run(): Comando que efetivamente inicia a aplicação Spring Boot.
        SpringApplication.run(BancoDigitalApplication.class, args);
    }

}