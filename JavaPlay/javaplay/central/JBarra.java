package javaplay.central;

import java.io.File;

import javax.swing.JProgressBar;

import javaplay.central.mascara.MascaraCentral;

public class JBarra extends JProgressBar {
	private final File arquivo;
	private final MascaraCentral mascara;
	public JBarra(File arq) {
		arquivo = arq;
		mascara = null;
		setString(arq.getName());
	}
	public JBarra(MascaraCentral mascara) {
		arquivo = mascara.getArquivo();
		this.mascara = mascara;
		setString(mascara.getFantasia());
	}
	/**
	 * @return the arquivo
	 */
	public File getArquivo() {
		return arquivo;
	}
	/**
	 * @return the mascara
	 */
	public MascaraCentral getMascara() {
		return mascara;
	}
	
	
}
