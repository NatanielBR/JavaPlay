package javaplay.central.mascara;

import java.nio.file.Path;

public class MascaraCentralDesconhecido implements MascaraCentral {
	private final Path arq;
	public MascaraCentralDesconhecido(Path a) {
		arq = a;
	}

	@Override
	public String getFantasia() {
		return arq.getFileName().toString();
	}

	@Override
	public Path getArquivo() {
		return arq;
	}

	@Override
	public String getOriginal() {
		return arq.getFileName().toString();
	}

	@Override
	public String getFantasiaSemEp() {
		return "Desconhecido";
	}

}
