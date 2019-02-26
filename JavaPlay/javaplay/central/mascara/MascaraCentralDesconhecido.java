package javaplay.central.mascara;

import java.io.File;

public class MascaraCentralDesconhecido implements MascaraCentral {
	private final File arq;
	public MascaraCentralDesconhecido(File a) {
		arq = a;
	}

	@Override
	public String getFantasia() {
		return arq.getName();
	}

	@Override
	public File getArquivo() {
		return arq;
	}

	@Override
	public String getOriginal() {
		return arq.getName();
	}

	@Override
	public String getNomeDaMascara() {
		return "Desconhecido";
	}

	@Override
	public String getFantasiaSemEp() {
		return getNomeDaMascara();
	}

}
