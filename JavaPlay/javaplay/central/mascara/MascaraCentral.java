package javaplay.central.mascara;

import java.io.File;
import java.nio.file.Path;

public interface MascaraCentral {
	public String getFantasia();
	public Path getArquivo();
	public String getOriginal();
	public String getFantasiaSemEp();

	public static MascaraCentral obterMascaraCompativel(Path arq) {
		String nome = arq.getFileName().toString();
		
		if (nome.startsWith("[EA]")) {
			return new MascaraCentralEternalAnimes(arq);
		}else if (nome.contains("Anbient")){
			return new MascaraCentralAnbient(arq);
		}else {
			return new MascaraCentralDesconhecido(arq);
		}
	}
}
