package javaplay.outros;

import java.io.File;

public class Regex {
	private final String REGEX;
	private final File[] ARQUIVOS;
	public Regex(String regex, String ... arqs) {
		REGEX = regex;
		ARQUIVOS = new File[arqs.length];
		for (int i = 0; i < arqs.length; i++) {
			String s = arqs[i];
			if (s.length()>0) {
				ARQUIVOS[i] = new File(s);
			}
		}
	}
	/**
	 * @return the rEGEX
	 */
	public String getRegex() {
		return REGEX;
	}
	/**
	 * @return the aRQUIVOS
	 */
	public File[] getArquivos() {
		return ARQUIVOS;
	}

}
