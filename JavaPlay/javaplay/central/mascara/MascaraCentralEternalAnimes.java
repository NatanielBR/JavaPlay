package javaplay.central.mascara;

import java.io.File;

public class MascaraCentralEternalAnimes implements MascaraCentral{
	// [EA]Log_Horizon_01_[1280x720][BDRIP][Hi10p][758A8215]
	private final String ORIGINAL;
	private final String FANTASIA;
	private final File ARQUIVO;
	private final String FANTASIA_SEM_EP;
	public MascaraCentralEternalAnimes(File f) {
		String nome = f.getName();
		nome = nome.substring(4);
		nome = nome.substring(0, nome.indexOf("[")) ;
		nome = nome.replace("_", " ");
		nome = nome.replaceAll("V\\d", "");
		while (nome.endsWith(" ")) {
			nome = nome.substring(0,nome.length()-1);
		}
		FANTASIA = nome;
		ORIGINAL = f.getName();
		FANTASIA_SEM_EP = nome.substring(0, nome.length()-3);
		ARQUIVO = f;
	}

	/**
	 * @return the fantasia
	 */
	public String getFantasia() {
		return FANTASIA;
	}

	/**
	 * @return the aRQUIVO
	 */
	public File getArquivo() {
		return ARQUIVO;
	}
	public String getNomeDaMascara() {
		return "Eternal Animes";
	}
	/**
	 * @return the original
	 */
	public String getOriginal() {
		return ORIGINAL;
	}

	@Override
	public String getFantasiaSemEp() {
		return FANTASIA_SEM_EP;
	}

}