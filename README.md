# JavaPlay
Um player feito em java e utilizando a biblioteca do VLC voltado com recursos para series e animes.
# Pré requisito
- Java (Onde tudo irá rodar) [Link Oficial](https://www.java.com/pt_BR/)
- VLC  (O próprio player) [Link Oficial](https://www.videolan.org/index.pt-BR.html)
A instalação é bem simples e intuitiva, não irei fazer um "tutorial" sobre o mesmo.
# Guia de instalação
Baixe a versão mais recente do JavaPlay ([Aqui](https://github.com/NatanielBR/JavaPlay/releases)).
Logo após, recomendo colocar em uma pasta qualquer e criar um atalho para o Desktop pois o mesmo irá criar outros arquivos.
# Funções
Ao abrir pela primeira vez irá surgir uma janela para escolher o diretorio. O diretorio será onde o programa irá "escanear" a procura de arquivos de video, compativeis com o VLC.

![](https://i.imgur.com/yv4n99R.png)

Após escolher o diretorio, uma nova janela irá aparecer com todos os arquivos daquele diretorio (A busca é recursiva). Escolha a serie, ou qualquer outra coisa/ que deseja assistir.

![](https://imgur.com/vYBRCQq.png)

Esse é o ~~simples~~ player do JavaPlay, na versão 1.0.0 só é possivel alterar o volume, pausar e alterar o tempo do video.
![](https://imgur.com/d0tmybm.png)

A principal diferença entre o JavaPlay e os outros é essa função e outras futuras: Ver o tempo assistido (de maneira facil) de cada video.
![](https://imgur.com/xWvGirg.png)

# Agradecimentos
Esse programa nao foi feito unicamente por mim, nela existe bibliotecas que foram criadas por outras pessoas.
- VLCJ - Usado para se "comunicar" com o VLC [Reporsitorio](https://github.com/caprica/vlcj)
- Apache Commons IO - Usado para "ver" os arquivos no diretorio (Talvez vai sair :) ) [Reporsitorio](https://github.com/apache/commons-io)
- SQLITE JDBC - Usado para se "comunicar" com o banco de dados (O arquivo "Database.db3") [Reporsitorio](https://github.com/xerial/sqlite-jdbc)

- LongJSliter - Originalmente é DoubleJSliter porém foi modificado para ser Long e merece meus agradecimentos. [Projeto](https://github.com/nasa/trick)
