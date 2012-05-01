import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;


public class BackgroundForm extends Form
{

    URL[] backgrounds;
    GraphPanel graphPanel;
    JCheckBox check;
    int bg_index = 0;
    public BackgroundForm(URL[] urls, GraphPanel graphPanel)
    {
        this.setBackground(Color.CYAN);
        this.graphPanel = graphPanel;
        this.graphPanel.key.add( this );
        this.backgrounds = urls;
        this.check = new JCheckBox("Background: ", true);
        this.check.setHorizontalTextPosition( JCheckBox.LEFT );
        check.setOpaque(false);
        check.setFont( BorderPanel.FONT );
        check.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(!check.isSelected())
                {
                    bg_index++;
                    bg_index %= backgrounds.length;
                }
                Synchronizer.synchronize(reference);
            }
        });
        this.add(this.check);
        Synchronizer.synchronize( this );
    }

    public HashMap<Synchronizer.Key, Object[]> pull()
    {
        HashMap<Synchronizer.Key, Object[]> map = new HashMap<Synchronizer.Key, Object[]>();
        Object[] data = new Object[GraphPanel.Index.values().length];
        if(check.isSelected())
        {
            data[GraphPanel.Index.BACKGROUNDIMAGE.ordinal()] = new ImageIcon(backgrounds[bg_index]);
        }
        else
        {
            ImageIcon i = new ImageIcon();
            data[GraphPanel.Index.BACKGROUNDIMAGE.ordinal()] = i;
        }  
        map.put( graphPanel.key, data );
        
        return map;
    }

    @Override
    public void push( HashMap<Synchronizer.Key, Object[]> map )
    {
        if(! map.containsKey( graphPanel.key ))
        {
            return;
        }
        
        Object[] vals = map.get( graphPanel.key );
        
        if(vals == null)
        {
            return;
        }
        ImageIcon new_backgroundImage = (ImageIcon)(vals[GraphPanel.Index.BACKGROUNDIMAGE.ordinal()]);
        if(new_backgroundImage != null)
        {
            check.setSelected(new_backgroundImage.getImage() != null);
        }
        
    }
    
    @Override
    public Synchronizer.Key[] getKeys()
    {
        Synchronizer.Key[] keys = {this.graphPanel.key};
        return keys;
    }

}
