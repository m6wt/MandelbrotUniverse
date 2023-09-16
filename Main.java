package main;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		int resolutionWidth = 800;
		int resolutionHeight = 600;
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setSize(resolutionWidth, resolutionHeight);
		
		Universe universe = new Universe(resolutionWidth, resolutionHeight);
		JSlider zoomSlider = universe.getZoomSlider();
		
		window.add(universe);
		window.setVisible(true);
		

	}

}
