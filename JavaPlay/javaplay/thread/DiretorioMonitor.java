package javaplay.thread;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javaplay.outros.Propriedades;
import javaplay.outros.Utilitario;
import uk.co.caprica.vlcj.filefilters.VideoFileFilter;

public class DiretorioMonitor implements Runnable {
	private File diretorio;

	/**
	 * @param diretorio the diretorio to set
	 */
	public void setDiretorio() {
		diretorio = new File(Utilitario.escolherDiretorio("Escolha o local da biblioteca"));
		Propriedades prop = Propriedades.instancia;
		prop.setDir(diretorio.getAbsolutePath());
	}

	private final AtomicBoolean cancelar;
	private static List<Path> cache;
	private Consumer<List<Path>> novidade;

	public DiretorioMonitor(AtomicBoolean atoc, Consumer<List<Path>> novid) {
		Propriedades prop = Propriedades.instancia;
		String dir = prop.getDir();
		if (dir == null) {
			setDiretorio();

		} else {
			diretorio = new File(dir);
		}
		novidade = novid;
		cancelar = atoc;

		new Thread(this).start();
	}

	@Override
	public void run() {
		VideoFileFilter filtro = new VideoFileFilter();
		List<Path> flist = null;
		try {
			flist = Files.list(diretorio.toPath()).filter((a) -> filtro.accept(a.toFile()))
					.collect(Collectors.toList());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		Files.list(diretorio.toPath());
//		Collections.sort(flist);
		while (!cancelar.get()) {
			if (cache == null && flist != null) {
				novidade.accept(flist);
				cache = flist;
			} else {
				if (cache.size() != flist.size()) {
					novidade.accept(flist);
					cache = flist;
				} else {
					// basicamente ele olha se no cache todos existem, se sim, ignora se nao usa
					// da nova lista
					for (Path p : cache) {
						if (!Files.exists(p, LinkOption.NOFOLLOW_LINKS)) {
							novidade.accept(flist);
							cache = flist;
							break;
						}
					}
				}
				
			}
			try {
				Thread.sleep(2000);
				flist = Files.list(diretorio.toPath()).filter((a) -> filtro.accept(a.toFile()))
						.collect(Collectors.toList());
				;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
