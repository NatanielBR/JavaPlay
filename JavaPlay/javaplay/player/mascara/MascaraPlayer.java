package javaplay.player.mascara;

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
	public boolean confirmarAberturaPulavel(String um);
	/**
	 * @return the aberturapulavel
	 */
	public boolean confirmarEncerramentoPulavel(String um);

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
