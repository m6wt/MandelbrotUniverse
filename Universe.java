package main;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


	
public class Universe extends JPanel {
	private final int width;
	private final int height;
	private final BufferedImage image;
	private JSlider zoomSlider;
	private int zoomLevel = 1;
	private BufferedImage previousImage;
	private int offsetX;
	private int offsetY;
	private int previousMouseX;
	private int previousMouseY;
	private boolean isDragging = false;
	
	
	public Universe(int width, int height) {
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		generateMandelbrot();
		
		zoomSlider = new JSlider(JSlider.VERTICAL, 1, 100, 1);
		zoomSlider.setMajorTickSpacing(10);
		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				zoomLevel = zoomSlider.getValue();
				generateMandelbrot();
			}
		});
		
		
		setLayout(new BorderLayout());
		add(zoomSlider, BorderLayout.EAST);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				previousMouseX = e.getX();
				previousMouseY = e.getY();
				isDragging = true;
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				isDragging = false;
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDragging) {
					int deltaX = e.getX() - previousMouseX;
					int deltaY = e.getY() - previousMouseY;
					
					offsetX +=  deltaX;
					offsetY += deltaY;
					
					generateMandelbrot();
					
					previousMouseX = e.getX();
					previousMouseY = e.getY();
;					
				}
			}
		});
		
		
					
	}
	
	public JSlider getZoomSlider() {
		return zoomSlider;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.drawImage(image, 0, 0, this);
		
	}
	
	private double map(double value, double start1, double stop1, double start2, double stop2 ) {
		return start2 + (stop2 - start2) * ((value - start1 ) / (stop1 - start1));
	}
	
	
	private Color mapColor(int value, int minVal, int maxVal) {
		
		double colorValue = map(value, minVal, maxVal, 0, 255);
		int colorValueInt = (int)colorValue;
		
		return new Color(colorValueInt, colorValueInt, colorValueInt);
		
		
	}
	
	public int mandelbrot(double real, double imag, int maxIterations) {
		double zReal = real;
		double zImag = imag;
		int n;
		
		for(n = 0; n < maxIterations; n++) {
			double zReal2 = zReal * zReal;
			double zImag2 = zImag * zImag;
			
			if (zReal2 + zImag2 > 4.0) {
				break;//this means the point is outside the set
				
			}
			//iteration formula
			zImag = 2.0 *zReal * zImag + imag;
			zReal = zReal2 - zImag2 + real;
			
		}
		
		return n;// return number of iterations
	}
	
	private void generateMandelbrot() {
	    double zoomFactor = 1.0 / zoomLevel;
	    double minReal = -2.0 * zoomFactor + offsetX * zoomFactor; // Adjust based on zoom factor
	    double maxReal = 1.0 * zoomFactor + offsetX * zoomFactor;
	    double minImag = -1.5 * zoomFactor + offsetY * zoomFactor;
	    double maxImag = 1.5 * zoomFactor + offsetY * zoomFactor;
	    
	    int maxIterations = 2000;
	    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	    // Loop through the entire image
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            double real = map(x, 0, width, minReal, maxReal);
	            double imag = map(y, 0, height, minImag, maxImag);
	            int value = mandelbrot(real, imag, maxIterations);

	            if (value < maxIterations) {
	            	Color color = getColorForIterations(value);
	            	newImage.setRGB(x, y, color.getRGB());
	            } else {
	            	newImage.setRGB(x, y, Color.WHITE.getRGB());
	            }
	        }
	    }

	    // Update the image with the newImage and repaint
	    image.setData(newImage.getData());
	    repaint();
	}

	private Color getColorForIterations(int iterations) {
		float hue = (float) iterations / 2000;
		return Color.getHSBColor(hue, 0.8f, 0.8f);
	}

}
