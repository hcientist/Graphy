import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextMeasurer;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class BorderPanel extends JPanel
{

    public static final Font FONT = new Font("Arial Unicode MS", Font.PLAIN, 14);
    HashMap<AttributedCharacterIterator.Attribute, Object> map;
    Point2D.Double title_size;
    private int pad = 2;
    private String title;
    private Form form;
    
    
    public BorderPanel(String title, Form form)
    {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        
        this.form = form;
        this.title = title;
        map = new HashMap<AttributedCharacterIterator.Attribute, Object>();
        map.put(TextAttribute.FONT, FONT);
        AttributedString att = new AttributedString(title, map);
        TextMeasurer measurer = new TextMeasurer(att.getIterator(),
            new FontRenderContext(new AffineTransform(), true, false));
        FontMetrics fm = this.getFontMetrics(FONT);
                   
        title_size = new Point2D.Double(
            measurer.getAdvanceBetween( 0, title.length() ),
            fm.getHeight());
        int height = (int)title_size.y + 3 * pad;
        c.insets = new Insets(height, pad * 3, pad, pad * 3);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.fill = c.HORIZONTAL;
        c.anchor = c.PAGE_START;
        add(form, c);
    }
    
    public void paintComponent(Graphics g)
    {
        Color c = form.getBackground();
        float[] hsb = Color.RGBtoHSB( c.getRed(), c.getGreen(), c.getBlue(), null );
        hsb[1] = (float).1;
        Color lighter = Color.getHSBColor( hsb[0], hsb[1], hsb[2] );
        hsb[1] = 1;
        hsb[2] = hsb[2]/2;
        Color saturated = Color.getHSBColor( hsb[0], hsb[1], hsb[2] );
        
        setBackground(lighter);
        super.paintComponent( g );
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor( saturated );
        g2.setFont( FONT );
        g2.drawString( this.title, pad * 2 + 4, pad + (int)title_size.y );
        g2.drawLine( pad, pad + 3 * (int)title_size.y / 4, pad, getHeight() - 1 - pad );
        g2.drawLine( getWidth() - 1 - pad, pad + 3 * (int)title_size.y / 4, 
            getWidth() - 1 - pad, getHeight() - 1 - pad );
        g2.drawLine( pad, getHeight() - 1 - pad, 
            getWidth() - 1 - pad, getHeight() - 1 - pad );
        g2.drawLine( pad, pad + 3 * (int)title_size.y / 4, 
            pad + 4, pad + 3 * (int)title_size.y / 4 );
        g2.drawLine( pad *3 + 4 + (int)title_size.x, pad + 3 * (int)title_size.y / 4, 
            getWidth() - 1 - pad, pad + 3 * (int)title_size.y / 4 );
        
    }
    public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();
        int width = Math.max( d.width, 4 * pad + 8 + (int)title_size.x );
        return new Dimension(width, d.height);
    }
    
    private class Listener implements ActionListener, FocusListener {

        public void actionPerformed( ActionEvent arg0 )
        {
            Synchronizer.synchronize(form);
            getParent().repaint();
        }

        public void focusGained( FocusEvent arg0 )
        {
            //Intentionally left blank            
        }

        public void focusLost( FocusEvent arg0 )
        {
            Synchronizer.synchronize(form);
            getParent().repaint();
        }
        
    }
    
}
