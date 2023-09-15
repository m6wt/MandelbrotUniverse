package main;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


	
public class Universe extends JPanel {
	private final int width;
	private final int height;
	private final BufferedImage image;
	private JSlider zoomSlider;
	private int zoomLevel = 1;
	private BufferedImage previousImage;
	
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

	    double minReal = -2.0 * zoomFactor; // Adjust based on zoom factor
	    double maxReal = 1.0 * zoomFactor;
	    double minImag = -1.5 * zoomFactor;
	    double maxImag = 1.5 * zoomFactor;
	    
	    int maxIterations = 2000;
	    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    double scaleFactor = 255.0 / maxIterations;
	    
	    Graphics2D g2d = newImage.createGraphics();
	    g2d.setColor(Color.BLACK);
	    g2d.fillRect(0, 0, width, height);
	    g2d.dispose();

	    // Loop through the entire image
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            double real = map(x, 0, width, minReal, maxReal);
	            double imag = map(y, 0, height, minImag, maxImag);
	            int value = mandelbrot(real, imag, maxIterations);

	            int grayValue = (int) (value * scaleFactor);
	            Color color = new Color(grayValue, grayValue, grayValue);
	            
	            // Set the color in the newImage
	            newImage.setRGB(x, y, color.getRGB());
	        }
	    }

	    // Update the image with the newImage and repaint
	    image.setData(newImage.getData());
	    repaint();
	}


}
