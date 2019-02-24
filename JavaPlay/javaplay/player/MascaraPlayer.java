package javaplay.player;

import java.io.File;

public interface MascaraPlayer {
	/**
	 * @return the dicionario
	 */
	public String[] getDicionario();

	/**
	 * @return the pulavel
	 */
	public String[] getPulavel();
	/**
	 * @return the abertura pulavel
	 */
	public int getAberturaPulavel();
	/**
	 * @return the aberturapulavel
	 */
	public int getEncerramentoPulavel();

	/**
	 * @return the proximo
	 */
	public String getProximo();
	
	public static MascaraPlayer descobrirMascara(File arq) {
		if (arq.getName().startsWith("[EA]")) {
			return new MascaraPlayerEternalAnimes();
		}
		return null;
	}
}
