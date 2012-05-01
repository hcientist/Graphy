import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class MetaForm extends Form
{
    Graph graph;
    JLabel substituted;
    JCheckBox check;
    
    
    public MetaForm(Graph graph)
    {
        this.graph = graph;
        check = new JCheckBox("Display: ");
        check.setHorizontalTextPosition( JCheckBox.LEFT );
        check.setOpaque( false );
        check.setFont( BorderPanel.FONT );
        check.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                Synchronizer.synchronize(reference);
            }
            
        });
        substituted = new JLabel();
        substituted.setFont( BorderPanel.FONT );
        
        this.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3, 1, 3, 1);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel l =  new JLabel("x = -b / ( 2 * a)");
        l.setFont( BorderPanel.FONT );
        this.add( l, c);
        
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        
        this.add( check, c );
        
        c.gridy = 2;
        c.anchor = GridBagConstraints.PAGE_START;
        
        this.add( substituted, c );
        
        this.graph.key.add( this );
        Synchronizer.synchronize( this.graph );
    }
    public Color getBackground()
    {
        if(graph == null)
        {
            return super.getBackground();
        }
        return graph.getColor();
    }
    
    @Override
    public void push( HashMap<Synchronizer.Key, Object[]> map )
    {
        if(!map.containsKey(graph.key))
        {
            return;
        }
        Object[] vals = map.get( graph.key );
        if(vals == null)
        {
            return;
        }
        Boolean new_axis = (Boolean)(vals[Graph.Index.AXIS.ordinal()]);
        if(new_axis != null)
        {
            check.setSelected(new_axis);
        }
        double[] coeff = (double[])(vals[Graph.Index.COEFFICIENTS.ordinal()]);
        if(coeff != null)
        {
            double num = - coeff[1] / (2 * coeff[0]);
            if(Double.isNaN( num ) || Double.isInfinite( num ))
            {
                substituted.setText( "Equation is linear" );
            }
            else
            {
                substituted.setText( "x = " + Form.forOutput( num ) );
            }
        }
                        
    }


    @Override
    public HashMap<Synchronizer.Key, Object[]> pull()
    {
        HashMap<Synchronizer.Key, Object[]> map = new HashMap<Synchronizer.Key, Object[]>();
        Object[] data = new Object[Graph.Index.values().length];
        
        data[Graph.Index.AXIS.ordinal()] = new Boolean(check.isSelected());
        
        map.put( graph.key, data );
        return map;
    }
    
    @Override
    public Synchronizer.Key[] getKeys()
    {
        Synchronizer.Key[] keys = {this.graph.key};
        return keys;
    }
}
