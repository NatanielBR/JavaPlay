
package javaplay.central;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * An extension of JSlider that uses Double for its values.
 * 
 * Changed (Alterado)
 * 
 * @since Trick 10
 */
public class LongJSlider extends JSlider {

	// ========================================
	// Public data
	// ========================================
	// public final static Long Long_FACTOR = 100.0;

	// ========================================
	// Protected data
	// ========================================

	// ========================================
	// Private Data
	// ========================================
	private static final long serialVersionUID = 8104332457463381081L;

	/** This border show the current value of the slider as its title. */
	private TitledBorder border = new TitledBorder(new EtchedBorder());

	// ========================================
	// Constructors
	// ========================================
	/**
	 * Default constructor - initializes with 0.0,100.0,50.0
	 */
	public LongJSlider() {
		super();
		setLongMinimum(0);
		setLongMaximum(100);
		setLongValue(50);

	}

	/**
	 * Constructor with specified minimum, maximum and init value.
	 * 
	 * @param min The minimum Long value for the slider.
	 * @param max The maximum Long value for the slider.
	 * @param val The init value for the slider.
	 */
	public LongJSlider(Long min, Long max, Long val) {
		super();
		setUI(new DoubleJSliderUI(this));
		setBorder(border);
		setLongMinimum(min);
		setLongMaximum(max);
		setLongValue(val);
		setLabel("Current Value: " + val);
	}

	public LongJSlider(Long min, Long max, Long val, Long factor) {
		super();
		setUI(new DoubleJSliderUI(this));
		setBorder(border);
		setLongMinimum(min);
		setLongMaximum(max);
		setLongValue(val);
		setLabel("Current Value: " + val);
	}

	/**
	 * Sets the title for the slider.
	 * 
	 * @param s The specified title for the slider.
	 */
	public void setLabel(String s) {
		border.setTitle(s);
	}

	// ========================================
	// Set/Get methods
	// ========================================
	/**
	 * Returns the maximum value for the slider in Long.
	 * 
	 * @return The maximum value for the slider in Long.
	 */
	public Long getLongMaximum() {
		return (long) (getMaximum());
	}

	/**
	 * Returns the minimum value for the slider in Long.
	 * 
	 * @return The minimum value for the slider in Long.
	 */
	public Long getLongMinimum() {
		return (long) (getMinimum());
	}

	/**
	 * Returns the current value for the slider in Long.
	 * 
	 * @return The current value of the slider in Long.
	 */
	public Long getLongValue() {
		return (long) (getValue());
	}

	/**
	 * Sets maximum value for the slider in Long.
	 * 
	 * @param max The value which needs to be set as maximum value for the slider.
	 */
	public void setLongMaximum(long max) {
		setMaximum((int) (max));
	}

	/**
	 * Sets minimum value for the slider in Long.
	 * 
	 * @param min The value which needs to be set as minimum value for the slider.
	 */
	public void setLongMinimum(long min) {
		setMinimum((int) (min));
	}

	/**
	 * Sets current value for the slider in Long.
	 * 
	 * @param val The value which needs to be set as current value for the slider.
	 */
	public void setLongValue(long val) {
		setValue((int) (val));
//		setToolTipText(Long.toString(val));
	}

	// ========================================
	// Inner classes
	// ========================================
	/**
	 * The class for customized slider UI.
	 */
	class DoubleJSliderUI extends BasicSliderUI implements MouseMotionListener {
		final JPopupMenu pop = new JPopupMenu();
		JMenuItem item = new JMenuItem();

		LongJSlider slider;

		/**
		 * Constructor with specifed slider.
		 * 
		 * @param slider The specified {@link LongJSlider}.
		 */
		public DoubleJSliderUI(LongJSlider slider) {
			super(slider);
//			slider.addMouseMotionListener(this);
			this.slider = slider;
			pop.add(item);
			pop.setDoubleBuffered(true);
		}

		/**
		 * Shows the tip text while mouse moving.
		 * 
		 * @param me The mouse event of slider moving.
		 */
		public void showToolTip(MouseEvent me) {
			item.setText("dasdasdas");

			// limit the tooltip location relative to the slider
			Rectangle b = me.getComponent().getBounds();
			int x = me.getX();
			x = (x > (b.x + b.width / 2) ? (b.x + b.width / 2) : (x < (b.x - b.width / 2) ? (b.x - b.width / 2) : x));
			System.out.println(x);
			pop.show(me.getComponent(), x - 5, -5);

			item.setArmed(false);
		}

		/**
		 * Required by {@link MouseMotionListener}.
		 */
		public void mouseMoved(MouseEvent me) {
			showToolTip(me);
		}

		/**
		 * Required by {@link MouseMotionListener}.
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
		}
	}

}
