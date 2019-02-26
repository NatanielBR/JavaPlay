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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javaplay.central.LongJSlider;
import javaplay.outros.BancoComunicador;
import javaplay.outros.Propriedades;
import uk.co.caprica.vlcj.player.base.AudioApi;
import uk.co.caprica.vlcj.player.base.ChapterApi;
import uk.co.caprica.vlcj.player.base.ChapterDescription;
import uk.co.caprica.vlcj.player.base.ControlsApi;
import uk.co.caprica.vlcj.player.base.MarqueeApi;
import uk.co.caprica.vlcj.player.base.MarqueePosition;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.StatusApi;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Player do vlc
 * 
 * @author Nataniel
 */
public class Player extends JDialog {
	// double porc = ((play.getTime() * 100) / play.getLength());

	private EmbeddedMediaPlayer play;
	private ControlsApi controle;
	private StatusApi status;
	private AudioApi audio;
	private ChapterApi chapter;
	private MarqueeApi marca;
	private MascaraPlayer mascara;
	private JDialog janela;

	private int mult = 10;
	private boolean ismult = false;
	private boolean audioMudança = false;
	private final boolean isPularAbertura;
	private final boolean isProximoArquivo;

	public Player(File arq, Consumer<Integer> con) {
		this(arq, con, 0, MascaraPlayer.descobrirMascara(arq));
	}

	public Player(File arq, Consumer<Integer> con, int tim) {
		this(arq, con, tim == 100 ? 0 : tim, MascaraPlayer.descobrirMascara(arq));
	}

	public Player(File arq, Consumer<Integer> con, int temp, MascaraPlayer masc) {
		EmbeddedMediaPlayerComponent comp = new EmbeddedMediaListPlayerComponent();
		JPanel inferior = new JPanel(new BorderLayout());
		Progresso progresso = new Progresso();
		Controles contro = new Controles();

		if (masc != null) {
			isPularAbertura = Propriedades.instancia.isPularAbertura();
			isProximoArquivo = Propriedades.instancia.isProximoArquivo();
		} else {
			isPularAbertura = false;
			isProximoArquivo = false;
		}
		janela = this;
		play = comp.mediaPlayer();
		status = play.status();
		controle = play.controls();
		audio = play.audio();
		chapter = play.chapters();
		marca = play.marquee();
		mascara = masc;


		marca.setSize(40);
		marca.setColour(Color.WHITE);
		marca.setPosition(MarqueePosition.TOP_RIGHT);
		marca.setOpacity(0.8f);
		marca.enable(true);

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
				janela.setSize(video.width,video.height+(inferior.getHeight()+inferior.getHeight()/2));
			}

			@Override
			public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
//				contro.alterarVolume(audio.volume());
				if (!audioMudança)
					return;
				alterarAudio(audio.volume());
				audioMudança = false;
			}
			@Override
			public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
				System.out.println(newPausable);
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
				janela.dispose();
			}
		}));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent we) {
				super.windowOpened(we);
				play.media().play(arq.getAbsolutePath());
			}

			@Override
			public void windowClosing(WindowEvent e) {
				controle.setPause(true);
				long max = progresso.getBarra().getLongMaximum();
				long atu = progresso.getBarra().getLongValue();
				int porc = (int) ((100 * atu) / max);
				new Thread(() -> BancoComunicador.instancia.inserirResultado(porc, arq)).start();
				con.accept(porc);
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
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				ismult = e.isControlDown();
			}
		});
		inferior.add(contro, BorderLayout.SOUTH);
		inferior.add(progresso, BorderLayout.NORTH);

		add(comp, BorderLayout.CENTER);
		add(inferior, BorderLayout.SOUTH);
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
	 * Classe que possui os controle de Pause e Volume (este só é controlado pelo
	 * mouse)
	 * 
	 * @author natan
	 *
	 */
	private class Controles extends JPanel {
		public Controles() {
			setLayout(new BorderLayout());
			JButton bt1 = new JButton(Strings.getString("1"));
			bt1.addActionListener((a) -> {
				controle.pause();
			});
			add(bt1, BorderLayout.WEST);
		}
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
			ChapterDescription ds = null;
			for (ChapterDescription c : chapter.descriptions()) {
				if (val >= c.offset() && val <= c.duration() + c.offset()) {
					ds = c;
					break;
				}
			}
			if (!ds.name().equals(noms)) {
				noms = ds.name();

				if (isPularAbertura) {
					int id = mascara.getAberturaPulavel();
					String pular = mascara.getDicionario()[id];
					if (noms.replace(" ", "").equals(pular)) {
						chapter.setChapter(id + 1);
					}
				}
				if (isProximoArquivo) {
					int id = mascara.getEncerramentoPulavel();
					String pular = mascara.getDicionario()[id];
					if (noms.replace(" ", "").equals(pular)) {
						System.out.println(noms + "-" + pular);
						chapter.setChapter(id + 1);
					}
				}
				alterarMensagem(noms);
			}
			barra.setLongValue(val);

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
