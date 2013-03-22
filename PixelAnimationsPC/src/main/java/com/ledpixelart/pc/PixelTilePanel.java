
package com.ledpixelart.pc;

import ioio.lib.api.RgbLedMatrix;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author rmarquez
 */
public abstract class PixelTilePanel extends JPanel implements ActionListener
{
    protected List<JButton> buttons;
    
//    protected byte[] BitmapBytes;
    
    protected boolean pixelFound;
    
//    protected InputStream BitmapInputStream;
    
//    protected short[] frame_;
    
    protected RgbLedMatrix matrix_;
    
    protected RgbLedMatrix.Matrix KIND;   
    
    public PixelTilePanel(RgbLedMatrix.Matrix KIND)
    {
	GridLayout experimentLayout = new GridLayout(0, 5);
	experimentLayout.setHgap(5);	
	experimentLayout.setVgap(5);
	
	setLayout(experimentLayout);
	
	buttons = new ArrayList();
	
//	BitmapBytes = new byte[KIND.width * KIND.height * 2]; //512 * 2 = 1024 or 1024 * 2 = 2048
	
//	frame_ = new short[KIND.width * KIND.height];
	
	pixelFound = false;
	
	this.KIND = KIND;
	
//	this.matrix_ = matrix;
//        System.out.println("matrix in PixelTilePanel: " + this.matrix_);
    }    
    
    /**
     * This method needs calling to place the icons on the panel.
     */
    public void populate()
    {
	List<String> filenames;
	try 
	{
	    filenames = imageNames();
	    for(String file : filenames)
	    {
		ImageIcon icon = getImageIcon(file);
		JButton button = new JButton(icon);
		int i = file.lastIndexOf(".");
		String command = file.substring(0, i);
		button.setActionCommand(command);
		button.addActionListener(this);
		add(button);
		buttons.add(button);
	    }
	} 
	catch (Exception ex) 
	{
	    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	}
	
    }
    
    protected abstract ImageIcon getImageIcon(String path);
    
    protected abstract List<String> imageNames() throws Exception;
    
    protected abstract String imagePath();
    
    protected void setPixelFound(boolean found)
    {
	pixelFound = found;
    }     
	    
}
