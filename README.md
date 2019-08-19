# Cálculo de melhor rota entre dois pontos

Esta aplicação implementa o cálculo de melhor rota entre dois pontos. É um problema clássico resolvido pelo algoritmo
de Dijkstra<sup>1</sup>. Esse algoritmo consiste em fazer uma busca em largura sobre um grafo dirigido e com custos em suas arestas
a fim de visitar todos os pontos e calcular todas as rotas possíveis entre dois pontos, escolhendo a de menor custo.

É necessário informar à aplicação um arquivo CSV com as rotas possíveis e seus custos, no seguinte formato:
```
pontoA,pontoB,10
pontoA,pontoC,5
pontoC,pontoB,2
```

A aplicação provê duas interfaces de consulta:
1. Interface via console - a aplicação inicia pedindo um trajeto no formato "pontoA-pontoB" e responde com o melhor
trajeto entre
os dois pontos, além do custo desse trajeto, no formato "pontoA - pontoC - pontoB > $custo".

2. Interface Rest - dois endpoints são expostos para consumo via HTTP, um para calcular o melhor trajeto (com saída
igual à da interface via console)
e outro para registrar uma nova rota. Mais detalhes abaixo na seção "Inteface Rest".

<sup>1</sup> https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

## Decisões tomadas

##### Linguagem e frameworks
A aplicação foi desenvolvida com a linguagem **Kotlin**<sup>2</sup>. A decisão de usar essa linguagem veio de sua simplicidade e facilidade
de desenvolvimento comparado ao Java. Mais conhecida como a nova linguagem oficial do Android, minha decisão também se
baseia em disseminar seu uso para backend.

Não foi necessária a adição de nenhuma biblioteca ou framework para desenvolver o algoritmo e a interface via console.
Porém, para prover a interface Rest, foi necessário um framework. O escolhido foi o **Javalin**<sup>3</sup>, que é um
*microframework* web extremamente leve e simples, que possibilita a criação de um web service Rest de maneira muito
simples, com pouca ou nenhuma configuração e com um conjunto muito básico de funcionalidades. A decisão de usá-lo se deu
pela sua simplicidade e facilidade de desenvolvimento, já que para esta aplicação necessitamos apenas da exposição do serviço
e suas rotas.

Além disso, foi necessário a biblioteca **jUnit 4** para execução de testes unitários. 

<sup>2</sup>https://kotlinlang.org

<sup>3</sup>https://javalin.io/

##### Design - Estrutura de pacotes e arquivos
O design da aplicação é inspirado no padrão de arquitetura MVC e possui três packages principais:
1. application: encapsula as classes de setup da aplicação e de interface com o usuário. Contém a classe principal (com
o método Main e o setup da interface Rest), e as classes de Controller do MVC;
2. domain: agrupa as regras de negócio e as classes que representam as entidades da aplicação;
3. resources: contém as classes de acesso a recursos externos (arquivos, bancos de dados, outros serviços etc).

## Como executar
Existem algumas maneiras possíveis para a execução da aplicação:

* Gradle

```./gradlew run --args "input-routes.csv" -Dport=8080```

Esse comando usa a task ```run``` do gradle para executar a aplicação. Apesar da facilidade,
não é recomendado pois o console fica poluído com os outputs do gradle em meio ao prompt da aplicação.

* Gradle + Java

Essa maneira separa o ```build``` da aplicação de sua execução. Para executar o build, deve-se executar:
```./gradlew build ```. Será gerado o jar *bexs-1.0.jar* na pasta ```build/libs```. Após isso, basta executar
```java -Dport=8080 -jar build/libs/bexs-1.0.jar input-routes.csv``` para a aplicação iniciar.

**Obs:** O parâmetro *port* é opcional e se não informado, usa a porta 7000.

## Interface Rest
Após a inicialização da aplicação, dois endpoints ficarão disponíveis.

* **GET /best-route**  
Recebe os *query params* **de** e **para** para informar a origem e destino.  
Exemplo de chamada:  
```curl -X GET 'http://localhost:7000/best-route?de=GRU&para=SCL'```  
Exemplo de resposta:
```best route: GRU - BRC - SCL > $15```

* **POST /register-route**  
Recebe no body uma string no mesmo formato do arquivo CSV de entrada (pontoA,pontoB,custo) e registra essa nova rota para
uso nas próximas consultas em ambas as interfaces.  
Exemplo de chamada:  
```curl -X POST 'http://localhost:7000/register-route' -d 'GRU,SCL,1'```  
Exemplo de resposta:
```route added```