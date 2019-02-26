package javaplay.central.mascara;

import java.io.File;

public interface MascaraCentral {
	public String getFantasia();
	public File getArquivo();
	public String getOriginal();
	public String getNomeDaMascara();
	public String getFantasiaSemEp();

	public static MascaraCentral obterMascaraCompativel(File arq) {
		String nome = arq.getName();
		
		if (nome.startsWith("[EA]")) {
			return new MascaraCentralEternalAnimes(arq);
		}else {
			return new MascaraCentralDesconhecido(arq);
		}
	}
}
