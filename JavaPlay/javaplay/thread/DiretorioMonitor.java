package javaplay.thread;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;

import javaplay.outros.Propriedades;
import uk.co.caprica.vlcj.filefilters.VideoFileFilter;

public class DiretorioMonitor implements Runnable {
	private final File diretorio;
	private final AtomicBoolean cancelar;
	private List<File> cache;
	private Consumer<List<File>> novidade;

	public DiretorioMonitor(AtomicBoolean atoc, Consumer<List<File>> novid) {
		Propriedades prop = Propriedades.instancia;
		String dir = prop.getDir();
		if (dir == null) {
			diretorio = new File(escolherDiretorio());

		} else {
			diretorio = new File(dir);
		}
		novidade = novid;
		cancelar = atoc;
		
		new Thread(this).start();
	}

	@Override
	public void run() {
		List<File> flist = Arrays.asList(FileUtils.listFiles(diretorio, new VideoFileFilter().getExtensions(), true).toArray(FileUtils.EMPTY_FILE_ARRAY));
		Collections.sort(flist);
		while (!cancelar.get()) {
			if (cache ==  null) {
				novidade.accept(flist);
				cache = flist;
			}else {
				if (cache.size()!=flist.size()) {
					novidade.accept(flist);
					cache = flist;
				}else {
					for (File f : cache) {
						try {
							if (!FileUtils.directoryContains(diretorio, f)) {
								novidade.accept(flist);
								cache = flist;
								break;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			flist = Arrays.asList(FileUtils.listFiles(diretorio, new VideoFileFilter().getExtensions(), true).toArray(FileUtils.EMPTY_FILE_ARRAY));;
			Collections.sort(flist);
		}
	}

	private String escolherDiretorio() {
		String dir = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Escolha o diretorio");
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
