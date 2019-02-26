/*******************************************************************************
 * Copyright (C) 2019 natan
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package javaplay.player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javaplay.central.JBarra;
import javaplay.central.JLista;
import javaplay.central.LongJSlider;
import javaplay.outros.BancoComunicador;
import javaplay.outros.Propriedades;
import javaplay.player.mascara.MascaraPlayer;
import uk.co.caprica.vlcj.player.base.AudioApi;
import uk.co.caprica.vlcj.player.base.ChapterApi;
import uk.co.caprica.vlcj.player.base.ChapterDescription;
import uk.co.caprica.vlcj.player.base.ControlsApi;
import uk.co.caprica.vlcj.player.base.LogoApi;
import uk.co.caprica.vlcj.player.base.MarqueeApi;
import uk.co.caprica.vlcj.player.base.MarqueePosition;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.StatusApi;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;

/**
 * Player do vlc
 * 
 * @author Nataniel
 */
public class Player extends JFrame {
	// double porc = ((play.getTime() * 100) / play.getLength());

	private EmbeddedMediaPlayer play;
	private ControlsApi controle;
	private StatusApi status;
	private AudioApi audio;
	private ChapterApi chapter;
	private MarqueeApi marca;
	private MascaraPlayer mascara;
	private JFrame janela;
	private JLista lista;
	private int mult = 10;
	private boolean ismult = false;
	private boolean audioMudança = false;
	private boolean isPularAbertura;
	private boolean isProximoArquivo;

	public Player(File arq, Consumer<Integer> con, int tim, JLista lista) {
		this(arq, con, tim == 100 ? 0 : tim, MascaraPlayer.descobrirMascara(arq), lista);
	}

	public Player(File arq, Consumer<Integer> con, int temp, MascaraPlayer masc, JLista lista) {
		setTitle("Player");
		EmbeddedMediaPlayerComponent comp = new EmbeddedMediaListPlayerComponent();
		JPanel inferior = new JPanel(new BorderLayout());
		Progresso progresso = new Progresso();
		setarPropriedades(masc);

		janela = this;
		this.lista = lista;
		play = comp.mediaPlayer();
		status = play.status();
		controle = play.controls();
		audio = play.audio();
		chapter = play.chapters();
		marca = play.marquee();
		play.logo();

		mascara = masc;

		marca.setSize(40);
		marca.setColour(Color.WHITE);
		marca.setPosition(MarqueePosition.TOP_RIGHT);
		marca.setOpacity(0.8f);
		marca.enable(true);

		play.fullScreen().strategy(new AdaptiveFullScreenStrategy(janela));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 470);
		setLayout(new BorderLayout());

		play.events().addMediaPlayerEventListener((new MediaPlayerEventAdapter() {
			@Override
			public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
				progresso.modificarFim(status.length());
				controle.setPosition((float) temp / 100);
				progresso.barra.setCapit(chapter);
				Dimension video = play.video().videoDimension();
				janela.setSize(video.width, video.height + (inferior.getHeight() + inferior.getHeight() / 2));
			}

			@Override
			public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
				if (!audioMudança)
					return;
				alterarAudio(audio.volume());
				audioMudança = false;
			}

			@Override
			public void paused(MediaPlayer mediaPlayer) {
				alterarMensagem("Pausado");
			}
			@Override
			public void playing(MediaPlayer mediaPlayer) {
				alterarMensagem("Rodando");
			}
			@Override
			public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
				progresso.modificarInicio(status.time());
				progresso.modificarBarra(status.time());
			}
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				int porc = 100;
				new Thread(() -> BancoComunicador.instancia.inserirResultado(porc, arq)).start();
				con.accept(porc);
				lista.getPai().setVisible(true);
				janela.dispose();
			}
		}));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent we) {
				super.windowOpened(we);
				play.media().prepare(arq.getAbsolutePath());
				lista.getPai().setVisible(false);
				controle.play();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				controle.setPause(true);
				long max = progresso.getBarra().getLongMaximum();
				long atu = progresso.getBarra().getLongValue();
				int porc = (int) ((100 * atu) / max);
				new Thread(() -> BancoComunicador.instancia.inserirResultado(porc, arq)).start();
				con.accept(porc);
				lista.getPai().setVisible(true);
				comp.release();
				super.windowClosing(e);
			}
		});

		comp.videoSurfaceComponent().addMouseWheelListener((a) -> {
			int vol = audio.volume() - (a.getWheelRotation() * (ismult ? mult : 1));
			if (vol > 200) {
				vol = 200;
			} else if (vol < 0) {
				vol = 0;
			}
			audioMudança = true;
			audio.setVolume(vol);
		});

		comp.videoSurfaceComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ismult = e.isControlDown();
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					controle.pause();
					break;
				case KeyEvent.VK_F:
					play.fullScreen().set(!play.fullScreen().isFullScreen());
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				ismult = e.isControlDown();
			}
		});
		inferior.add(progresso, BorderLayout.NORTH);

		add(comp, BorderLayout.CENTER);
		add(inferior, BorderLayout.SOUTH);
	}

	private void setarPropriedades(MascaraPlayer masc) {
		if (masc != null) {
			isPularAbertura = Propriedades.instancia.isPularAbertura();
			isProximoArquivo = Propriedades.instancia.isProximoArquivo();
		} else {
			isPularAbertura = false;
			isProximoArquivo = false;
		}
	}

	public void alterarMensagem(String men) {
		marca.setText(men);
		marca.setTimeout(3000);
		marca.setColour(Color.white);
	}

	public void alterarAudio(int val) {
		marca.setText(Strings.getString("0") + val);
		marca.setColour(Color.yellow);
		marca.setTimeout(500);
	}

	/**
	 * Classe onde possui as JLabels inicio e fim (ini e fim) e a barra de progresso
	 * 
	 * @author natan
	 *
	 */
	private class Progresso extends JPanel {
		private JLabel ini, fim;
		private LongJSlider barra;
		private String noms = null;
		private int quand = -1;

		public LongJSlider getBarra() {
			return barra;
		}

		public Progresso() {
			ini = new JLabel();
			fim = new JLabel();
			barra = new LongJSlider();

			barra.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					controle.setTime(barra.getLongValue());
					controle.play();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					controle.pause();
				}
			});
			setLayout(new BorderLayout());
			add(ini, BorderLayout.WEST);
			add(barra, BorderLayout.CENTER);
			add(fim, BorderLayout.EAST);
		}

		/**
		 * Metodo para alterar o texto do fim do video
		 * 
		 * @param ms
		 */
		public void modificarFim(long ms) {
			barra.setLongMaximum(ms);
			this.fim.setText(tempoDinamico(ms, -1));
			quand = tempodinamico(ms);
		}

		/**
		 * Metodo para alterar o texto do inicio do video
		 * 
		 * @param ms
		 */
		public void modificarInicio(long ms) {
			this.ini.setText(tempoDinamico(ms, quand));
		}

		/**
		 * Metodo para alterar o progresso do video
		 * 
		 * @param val
		 */
		public void modificarBarra(long val) {
			barra.setLongValue(val);
			if (chapter.count() > 0) {
				trabalharMascara(val);
			}
		}

		private void proximoArquivo() {
			int ind = lista.getSelectedIndex();
			if ((ind + 1) < lista.getModel().getSize()) {
				new Thread(() -> {

					JBarra barra = lista.getModel().getElementAt(ind + 1);
					Consumer<Integer> ina = (b) -> {
						barra.setValue(b);
					};
					lista.setSelectedIndex(ind + 1);
					Player play = new Player(barra.getArquivo(), ina, barra.getValue(), lista);
					janela.dispatchEvent(new WindowEvent(janela, WindowEvent.WINDOW_CLOSING));
					lista.repaint();
					play.setVisible(true);
				}).start();
			}
		}

		private void trabalharMascara(long val) {
			ChapterDescription desc = chapter.descriptions().stream().filter((a) -> val >= a.offset())
					.filter((c) -> val <= c.duration() + c.offset()).findFirst().orElse(null);
			if (desc == null) {
				return;
			} else if (!desc.name().equals(noms)) {
				noms = desc.name();
				if (isPularAbertura) {
					if (mascara.confirmarAberturaPulavel(noms)) {
						chapter.setChapter(chapter.chapter() + 1);
					}
				}
				if (isProximoArquivo) {
					if (mascara.confirmarEncerramentoPulavel(noms)) {
						proximoArquivo();
					}
				}
				alterarMensagem(noms);
			}
		}

		/**
		 * Metodo que converte os milisegundos para hora:minuto:segundo
		 * 
		 * @param ms
		 * @return
		 */
		private String tempoDinamico(long ms, int quand) {
			int secs, mins, horas;
			horas = (int) (ms / 3600000);
			mins = (int) (ms / 60000);
			secs = (int) ((ms / 1000) % 60);
			String time;
			switch (quand) {
			case 0:
				time = String.format(Strings.getString("6"), secs);
				break;
			case 1:
				time = String.format(Strings.getString("7"), mins, secs);
				break;
			case 2:
				time = String.format(Strings.getString("8"), horas, mins, secs);
				break;
			default:
				if (horas > 0) {
					time = String.format(Strings.getString("9"), horas, mins, secs);
				} else if (mins > 0) {
					time = String.format(Strings.getString("10"), mins, secs);
				} else {
					time = String.format(Strings.getString("11"), secs);
				}
				break;
			}

			return time;
		}

		private int tempodinamico(long ms) {
			int secs, mins, horas;
			horas = (int) (ms / 3600000);
			mins = (int) (ms / 60000);
			secs = (int) ((ms / 1000) % 60);
			int count;
			if (horas > 0) {
				count = 2;
			} else if (mins > 0) {
				count = 1;
			} else {
				count = 0;
			}

			return count;
		}
	}
}
