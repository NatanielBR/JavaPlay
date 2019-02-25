package javaplay;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;

import javaplay.outros.ObterResultado;
import javaplay.outros.Propriedades;
import javaplay.outros.Resultado;
import javaplay.player.Player;
import javaplay.thread.BancoComunicador;
import javaplay.thread.DiretorioMonitor;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class AnimeLista {

	private JFrame frame;
	private JTabbedPane Coluna;
	
	private AtomicBoolean encerrar;
	private JPanel ListaDesconhecida;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		AnimeLista window = new AnimeLista();
		new BancoComunicador();
		new Propriedades();
		Consumer<Collection<File>> novidade = (a)->{
			window.ListaDesconhecida.removeAll();
			a.forEach((info)->{
				JProgressBar barr = new JProgressBar(0, 100);
				barr.setString(info.getName());
				barr.setStringPainted(true);
				Resultado res = BancoComunicador.instancia.obterResultado(new ObterResultado(info));
				barr.setValue(res == null?0:res.getUltimoTempo());
				barr.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
//						if (play != null && play.isVisible()) {
//							return;
//						}
//						Resultado res = BancoComunicador.instancia.obterResultado(new ObterResultado(info));
//						Consumer<Integer> ina = (a)->{
//							barr.setValue(a);
//						};
//						play = barr.getValue()>0?new Player(info,ina,res.getUltimoTempo()):new Player(info,ina);
//						play.setModal(true);
//						play.setModalityType(ModalityType.DOCUMENT_MODAL);
//						play.setVisible(true);
					}

				});
				barr.setBorderPainted(false);
				window.ListaDesconhecida.add(barr);
			});
		};
		DiretorioMonitor monitor = new DiretorioMonitor(window.encerrar,novidade);
		window.frame.setVisible(true);
		
	}

	/**
	 * Create the application.
	 */
	public AnimeLista() {
		initialize();
		encerrar = new AtomicBoolean();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 760, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Coluna = new JTabbedPane(JTabbedPane.LEFT);
		frame.getContentPane().add(Coluna, BorderLayout.CENTER);

		JPanel PainelDesconhecido = new JPanel();
		Coluna.addTab("Desconhecido", null, PainelDesconhecido, null);
		PainelDesconhecido.setLayout(new BorderLayout(0, 0));
		
		ListaDesconhecida = new JPanel();
		PainelDesconhecido.add(ListaDesconhecida);
		ListaDesconhecida.setLayout(new GridLayout(0, 1, 0, 10));
		Coluna.setEnabledAt(0, true);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.EAST);

		JLabel lblNewLabel = new JLabel("New label");
		panel_1.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("New label");
		panel_1.add(lblNewLabel_1);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnJavaplay = new JMenu("JavaPlay");
		menuBar.add(mnJavaplay);

		JMenu mnNewMenu = new JMenu("Player");
		menuBar.add(mnNewMenu);

		JMenu mnSobre = new JMenu("Sobre");
		menuBar.add(mnSobre);
	}

}
