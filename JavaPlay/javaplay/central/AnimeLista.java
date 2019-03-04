package javaplay.central;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import com.sun.jna.NativeLibrary;

import javaplay.central.mascara.MascaraCentral;
import javaplay.outros.BancoComunicador;
import javaplay.outros.Propriedades;
import javaplay.thread.DiretorioMonitor;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

public class AnimeLista {

	private JFrame frame;
	private JTabbedPane Coluna;
	private AtomicBoolean encerrar;
	private Consumer<List<Path>> novidade;
	private DiretorioMonitor monitor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Propriedades();
		new NativeDiscovery().discover();
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		for(Path di : FileSystems.getDefault().getRootDirectories()) {
			String dir = di.toString();
			for (String posi : new String[] {"Program Files\\","Program Files (x86)\\"}) {
				NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), dir+posi+"VideoLAN\\VLC");
			}
		}
		try {
			NativeLibrary.getInstance(RuntimeUtil.getLibVlcLibraryName());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		new BancoComunicador();
		AnimeLista window = new AnimeLista();
		window.frame.setVisible(true);

	}

	public void dividirPorMascara(List<Path> arq) {
		HashMap<String, List<Path>> listas = new HashMap<>();
		Coluna.removeAll();
		for (Path f : arq) {
			MascaraCentral masc = MascaraCentral.obterMascaraCompativel(f);
			if (masc == null)
				continue;
			if (listas.get(masc.getFantasiaSemEp()) == null) {
				List<Path> arqs = new ArrayList<>();
				arqs.add(f);
				listas.put(masc.getFantasiaSemEp(), arqs);
			} else {
				listas.get(masc.getFantasiaSemEp()).add(f);
			}
		}
		listas.keySet().forEach((mascara) -> {
			JLista lis = new JLista(frame);
			lis.adicionarConteudo(listas.get(mascara));
			Coluna.addTab(mascara, lis);
		});

	}

	/**
	 * Create the application.
	 */
	public AnimeLista() {
		initialize();
		encerrar = new AtomicBoolean();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				encerrar.set(true);
				System.exit(0);
			}
		});
		novidade = (a) -> {
			dividirPorMascara(a);
			frame.repaint();
			frame.pack();
		};
		monitor = new DiretorioMonitor(encerrar, novidade);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("JavaPlay");
		frame.setBounds(100, 100, 760, 510);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Coluna = new JTabbedPane(JTabbedPane.LEFT);
		frame.getContentPane().add(Coluna, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnJavaplay = new JMenu("JavaPlay");
		menuBar.add(mnJavaplay);

		JMenuItem mntmNewMenuItem = new JMenuItem("Mudar Diretorio");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Propriedades.instancia.removerDir();
				monitor.setDiretorio();
			}
		});
		mnJavaplay.add(mntmNewMenuItem);

		JMenuItem Sair = new JMenuItem("Sair");
		Sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		mnJavaplay.add(Sair);

		JMenu mnNewMenu = new JMenu("Player");
		menuBar.add(mnNewMenu);

		JCheckBoxMenuItem ProximoBoolean = new JCheckBoxMenuItem("Proximo Arquivo");
		ProximoBoolean.setSelected(Propriedades.instancia.isProximoArquivo());
		ProximoBoolean.addActionListener((a) -> {
			Propriedades.instancia.setProximoArquivo(ProximoBoolean.isSelected());
		});
		mnNewMenu.add(ProximoBoolean);

		JCheckBoxMenuItem PularBoolean = new JCheckBoxMenuItem("Pular Abertura");
		PularBoolean.setSelected(Propriedades.instancia.isPularAbertura());
		PularBoolean.addActionListener((a) -> {
			Propriedades.instancia.setPularAbertura(PularBoolean.isSelected());
		});
		mnNewMenu.add(PularBoolean);

		JMenu mnSobre = new JMenu("Ajuda");
		menuBar.add(mnSobre);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Versão");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Sobre(frame);
			}
		});
		mnSobre.add(mntmNewMenuItem_1);
	}

}
