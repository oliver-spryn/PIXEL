
package com.ledpixelart.pc.plugins.swing;

import com.ledpixelart.pc.PixelApp;
import ioio.lib.api.RgbLedMatrix;
import ioio.lib.api.exception.ConnectionLostException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * @author rmarquez
 */
public class ScrollingTextPanel extends SingleThreadedPixelPanel
{
    private static final long serialVersionUID = 1L;

    private JTextField textField;
    
    private JComboBox<String> fontFamilyChooser;
    
    final JPanel colorPanel;
    
    private JSlider scrollSpeedSlider;
    
    private JColorChooser colorChooser;
    
    private HashMap<String, Font> fonts;
    
    private int x;
    
    public ScrollingTextPanel(RgbLedMatrix.Matrix KIND)
    {
        super(KIND);
        
        fonts = new HashMap();
        
        x = 0;
	
	colorChooser = new JColorChooser();
        
        textField = new JTextField("Type Something Here");
        
        JPanel inputSubPanel = new JPanel( new BorderLayout() );
        JPanel inputPanel = new JPanel( new BorderLayout() );
        inputSubPanel.add(textField, BorderLayout.CENTER);        
        inputPanel.add(inputSubPanel, BorderLayout.NORTH);              
        
        String [] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        JPanel fontPanel = new JPanel( new BorderLayout() );
        fontFamilyChooser = new JComboBox(fontNames);
        fontPanel.add(fontFamilyChooser, BorderLayout.CENTER);
	
	colorPanel = new JPanel();
	colorPanel.setBackground(Color.GREEN);
	JButton colorButton = new JButton("choose");
	colorButton.addActionListener( new ActionListener() 
	{
	    public void actionPerformed(ActionEvent e) 
	    {
		Color color = colorChooser.showDialog(ScrollingTextPanel.this, "Select the text color.", Color.yellow);
		colorPanel.setBackground(color);
	    }
	});	
	JPanel colorComponents = new JPanel( new BorderLayout() );
	colorComponents.add(colorPanel, BorderLayout.CENTER);
	colorComponents.add(colorButton, BorderLayout.EAST);
        
        JPanel configurationPanel = new JPanel( new GridLayout(4, 1));
        configurationPanel.add(inputSubPanel);
        configurationPanel.add(fontPanel);
	configurationPanel.add(colorComponents);
	
	JPanel textPanel = new JPanel( new BorderLayout());
	textPanel.add(configurationPanel, BorderLayout.NORTH);
	textPanel.setPreferredSize( new Dimension(250, 1) );
	textPanel.setBorder( BorderFactory.createTitledBorder("Text") );	
	
	scrollSpeedSlider = new JSlider(200, 709);
	JPanel xPanel = new JPanel();
	xPanel.add(scrollSpeedSlider);
	xPanel.setBorder( BorderFactory.createTitledBorder("Scroll Speed") );
        
        setLayout(new BorderLayout());
        add(textPanel, BorderLayout.CENTER);
	add(xPanel, BorderLayout.SOUTH);
    }

    @Override
    public ActionListener getActionListener() 
    {
        ActionListener listener = new TextScroller();
        
        return listener;
    }
    
    private class TextScroller implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
	    int delay = scrollSpeedSlider.getValue();	
	    delay = 710 - delay;                            //al linke: added this so the higher slider value means faster scrolling
	    ScrollingTextPanel.this.timer.setDelay(delay);
	    
            int w = 64;
            int h = 64;
	    
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            
	    Color textColor = colorPanel.getBackground();
	    
            Graphics2D g2d = img.createGraphics();
            g2d.setPaint(textColor);
            
            String fontFamily = fontFamilyChooser.getSelectedItem().toString();
            
            Font font = fonts.get(fontFamily);
            if(font == null)
            {
                font = new Font(fontFamily, Font.PLAIN, 32);
                fonts.put(fontFamily, font);
            }            
            
            g2d.setFont(font);
            
            String message = textField.getText();
            
            FontMetrics fm = g2d.getFontMetrics();
            
            int y = fm.getHeight();            

            g2d.drawString(message, x, y);
            g2d.dispose();

            try 
            {              
                PixelApp.pixel.writeImagetoMatrix(img);
            } 
            catch (ConnectionLostException ex) 
            {
                Logger.getLogger(ScrollingTextPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
            int messageWidth = fm.stringWidth(message);            
            int resetX = 0 - messageWidth;
            
            if(x == resetX)
            {
                x = w;
            }
            else
            {
                x--;
            }
        }        
    }
    
}