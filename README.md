# JavaPlay
Um player feito em java e utilizando a biblioteca do VLC voltado com recursos para series e animes.
# Pré requisito
- Java (Onde tudo irá rodar) [Link Oficial](https://www.java.com/pt_BR/)
- VLC  (O próprio player) [Link Oficial](https://www.videolan.org/index.pt-BR.html)
- A instalação é bem simples e intuitiva, não irei fazer um "tutorial" sobre o mesmo.
# Guia de instalação
1. Baixe a versão mais recente do JavaPlay ([Aqui](https://github.com/NatanielBR/JavaPlay/releases)).
2. Logo após, recomendo colocar em uma pasta qualquer e criar um atalho para o Desktop pois o mesmo irá criar outros arquivos.
# Funções
- Ao abrir pela primeira vez irá surgir uma janela para escolher o diretorio. O diretorio será onde o programa irá "escanear" a procura de arquivos de video, compativeis com o VLC. Na atual versão é possivel alterar de uma maneira facil dentro do programa.

![](https://i.imgur.com/yv4n99R.png)

- Após escolher o diretorio, uma nova janela irá aparecer com todos os arquivos daquele diretorio (A busca é recursiva). Escolha a serie, ou qualquer outra coisa/ que deseja assistir. A principal diferença entre o JavaPlay e outros Video Player's é poder ver o tempo assistido de maneira rapida e clara e é possivel a rapida passagem de aberturas e de episodios (caso seus respectivos valores sejam sim). 

![](https://imgur.com/j4sBhVC.png)

- Esse é o ~~simples~~ player do JavaPlay, na versão 2.1.2 é possivel alterar o volume, pausar e alterar o tempo do video, tendo as notificaçoes do video (como a mudança do capitulo, do volume e de pausa) na tela. Tambem é possivel assistir em FullScreen com a tecla 'f'.

![](https://imgur.com/RJNWhwp.png)

- Dentro do arquivo JavaPlay.properties é possivel inserir "pularAbertura" e "proximoArquivo" (sem aspas) tendo como valores "true" e "false" (sem aspas também). Caso o player reconheça o arquivo, ele ira pular a abertura (se pularAbertura for true) e pulará para o proximo arquivo (caso o proximoArquivo seja true e a mascara permita).

- Caso queira contribuir sobre as mascaras que se encaixe com o seu arquivo, abra um pedido em [Issues](https://github.com/NatanielBR/JavaPlay/issues) informando os arquivos "modelo", e caso queira relatar bugs ou novas funções utilize o mesmo.

# Agradecimentos
Esse programa nao foi feito unicamente por mim, nela existe bibliotecas que foram criadas por outras pessoas.
- VLCJ - Usado para se "comunicar" com o VLC [Reporsitorio](https://github.com/caprica/vlcj)
- SQLITE JDBC - Usado para se "comunicar" com o banco de dados (O arquivo "Database.db3") [Reporsitorio](https://github.com/xerial/sqlite-jdbc)

- LongJSliter - Originalmente é DoubleJSliter porém foi modificado para ser Long e merece meus agradecimentos. [Projeto](https://github.com/nasa/trick)
