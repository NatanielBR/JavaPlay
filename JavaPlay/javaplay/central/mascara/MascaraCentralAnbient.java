package javaplay.central.mascara;

import java.nio.file.Path;

public class MascaraCentralAnbient implements MascaraCentral {
	private final String ORIGINAL;
	private final String FANTASIA;
	private final Path ARQUIVO;
	private final String FANTASIA_SEM_EP;
	@Override
	public String getFantasia() {
		return FANTASIA;
	}

	@Override
	public Path getArquivo() {
		return ARQUIVO;
	}

	@Override
	public String getOriginal() {
		return ORIGINAL;
	}

	@Override
	public String getFantasiaSemEp() {
		// TODO Auto-generated method stub
		return FANTASIA_SEM_EP;
	}
	
	public MascaraCentralAnbient(Path cam) {
		String nome = cam.getFileName().toString();
		ARQUIVO = cam;
		ORIGINAL = nome;
		FANTASIA = nome.substring(0, nome.lastIndexOf("_")).replace("_", " ");
		String temp = FANTASIA.replaceAll("\\d", "");
		FANTASIA_SEM_EP = temp.substring(0,temp.length()-1);
	}

}
