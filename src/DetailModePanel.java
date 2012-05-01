import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class DetailModePanel extends ModePanel
{
    public DetailModePanel()
    {
        super();
        
        Graph g = new Graph(1, 0, 0, Color.MAGENTA);
        
        display.add( g );
        
        
        formPanels.add(new BorderPanel("Polynomial Form", new StandardForm(g)));

        formPanels.add( new BorderPanel("Factored Form", new RootForm(g)) );
        
        formPanels.add(new BorderPanel("Axis of Symmetry", new MetaForm(g)));

        String[] urls = {
            "http://dl.dropbox.com/u/24299910/Graphy/01_suspensionBridge.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/02_projectiles.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/03_satelliteDish.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/04_waterHose.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/05_bouncingBall.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/06_conicSection.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/07_parabolicMics.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/08_fireworks.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/09_flashlight.jpg",
            "http://dl.dropbox.com/u/24299910/Graphy/10_spinningLiquid.jpg"
        };
        
        URL[] backgrounds = new URL[urls.length];
        
        for(int i = 0; i < urls.length; i++)
        {
            try
            {
                backgrounds[i] = new URL(urls[i]);
            }
            catch(MalformedURLException e)
            {
                backgrounds[i] = null;
            }
        }
        
        
        formPanels.add(new BorderPanel("Real-world Example", new BackgroundForm(backgrounds, display)));
        
        refreshLayout();
    }
    
    

}
