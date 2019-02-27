package javaplay.player.mascara;

import java.nio.file.Path;

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
	
	public static MascaraPlayer descobrirMascara(Path arq) {
		if (arq.getFileName().startsWith("[EA]")) {
			return new MascaraPlayerEternalAnimes();
		}
		return null;
	}
}
