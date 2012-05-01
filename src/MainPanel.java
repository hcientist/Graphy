import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class MainPanel extends JPanel
{   
    public MainPanel()
    {
        
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setLayout( new BorderLayout() );
        this.add( tabs , BorderLayout.CENTER);
        
        DetailModePanel detail = new DetailModePanel();
        
        tabs.add( detail, "Detail" );
        
        ColorModePanel color = new ColorModePanel();
        
        tabs.add(color, "MultiGraph" );
    }
}
