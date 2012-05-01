import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JTextField;


public class RootForm extends Form
{
    
    Graph graph;
    JLabel substituted;
    JTextField[] texts;
    
    public static String substitute(double[] root_coeff, double[] coeff)
    {
        String scalar = "";
        String root1 = "(x ";
        String root2 = "(x ";
        if(coeff[0] == 0)
        {
            if(coeff[1] == 0)
            {
                return "y = " + Form.forOutput( coeff[2]);
            }
            if(coeff[2] == 0)
            {
                return "y = " + Form.forOutput( coeff[1]) +
                            "x";
            }
            return "y = " + Form.forOutput( coeff[1]) +
                            "(x " + (coeff[2] / coeff[1] < 0 ? "- " : "+ ") +
                            Form.forOutput( Math.abs(coeff[2] / coeff[1])) +
                            ")";
            
        }
        if(root_coeff[0] != 1)
        {
            scalar = Form.forOutput( coeff[0]);
        }
        
        if(root_coeff[1] != 0)
        {
            root1 = root1 + (root_coeff[1] < 0 ? "+ " : "- ") + 
                            Form.forOutput( Math.abs( root_coeff[1]) );
        }
        if(root_coeff[2] != 0)
        {
            root1 = root1 + (root_coeff[2] < 0 ? "+ " : "- ") + 
                            Form.forOutput( Math.abs( root_coeff[2] ) ) + "i";
        }
        if(root1.equals( "(x " ))
        {
            root1 = "x";
        }
        else
        {
            root1 = root1 + ")";
        }
        
        if(root_coeff[3] != 0)
        {
            root2 = root2 + (root_coeff[3] < 0 ? "+ " : "- ") + 
                            Form.forOutput(Math.abs( root_coeff[3] ) );
        }
        if(root_coeff[4] != 0)
        {
            root2 = root2 + (root_coeff[4] < 0 ? "+ " : "- ") + 
                            Form.forOutput( Math.abs(  root_coeff[4] ) ) + "i";
        }
        if(root2.equals( "(x " ))
        {
            root2 = "x";
        }
        else
        {
            root2 = root2 + ")";
        }
        if(root1.equals( root2 ))
        {
            return  "y = " + scalar + root1 + "\u00b2";
        }
            
        return "y = " + scalar + root1 + root2;
    }
    
    public RootForm(Graph graph)
    {
        this.graph = graph;
        this.substituted = new JLabel();
        this.texts = new JTextField[5];
        substituted.setFont( BorderPanel.FONT );
        JLabel equationLabel = new JLabel("y = k(x - r\u2081)(x - r\u2082)");
        equationLabel.setFont( BorderPanel.FONT );
        JLabel[] labels = new JLabel[7];
        labels[0] = new JLabel("k =");
        labels[1] = new JLabel("r\u2081 =");
        labels[2] = new JLabel("+");
        labels[3] = new JLabel("i");
        labels[4] = new JLabel("r\u2082 =");
        labels[5] = new JLabel("+");
        labels[6] = new JLabel("i");
        
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                normalize((JTextField)e.getSource());
                Synchronizer.synchronize( reference );
            }
        };
        FocusListener f = new FocusListener() {

            @Override
            public void focusLost( FocusEvent e )
            {
                normalize((JTextField)e.getSource());
                Synchronizer.synchronize( reference );
            }

            @Override
            public void focusGained( FocusEvent arg0 )
            {
                // do nothing
            }
        };
        
        for(int i = 0; i < labels.length; i++)
        {
            labels[i].setFont( BorderPanel.FONT );
        }
        
        for(int i = 0; i < texts.length; i++)
        {
            texts[i] = new JTextField() {
                public Dimension getPreferredSize()
                {
                    return new Dimension(0, super.getPreferredSize().height);
                }
            };
            texts[i].setFont( BorderPanel.FONT );
            texts[i].addActionListener( l );
            texts[i].addFocusListener( f );
            texts[i].setMargin( new Insets(0, 0, 0, 0 ));
        }
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridy = 0;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(5, 5, 5, 5);
        
        add(equationLabel, c);
        
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(5, 1, 5, 1);
        
        add(labels[0], c);
        
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        add(texts[0], c);
        
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.gridwidth = 1;
        
        add(labels[1], c);
        
        c.gridx = 1;
        c.weightx = 1;
        
        add(texts[1], c);
        
        c.gridx = 2;
        c.weightx = 0;
        
        add(labels[2], c);
        
        c.gridx = 3;
        c.weightx = 1;
        
        add(texts[2], c);
        
        c.gridx = 4;
        c.weightx = 0;
        
        add(labels[3], c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        
        add(labels[4], c);
        
        c.gridx = 1;
        c.weightx = 1;
        
        add(texts[3], c);
        
        c.gridx = 2;
        c.weightx = 0;
        
        add(labels[5], c);
        
        c.gridx = 3;
        c.weightx = 1;
        
        add(texts[4], c);
        
        c.gridx = 4;
        c.weightx = 0;
        
        add(labels[6], c);
        
        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        add(substituted, c);
        
        this.graph.key.add( this );
        Synchronizer.synchronize( this.graph );
        
    }
    public void push( HashMap<Synchronizer.Key, Object[]> map )
    {
        if(! map.containsKey( graph.key ))
        {
            return;
        }
        
        Object[] vals = map.get( graph.key );
        
        if(vals == null)
        {
            return;
        }
        
        double[] new_coeff = (double[])(vals[Graph.Index.COEFFICIENTS.ordinal()]);
        if(new_coeff != null)
        {
            double[] root_coeff = new double[5];
            double a = new_coeff[0];
            double b = new_coeff[1];
            double c = new_coeff[2];
            if(a == 0)
            {
                if(b == 0)
                {
                    root_coeff[0] = c;
                    for(int i = 1; i < 5; i++)
                    {
                        root_coeff[i] = Double.NaN;
                    }
                }
                else
                {
                    root_coeff[0] = b;
                    root_coeff[1] = c/b;
                    root_coeff[2] = 0;
                    root_coeff[3] = Double.NaN;
                    root_coeff[4] = Double.NaN;
                }
            }
            else
            {
                root_coeff[0] = a;
                double d = b*b - 4*a*c;
                if(d < 0)
                {
                    root_coeff[1] = - b / (2 * a);
                    root_coeff[2] = Math.sqrt( -d ) / (2 * a);
                    root_coeff[3] = - b / (2 * a);
                    root_coeff[4] = - Math.sqrt( -d ) / (2 * a);
                }
                else
                {
                    root_coeff[1] = (- b + Math.sqrt( d )) / (2 * a);
                    root_coeff[2] = 0;
                    root_coeff[3] = (- b - Math.sqrt( d )) / (2 * a);
                    root_coeff[4] = 0;
                }
                
            }
            for(int i = 0; i < root_coeff.length; i++)
            {
                if(Double.isNaN( root_coeff[i]))
                {
                    texts[i].setText("");
                }
                else
                {
                    texts[i].setText( Form.forOutput(root_coeff[i], 8) );
                }
            }
            substituted.setText( substitute(root_coeff, new_coeff));
        }
    }
    
    private void normalize(JTextField source)
    {
        int source_index = 0;
        for(int i = 0; i < texts.length; i++)
        {
            if(source == texts[i])
            {
                source_index = i;
            }
        }
        if(source_index == 0)
        {
            double scalar = 0;
            try {
                scalar = Double.parseDouble(texts[0].getText());
            }
            catch(NumberFormatException e) { /*do nothing*/}
            texts[0].setText( Form.forOutput( scalar, 8 ) );
            return;
        }
        double real = 0;
        double imag = 0;
        int real_index = ((source_index - 1) & (~1)) + 1;
        int imag_index = ((source_index - 1) | 1) + 1;
        try {
            real = Double.parseDouble(texts[real_index].getText());
        }
        catch(NumberFormatException e) { /*do nothing*/}
        try {
            imag = Double.parseDouble(texts[imag_index].getText());
        }
        catch(NumberFormatException e) { /*do nothing*/}
        
        texts[real_index].setText( Form.forOutput(real, 8) );
        
        texts[imag_index].setText( Form.forOutput(imag, 8) );
        
        
        if(imag != 0)
        {
            
            int compl_real_index = ((real_index - 1) ^ 2) + 1;
            int compl_imag_index = ((imag_index - 1) ^ 2) + 1;
            
            texts[compl_real_index].setText( Form.forOutput(real, 8) );
            
            texts[compl_imag_index].setText( Form.forOutput(-imag, 8) );
        }
                
    }
    @Override
    public HashMap<Synchronizer.Key, Object[]> pull()
    {
        HashMap<Synchronizer.Key, Object[]> map = new HashMap<Synchronizer.Key, Object[]>();
        Object[] data = new Object[Graph.Index.values().length];
        double[] coeff = new double[3];
        double[] root_coeff = new double[5];
        for(int i = 0; i < root_coeff.length; i++)
        {
            try {
                root_coeff[i] = Double.parseDouble(texts[i].getText() );
            }
            catch(NumberFormatException e) {
                root_coeff[i] = 0;
            }
            texts[i].setText(Form.forOutput(root_coeff[i], 8));
        }
        coeff[0] = root_coeff[0];
        coeff[1] = - root_coeff[0] * (root_coeff[1] + root_coeff[3]);
        coeff[2] = root_coeff[0] * (root_coeff[1] * root_coeff[3] - root_coeff[2] * root_coeff[4]);
        data[Graph.Index.COEFFICIENTS.ordinal()] = coeff;
        
        map.put( graph.key,  data);
        
        return map;
    }
    
    @Override
    public Color getBackground()
    {
        if(graph == null)
        {
            return super.getBackground();
        }
        return graph.getColor();
    }
    
    @Override
    public Synchronizer.Key[] getKeys()
    {
        Synchronizer.Key[] keys = {this.graph.key};
        return keys;
    }
    

}
