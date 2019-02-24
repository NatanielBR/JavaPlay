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
package iptv.player;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import iptv.player.thread.BancoComunicador;
import iptv.player.thread.InserirResultado;
import uk.co.caprica.vlcj.player.base.AudioApi;
import uk.co.caprica.vlcj.player.base.ChapterApi;
import uk.co.caprica.vlcj.player.base.ChapterDescription;
import uk.co.caprica.vlcj.player.base.ControlsApi;
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
public class Player extends JFrame {
	// double porc = ((play.getTime() * 100) / play.getLength());

	private EmbeddedMediaPlayer play;
	private ControlsApi controle;
	private StatusApi status;
	private AudioApi audio;
	private ChapterApi chapter;
	private int mult = 10;
	private boolean ismult = false;

	public Player(File arq, Consumer<Long[]> con, int temp) {
		EmbeddedMediaPlayerComponent comp = new EmbeddedMediaListPlayerComponent();
		JPanel inferior = new JPanel(new BorderLayout());
		Progresso progresso = new Progresso();
		Controles contro = new Controles();
		play = comp.mediaPlayer();
		status = play.status();
		controle = play.controls();
		audio = play.audio();
		chapter = play.chapters();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 470);
		comp.setLocation(0, 0);
		setLayout(new BorderLayout());

		play.events().addMediaPlayerEventListener((new MediaPlayerEventAdapter() {
			@Override
			public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
				progresso.modificarFim(status.length());
				controle.setPosition((float) temp / 100);
				progresso.barra.setCapit(chapter);
			}

			@Override
			public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
				contro.alterarVolume(audio.volume());
			}

			@Override
			public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
				progresso.modificarInicio(status.time());
				progresso.modificarBarra(status.time());
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
				comp.release();
				long max = progresso.getBarra().getLongMaximum();
				long atu = progresso.getBarra().getLongValue();
				int porc = (int) ((100 * atu) / max);
				new Thread(() -> BancoComunicador.instancia.inserirResultado(new InserirResultado(porc, arq))).start();
				con.accept(new Long[] { atu, max });
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

	public Player(File arq, Consumer<Long[]> con) {
		this(arq, con, 0);
	}

	/**
	 * Classe que possui os controle de Pause e Volume (este só é controlado pelo
	 * mouse)
	 * 
	 * @author natan
	 *
	 */
	private class Controles extends JPanel {

		private String[] strs = new String[] { "Pausar", "Volume: " };
		private JLabel lb;

		public Controles() {
			setLayout(new BorderLayout());
			JButton bt1 = new JButton(strs[0]);
			bt1.addActionListener((a) -> {
				controle.pause();
			});
			lb = new JLabel();
			add(lb, BorderLayout.EAST);
			add(bt1, BorderLayout.WEST);
		}

		/**
		 * Metodo para alterar o texto do volume
		 * 
		 * @param novo
		 */
		public void alterarVolume(int novo) {
			lb.setText(strs[1] + novo);
		}
	}

	/**
	 * Classe onde possui as JLabels inicio e fim (ini e fim) e a barra de progresso
	 * 
	 * @author natan
	 *
	 */
	private class Progresso extends JPanel {
		final JPopupMenu pop = new JPopupMenu();
		JMenuItem item = new JMenuItem();
		private JLabel ini, fim;
		private LongJSlider barra;
		private String noms = "";
		private int quand = -1;

		public LongJSlider getBarra() {
			return barra;
		}

		public Progresso() {
			ini = new JLabel();
			fim = new JLabel();
			barra = new LongJSlider();
			item.setText("");
			pop.add(item);
			pop.setDoubleBuffered(true);

			barra.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// para alterar o tempo no video
					controle.setTime(barra.getLongValue());
					controle.play();
				}

				@Override
				public void mousePressed(MouseEvent e) {
//					controle.pause();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					pop.setVisible(false);
				}
			});
			barra.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent me) {
					// limit the tooltip location relative to the slider
					List<ChapterDescription> dec = chapter.descriptions();
					Point p = me.getPoint();
					double percent = p.x / ((double) getWidth());
					long range = barra.getLongMaximum() - barra.getLongMinimum();
					double newVal = range * percent;
					long result = (long) (barra.getLongMinimum() + newVal);

					ChapterDescription ds = null;
					for (ChapterDescription c : dec) {
						if (result >= c.offset() && result <= c.duration() + c.offset()) {
							ds = c;
							break;
						}
					}
					if (ds == null) return;
					if (!ds.name().equals(item.getText())|| !pop.isVisible()) {
						item.setText(ds.name());
						pop.setLocation(me.getXOnScreen(), me.getYOnScreen() - 30);
						pop.setVisible(true);
						item.setArmed(false);
					}
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
			this.fim.setText(tempoDinamico(ms,-1));
			quand = tempodinamico(ms);
		}

		/**
		 * Metodo para alterar o texto do inicio do video
		 * 
		 * @param ms
		 */
		public void modificarInicio(long ms) {
			this.ini.setText(tempoDinamico(ms,quand));
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
				System.out.println(noms);
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
				time = String.format("%02d", secs);
				break;
			case 1:
				time = String.format("%02d:%02d", mins, secs);
				break;
			case 2:
				time = String.format("%02d:%02d:%02d", horas, mins, secs);
				break;
			default:
				if (horas > 0) {
					time = String.format("%02d:%02d:%02d", horas, mins, secs);
				} else if (mins > 0) {
					time = String.format("%02d:%02d", mins, secs);
				} else {
					time = String.format("%02d", secs);
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
				count  = 1;
			} else {
				count = 0;
			}

			return count;
		}
	}
}
