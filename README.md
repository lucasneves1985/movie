## **Instruções para iniciar o projeto.**

## **Rodar projeto através do CMD**

Após baixar o código fonte abrir o prompt de comando (Windows) ou terminal (Linux) na pasta principal do projeto e rodar os seguintes comandos:

    mvnw clean package
    java -jar target/goldenRaspberryAwards-1.0.jar

A aplicação irá iniciar na porta 8080, nesse cenário no momento do empacotamento será realizado os testes de integração, caso não deseje rodar os teste no momento do empacotamento adicionar `-DskipTests` no comando `mvnw clean package`.

## **Rodar projeto através do Docker**

Após baixar o código fonte abrir o prompt de comando (Windows) ou terminal (Linux) na pasta principal do projeto e rodar os seguintes comandos:

    mvnw clean package -DskipTests
    docker build -t goldenraspberryawards:1.0 .
    docker run --name goldenraspberryawards -p 8080:8080 -d goldenraspberryawards:1.0

A aplicação irá iniciar via o Docker na porta 8080, nesse cenário no momento do empacotamento será realizado os testes de integração, caso não deseje rodar os teste no momento do empacotamento adicionar `-DskipTests` no comando `mvnw clean package`.

Para parar a aplicação executar no prompt de comando (Windows) ou terminal (Linux)

    docker stop goldenraspberryawards `

Para Iniciar novamente a aplicação executar no prompt de comando (Windows) ou terminal (Linux)

    docker run goldenraspberryawards 

Para visualizar os logs executar no prompt de comando (Windows) ou terminal (Linux)

    docker logs goldenraspberryawards 

## **Testes de Integração**

Para realizar apenas ***testes de integração*** do projeto prompt de comando (Windows) ou terminal (Linux) na pasta principal do projeto rodar o seguinte comando:

    mvnw test

## **Documentação Open API (Swagger)**

A documentação foi desenvolvida com a Open API Swagger e seu acesso é através da do padrão de url: `http://<caminho_server>:<porta>/swagger-ui.html`.
Para acessar a documentação na maquina que esta rodando a aplicação por exemplo:
`http://localhost:8080/swagger-ui.html`

## **Acesso a base de dados H2**

O acesso ao console da base de dados H2 é através do padrão de url:
`http://<caminho_server>:<porta>/h3`.
Para acessar a documentação na maquina que esta rodando a aplicação por exemplo:
`http://localhost:8080/swagger-ui.html`

Utilizar as seguintes informações para acessar o console.
Driver Class: *org.h2.Driver*
JDBC URL: *jdbc:h2:mem:gra*
User Name: *admin*
O campo ***password*** não deve ser informado.

## **Padrão para arquivo de Importação**
Ao iniciar a aplicação é carregado as informações dos filmes contidas um arquivo CSV, que deverá estar na pasta `src/main/resources` e deve ter o nome de `movielist.csv` o delimitador padrão do arquivo deverá ser o ponto e virgula.
O arquivo deve conter na primeira linha um cabeçalho com as seguintes nomenclaturas:  
`year title studios winner producers`
A aplicação irá identificar através dessa nomenclatura de cabeçalho qual informação consta em determinada coluna.
No exemplo a seguir a aplicação assumirá que na coluna 1 estão as informações referente ao ano do filme, na coluna 2 o titulo do filme e assim por diante.

    year;title;studios;producers;winner  
    1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes  
    1980;Cruising;Lorimar Productions, United Artists;Jerry Weintraub;  
    1980;The Formula;MGM, United Artists;Steve Shagan;

A coluna onde constam os produtores (`producers`) tem como separador a virgula e a expressão " and " (com espaço antes e após)