package javaplay.player;

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
	public int getAberturaPulavel() {
		return 1;
	}

	@Override
	public int getEncerramentoPulavel() {
		return 4 ;
	}
	
}
