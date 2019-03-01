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
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javaplay.central.JBarra;
import javaplay.central.JLista;
import javaplay.central.LongJSlider;
import javaplay.outros.BancoComunicador;
import javaplay.outros.Propriedades;
import javaplay.player.mascara.MascaraPlayer;
import uk.co.caprica.vlcj.player.base.ChapterDescription;
import uk.co.caprica.vlcj.player.base.MarqueePosition;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;
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
	private EmbeddedMediaListPlayerComponent comp;
	private MascaraPlayer mascara;
	private JPanel esquerda;
	private Progresso progresso;
	private JPanel inferior;
	private JFrame janela;
	private JLista lista;
	private List<Path> arquivos;
	private Path atual;
	private int mult = 10;
	private int temp = 0;
	private boolean ismult = false;
	private boolean audioMudanca = false;
	private boolean isPularAbertura;
	private boolean isProximoArquivo;

	public Player(List<Path> arq, JLista lista) {
		setTitle("Player");
		comp = new EmbeddedMediaListPlayerComponent();
		inferior = new JPanel(new BorderLayout());
		progresso = new Progresso();
		esquerda = new JPanel();
		
		janela = this;
		arquivos = arq;
		play = comp.mediaPlayer();
		this.lista = lista;
		
		arq.forEach((a)->{
			comp.mediaListPlayer().list().items().add(a.toAbsolutePath().toString());
		});
		
		play.fullScreen().strategy(new AdaptiveFullScreenStrategy(janela));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		setarPropriedades(null);
		configurarEsquerda();
		configurarEventoDoPlayer();
		configurarEventoDaJanela(arq);
		
		inferior.add(progresso, BorderLayout.NORTH);

		add(comp, BorderLayout.CENTER);
		add(inferior, BorderLayout.SOUTH);
		add(esquerda,BorderLayout.EAST);
	}
	public void play(Path index, int tempo) {
		if (comp.isDisplayable()) {
			comp.mediaListPlayer().controls().play(arquivos.indexOf(index));
			comp.mediaPlayer().controls().skipTime(tempo);
		}else {
			atual = index;
			temp = tempo;
			System.out.println(tempo);
		}
//		comp.mediaListPlayer().controls().play(0);
		comp.mediaPlayer().controls().skipTime(tempo);
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
		play.marquee().setText(men);
		play.marquee().setTimeout(3000);
		play.marquee().setColour(Color.white);
	}

	public void alterarAudio(int val) {
		play.marquee().setText("Volume: " + val);
		play.marquee().setColour(Color.yellow);
		play.marquee().setTimeout(500);
	}
	private void configurarEsquerda() {
		esquerda.add(lista);
	}
	private void configurarEventoDaJanela(List<Path> arq) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent we) {
				super.windowOpened(we);
				lista.getPai().setVisible(false);
				System.out.println("dadasd");
				comp.mediaListPlayer().controls().play(arquivos.indexOf(atual));
				comp.mediaPlayer().controls().skipTime(temp);
			}

			@Override
			public void windowClosing(WindowEvent e) {
				if (play.status().isPlaying()) {
					play.controls().setPause(true);
				}
				long max = progresso.getBarra().getLongMaximum();
				long atu = progresso.getBarra().getLongValue();
				int porc = (int) ((100 * atu) / max);
//				new Thread(() -> BancoComunicador.instancia.inserirResultado(porc, arq)).start();
				lista.getPai().setVisible(true);
				lista.devolver();
				janela.dispose();
				super.windowClosing(e);
			}
		});
	}
	private void configurarEventoDoPlayer() {
		play.events().addMediaPlayerEventListener((new MediaPlayerEventAdapter() {
			@Override
			public void mediaPlayerReady(MediaPlayer mediaPlayer) {
				play.marquee().setSize(40);
				play.marquee().setPosition(MarqueePosition.TOP_RIGHT);
				play.marquee().setOpacity(0.8f);
				play.marquee().enable(true);
				Dimension video = play.video().videoDimension();
				janela.setSize(video.width, video.height + (inferior.getHeight() + inferior.getHeight() / 2));
			}
			@Override
			public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
				progresso.modificarFim(play.status().length());
				play.controls().setPosition((float) temp / 100);
			}

			@Override
			public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
				if (!audioMudanca)
					return;
				alterarAudio(play.audio().volume());
				audioMudanca = false;
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
				progresso.modificarInicio(play.status().time());
				progresso.modificarBarra(play.status().time());
			}

			@Override
			public void finished(MediaPlayer mediaPlayer) {
				janela.dispatchEvent(new WindowEvent(janela, WindowEvent.WINDOW_CLOSING));
			}
		}));
		comp.videoSurfaceComponent().addMouseWheelListener((a) -> {
			ismult = a.isControlDown();
			if (a.isShiftDown()) {
				int skip = -(a.getWheelRotation()*10000)*(ismult?mult:1);
				play.controls().skipTime(skip);
			}else {
				int vol = play.audio().volume() - (a.getWheelRotation() * (ismult ? mult : 1));
				vol = confirmarVolume(vol);
				audioMudanca = true;
				play.audio().setVolume(vol);
			}
			
		});
		comp.videoSurfaceComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ismult = e.isControlDown();
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					play.controls().pause();
					break;
				case KeyEvent.VK_F:
					esquerda.setVisible(play.fullScreen().isFullScreen());
					play.fullScreen().set(!play.fullScreen().isFullScreen());
					break;
				case KeyEvent.VK_LEFT:
					play.controls().skipTime(-10000);
					break;
				case KeyEvent.VK_RIGHT:
					play.controls().skipTime(10000);
					break;
				case KeyEvent.VK_UP:
					int vol = play.audio().volume() + (5 * (ismult ? mult : 1));
					vol = confirmarVolume(vol);
					audioMudanca = true;
					play.audio().setVolume(vol);
					break;
				case KeyEvent.VK_DOWN:
					vol = play.audio().volume() - (5 * (ismult ? mult : 1));
					vol = confirmarVolume(vol);
					audioMudanca = true;
					play.audio().setVolume(vol);
					break;
				case KeyEvent.VK_L:
					esquerda.setVisible(!esquerda.isVisible());
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				ismult = e.isControlDown();
			}
		});
	}
	private int confirmarVolume(int vol) {
		if (vol > 200) {
			vol = 200;
		} else if (vol < 0) {
			vol = 0;
		}
		return vol;
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
					play.controls().setTime(barra.getLongValue());
					play.controls().play();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					play.controls().pause();
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
			if (play.chapters().count() > 0) {
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
//					Player play = new Player(barra.getArquivo(), ina, barra.getValue(), lista);
					janela.dispatchEvent(new WindowEvent(janela, WindowEvent.WINDOW_CLOSING));
					lista.repaint();
//					play.setVisible(true);
				}).start();
			}
		}

		private void trabalharMascara(long val) {
			ChapterDescription desc = play.chapters().descriptions().stream().filter((a) -> val >= a.offset())
					.filter((c) -> val <= c.duration() + c.offset()).findFirst().orElse(null);
			if (desc == null) {
				return;
			} else if (!desc.name().equals(noms)) {
				noms = desc.name();
				if (isPularAbertura) {
					if (mascara.confirmarAberturaPulavel(noms)) {
						play.chapters().setChapter(play.chapters().chapter()+1);
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
				count = 1;
			} else {
				count = 0;
			}

			return count;
		}
	}
}
