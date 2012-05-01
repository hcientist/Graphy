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


public class StandardForm extends Form
{
    Graph graph;
    JTextField[] texts;
    JLabel substituted;
    public static enum Letter {
        A ("a"), B ("b"), C ("c"), D ("d"), E ("e"), F ("f"), G ("g"), H ("h"), I ("i"), J ("j"), K ("k"), 
        L ("l"), M ("m"), N ("n"), O ("o"), P ("p"), Q ("q"), R ("r"), S ("s"), T ("t"), U ("u"), V ("v"), W ("w"),
        ALPHA ("\u03b1"), BETA ("\u03b2"), GAMMA ("\u03b3"), DELTA ("\u03b4"), EPSILON ("\u03b5"), ZETA ("\u03b6"), 
        ETHA ("\u03b7"), THETA ("\u03b8"), IOTA ("\u03b9"), KAPPA ("\u03ba"), LAMBDA ("\u03bb"), MU ("\u03bc"), 
        NU ("\u03bd"), XI ("\u03be"), OMICRON ("\u03bf"), PI ("\u03c0"), RHO ("\u03c1"), SIGMA ("\u03c2"), 
        TAU ("\u03c3"), UPSILON ("\u03c4"), PHI ("\u03c5"), CHI ("\u03c6"), PSI ("\u03c7"), OMEGA("\u03c8");
        public final String letter; 
        private Letter(String letter)
        {
            this.letter = letter;
        }
    }
    public static enum Superscript {
        ZERO ("\u2070"), ONE ("\u00b9"), TWO ("\u00b2"), THREE ("\u00b3"), FOUR ("\u2074"), 
        FIVE ("\u2075"), SIX ("\u2076"), SEVEN ("\u2077"), EIGHT ("\u2078"), NINE ("\u2079")
        ;
        public final String letter; 
        private Superscript(String letter)
        {
            this.letter = letter;
        }
    }
    
    public static String substitute(String[] coeff)
    {
        String equation = "y = ";
        
        Superscript[] superscripts = Superscript.values();
        for(int i = 0; i < coeff.length; i++)
        {
            if(i != 0)
            {
                equation = equation +  "+ ";
            }
            equation = equation + coeff[i];
            if(i < coeff.length - 1)
            {
                equation = equation + "x";
            }
            if(i > coeff.length - 3)
            {
                equation = equation + " ";
                continue;
            }
            int exponent = coeff.length - 1 - i;
            int power = 1;
            String superscript = "";
            while(exponent != 0)
            {
                int num = (exponent % (power * 10))/power;
                superscript = superscripts[num].letter + superscript;
                exponent -= num * power;
                
            }
            equation = equation + superscript + " ";
        }
        return equation.equals( "y = " ) ? "y = 0" : equation;
    }
    
    public static String substitute(double[] coeff)
    {
        String equation = "y = ";
        
        Superscript[] superscripts = Superscript.values();
        for(int i = 0; i < coeff.length; i++)
        {
            if(coeff[i] == 0)
            {
                continue;
            }
            if(equation.length() != 4)
            {
                equation = equation +  (coeff[i] < 0 ? "- " : "+ ");
            }
            else if( coeff[i] < 0)
            {
                equation = equation + "-";
            }
            if(coeff[i] != 1 || i == coeff.length - 1)
            {
                String s = Form.forOutput(Math.abs(coeff[i]));
                equation = equation + s;
            }
            
            if(i < coeff.length - 1)
            {
                equation = equation + "x";
            }
            if(i > coeff.length - 3)
            {
                continue;
            }
            int exponent = coeff.length - 1 - i;
            int power = 1;
            String superscript = "";
            while(exponent != 0)
            {
                int num = (exponent % (power * 10))/power;
                superscript = superscripts[num].letter + superscript;
                exponent -= num * power;
            }
            equation = equation + superscript + " ";
        }
        return equation.equals( "y = " ) ? "y = 0" : equation;
    }
    
    
    public StandardForm(Graph graph)
    {
        this.graph = graph;
        int num_coeff = graph.getCoefficients().length;
        Letter[] letters = Letter.values();
        String[] coeff = new String[num_coeff];
        texts = new JTextField[num_coeff];
//        for(int i = 0; i < letters.length; i++)
//        {
//            System.out.println(letters[i].letter);
//        }
//        
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                Synchronizer.synchronize(reference);
            }
        };
        FocusListener f = new FocusListener() {

            @Override
            public void focusGained( FocusEvent arg0 )
            {
                //do nothing
            }

            @Override
            public void focusLost( FocusEvent arg0 )
            {
                Synchronizer.synchronize( reference );
            }
        };
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 0;
        c.insets = new Insets(3, 1, 3, 1);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        for(int i = 0; i < num_coeff; i++)
        {
            coeff[i] = String.valueOf( letters[i].letter );
            
            JLabel label = new JLabel(coeff[i] + " = ");
            label.setFont(BorderPanel.FONT);
            
            c.anchor = GridBagConstraints.LINE_START;
            c.gridx = 0;
            c.gridy = i + 1;
            c.weightx = 0;
            
            this.add( label, c );
            
            texts[i] = new JTextField();
            texts[i].setFont( BorderPanel.FONT );
            texts[i].addActionListener( l );
            texts[i].addFocusListener( f );
            texts[i].setMargin(new Insets(0, 0, 0, 0 ));
            
            c.anchor = GridBagConstraints.LINE_END;
            c.gridx = 1;
            c.weightx = 1;
            
            this.add( texts[i], c );
        }
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        JLabel equationLabel = new JLabel(substitute(coeff));
        equationLabel.setFont( BorderPanel.FONT );
        this.add( equationLabel, c );
        
        c.gridy = num_coeff + 1;

        this.substituted = new JLabel();
        substituted.setFont( BorderPanel.FONT );
        this.add( this.substituted, c );
        
        this.graph.key.add( this );
        Synchronizer.synchronize( graph );
        
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
        
        double[] new_coeff = (double[])vals[Graph.Index.COEFFICIENTS.ordinal()];
        if(new_coeff != null)
        {
            for(int i = 0; i < new_coeff.length; i++)
            {
                texts[i].setText( Form.forOutput( new_coeff[i], 8) );
            }
            substituted.setText( substitute(new_coeff));
        }
    }

    @Override
    public HashMap<Synchronizer.Key, Object[]> pull()
    {
        HashMap<Synchronizer.Key, Object[]> map = new HashMap<Synchronizer.Key, Object[]>();
        Object[] data = new Object[Graph.Index.values().length];
        double[] coeff = new double[texts.length];
        for(int i = 0; i < coeff.length; i++)
        {
            try {
                coeff[i] = Double.parseDouble(texts[i].getText() );
            }
            catch(NumberFormatException e) {
                coeff[i] = 0;
            }
            
            texts[i].setText( Form.forOutput( coeff[i], 8));
        }
        data[Graph.Index.COEFFICIENTS.ordinal()] = coeff;
        map.put(graph.key, data);
        
        return map;
    }
    @Override
    public Synchronizer.Key[] getKeys()
    {
        Synchronizer.Key[] keys = {this.graph.key};
        return keys;
    }
}
