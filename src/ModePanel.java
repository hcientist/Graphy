import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;


public class ModePanel extends JPanel
{
    GraphPanel display;
    ArrayList<BorderPanel> formPanels = new ArrayList<BorderPanel>();
    protected ModePanel()
    {
        display = new GraphPanel(new Point2D.Double(50, 50));

        this.setLayout( new GridBagLayout());
        
    }
    public void refreshLayout()
    {
        this.removeAll();
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridheight = 1 + formPanels.size();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = .8;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque( false );
        
        this.add( controlPanel, c );
        
        controlPanel.setLayout(new GridBagLayout());
        
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        controlPanel.add( display, c );
        
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;
        
        Font zoomFont = new Font(BorderPanel.FONT.getName(), 
            BorderPanel.FONT.getStyle(), 
            (BorderPanel.FONT.getSize() * 3) / 2);
        
        JButton zoomIn = new JButton("+");
        zoomIn.setToolTipText( "zoom in" );
        zoomIn.setFont( zoomFont );
        zoomIn.setPreferredSize( new Dimension(30, 30) );
        zoomIn.setMargin( new Insets(0, 0, 0, 0 ));
        zoomIn.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e)
            {
                display.zoom(-1);
            }
        });
        JButton zoomOut = new JButton("\u2013");
        zoomOut.setToolTipText( "zoom out" );
        zoomOut.setFont( zoomFont);
        zoomOut.setPreferredSize( new Dimension(30, 30) );
        zoomOut.setMargin( new Insets(0, 0, 0, 0 ));
        zoomOut.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e)
            {
                display.zoom(1);
            }
        });
        JButton toOrigin = new JButton("origin");
        toOrigin.setToolTipText( "zoom to origin" );
        toOrigin.setFont( BorderPanel.FONT );
        toOrigin.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e)
            {
                display.restoreDefaults();
            }
        });
        
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        
        controlPanel.add( zoomIn, c );
        
        c.gridx++;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0;
        
        controlPanel.add( zoomOut, c );
        
        c.gridx++;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = .5;
        
        controlPanel.add( toOrigin, c );
        
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weighty = 0;
        c.weightx = .12;
        c.anchor = GridBagConstraints.PAGE_START;
        
        for(BorderPanel f : formPanels)
        {
            f.setMaximumSize( new Dimension(250, 600 ));
            this.add( f, c);
            c.gridy++;
        }
        
        
    }

}
