import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.JTextField;


public abstract class Form extends JPanel implements Synchronizer.Synchable
{
    protected final Form reference = this;
    
    protected Form()
    {
        this.setOpaque( false );
    }
    public static String forOutput(double d)
    {
        return forOutput(d, 2);
    }
    public static String forOutput(double d, int p)
    {
        if(d == 0)
        {
            d = 0;
        }
        if(Math.abs(d) >= Math.pow( 10, p + 1))
        {
            return String.format("%." + p + "G", d)
                            .replaceAll( "(\\.[0-9]*?)0*$", "$1")
                            .replaceFirst( "(\\.[0-9]*?)0*E", "$1E")
                            .replaceFirst( "\\.0*$", "" )
                            .replaceFirst( "(\\.0*)?E(\\+?0*)?", "E" );
        }
        return String.format("%." + p + "f", d)
                        .replaceFirst( "(\\.\\d*?)0*$", "$1")
                        .replaceFirst( "(\\.\\d*?)0*E", "$1E")
                        .replaceFirst( "\\.0*$", "" )
                        .replaceFirst( "(\\.0*)?E(\\+?0*)?", "E" );
    }
    public Dimension getPreferredSize()
    {
        return new Dimension(150, super.getPreferredSize().height);
    }

}
