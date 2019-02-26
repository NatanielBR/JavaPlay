package javaplay.player.mascara;

public class MascaraPlayerEternalAnimes implements MascaraPlayer{
	private String[] dicionario;
	/**
	 * @return the dicionario
	 */
	public String[] getDicionario() {
		return dicionario;
	}

	/**
	 * @return the pulavel
	 */
	public String[] getPulavel() {
		return pulavel;
	}

	/**
	 * @return the proximo
	 */
	public String getProximo() {
		return proximo;
	}

	private String[] pulavel;
	private String proximo;

	public MascaraPlayerEternalAnimes() {
		this.dicionario = new String[] {"Prólogo", "Abertura","Parte A","Parte B","Encerramento","Prévia"};
		this.pulavel = new String[] {"Prólogo","Abertura"};
		this.proximo =  "Encerramento";
	}

	@Override
	public boolean confirmarAberturaPulavel(String um) {
		String dic = dicionario[1];
		return um.replaceAll(" ", "").equals(dic);
	}

	@Override
	public boolean confirmarEncerramentoPulavel(String um) {
		String dic = dicionario[4];
		return um.replaceAll(" ", "").equals(dic);
	}
	
}
