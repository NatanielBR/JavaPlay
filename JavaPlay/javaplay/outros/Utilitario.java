package javaplay.outros;

import javax.swing.JFileChooser;

public class Utilitario {
	public static String escolherDiretorio(String titulo) {
		String dir = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(titulo);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			dir = chooser.getSelectedFile().getAbsolutePath();
		} else {
			System.exit(0);
		}
		return dir;
	}
}
