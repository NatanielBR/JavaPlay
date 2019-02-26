package javaplay.central;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import javaplay.central.mascara.MascaraCentral;
import javaplay.outros.BancoComunicador;
import javaplay.outros.Resultado;
import javaplay.player.Player;

public class JLista extends JList<JBarra> {
	private JPopupMenu menu;
	private JMenuItem inicio, parou;
	private Player play;
	private JLista eu;
	private JFrame pai;
	private JBarra selecionado;
	public JLista(JFrame p) {
		super();
		pai = p;
		menu = new JPopupMenu();
		inicio = new JMenuItem("Abrir no inicio");
		parou = new JMenuItem("Abrir onde parou");
		eu = this;
		setCellRenderer(new ListCellRenderer<JBarra>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends JBarra> list, JBarra value,
					int index, boolean isSelected, boolean cellHasFocus) {
				if (isSelected) {
					value.setBackground(Color.LIGHT_GRAY);
				}else {
					value.setBackground(Color.WHITE);
				}
				return value;
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					if (!isSelectionEmpty()) {
						JProgressBar barra = getSelectedValue();
						menu.removeAll();
						if (barra.getValue() > 0) {
							menu.add(inicio);
							menu.add(parou);
						}else {
							menu.add(inicio);
						}
						selecionado = getSelectedValue();
						menu.show(eu,e.getX(),e.getY());
					}
					
				}else {
					menu.setVisible(false);
				}
			}
		});
		inicio.addActionListener((a)->{
			if (play != null && play.isVisible()) {
				return;
			}
			Consumer<Integer> ina = (b)->{
				selecionado.setValue(b);
				selecionado = null;
			};
			play = new Player(selecionado.getArquivo(),ina,0,this);
//			play.setModal(true);
//			play.setModalityType(ModalityType.APPLICATION_MODAL);
			play.setVisible(true);
		});
		parou.addActionListener((a)->{
			if (play != null && play.isVisible()) {
				return;
			}
			Consumer<Integer> ina = (b)->{
				selecionado.setValue(b);
				selecionado = null;
			};
			play = new Player(selecionado.getArquivo(),ina,selecionado.getValue(),this);
//			play.setModal(true);
//			play.setModalityType(ModalityType.APPLICATION_MODAL);
			play.setVisible(true);
		});
	}
	/**
	 * @return the pai
	 */
	public JFrame getPai() {
		return pai;
	}
	public void adicionarConteudo(List<File> arq) {
		List<JBarra> convert = new ArrayList<>(arq.size());
		arq.forEach((a)->convert.add(criarBarra(a)));
		trocarLista(convert);
	}
	private JBarra criarBarra(File info) {
		MascaraCentral masc = MascaraCentral.obterMascaraCompativel(info);
		JBarra barr = masc == null ? new JBarra(info):new JBarra(masc);
		barr.setMaximum(100);
		barr.setMinimum(0);
		barr.setStringPainted(true);
		Resultado res = BancoComunicador.instancia.obterResultado(info);
		barr.setValue(res == null?0:res.getUltimoTempo());
		barr.setToolTipText("Progresso: "+barr.getValue()+ "%");
		barr.setBorderPainted(false);
		return barr;
	}
	private void trocarLista(List<JBarra> novo) {
		DefaultListModel<JBarra> barras = new DefaultListModel<>();
		novo.forEach(barras::addElement);
		setModel(barras);
	}
}
