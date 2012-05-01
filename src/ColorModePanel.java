import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;


public class ColorModePanel extends ModePanel
{
    
    public ColorModePanel()
    {
        super();
        
        
        Graph red = new Graph(1, 0, 0, Color.red);
        Graph green = new Graph(0, 1, 0, Color.green);
        Graph blue = new Graph(0, 0, 1, Color.blue);
        
        display.add( red );
        display.add( green );
        display.add(blue);
        
        formPanels.add( new BorderPanel("Red", new StandardForm(red)));

        formPanels.add( new BorderPanel("Green", new StandardForm(green)));

        formPanels.add( new BorderPanel("Blue", new StandardForm(blue)));
        
        this.refreshLayout();
    }
    
}
