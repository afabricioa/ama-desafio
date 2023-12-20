# AMA Desafio - API Spring Boot
### Bem-vindo ao repositório da API Spring Boot do AMA Desafio! 

Aqui estão as instruções para iniciar o projeto e algumas informações importantes.

## Acesso à API
A API está implantada na AWS, e você pode acessar a interface do Swagger pelo link: 
[Deploy AWS](http://54.147.166.247:8080/swagger-ui/index.html)

## Instruções de Inicialização
Siga estas etapas para iniciar o projeto em seu ambiente local:

1. Clone o repositório.
2. No arquivo application.properties, substitua as variáveis ${DATABASE_PASSWORD}, ${DATABASE_USERNAME}, e ${DATABASE_URL} com as informações do seu banco de dados.
3. Crie o schema "amadesafio" no seu banco de dados.
4. Inicie o projeto executando a classe principal.

**Dados Iniciais**

Ao iniciar o projeto pela primeira vez, dois usuários serão criados automaticamente:

**Login: admin, Senha: admin**

**Login: estoquista, Senha: estoquista**

## Tecnologias/Dependências Utilizadas
1. Java 17
2. Spring Web MVC
3. Spring Tools
4. SpringBoot versão 3.2.0
5. Lombok
6. Spring Security
7. Java JWT
8. Spring Doc
9. Apache POI para Relatórios
10. Hibernate Envers
11. Docker
12. AWS
13. Github Actions

Para mais informações meu e-mail de contato é fabricio.a.s@hotmail.com
