package javaplay.central;

import java.nio.file.Path;

import javax.swing.JProgressBar;

import javaplay.central.mascara.MascaraCentral;

public class JBarra extends JProgressBar {
	private final Path arquivo;
	private final MascaraCentral mascara;
	public JBarra(Path arq) {
		arquivo = arq;
		mascara = null;
		setString(arq.getFileName().toString());
	}
	public JBarra(MascaraCentral mascara) {
		arquivo = mascara.getArquivo();
		this.mascara = mascara;
		setString(mascara.getFantasia());
	}
	/**
	 * @return the arquivo
	 */
	public Path getArquivo() {
		return arquivo;
	}
	/**
	 * @return the mascara
	 */
	public MascaraCentral getMascara() {
		return mascara;
	}
	
	
}
