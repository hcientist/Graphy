import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class GraphPanel extends JPanel implements Synchronizer.Synchable
{
    ArrayList<Graph> graphs;
    Point size;
    Point2D.Double origin;
    Point2D.Double scale;
    Point2D.Double defaultScale;
    double scaling_factor = .75;
    int last_mouse_x;
    int last_mouse_y;
    FontMetrics fm = this.getFontMetrics(BorderPanel.FONT);
    ImageIcon backgroundImage;
    public final Synchronizer.Key key;
    private Point2D.Double focusedPoint;
    private Graph focusedGraph;
    
    public static enum Index {
        BACKGROUNDIMAGE (ImageIcon.class),
        ;
        public final Class<?> type;
        Index (Class<?> type)
        {
            this.type = type;
        }
        
        private static Class<?>[] getIndexTypes()
        {
            Class<?>[] ret = new Class<?>[Index.values().length];
            for(int i = 0; i < Index.values().length; i++)
            {
                ret[i] = Index.values()[i].type;
            }
            return ret;
        }
    }
    
    public GraphPanel(Point2D.Double scale)
    {
        this.graphs = new ArrayList<Graph>();
        this.origin = null; 
        this.scale = scale;
        this.defaultScale = new Point2D.Double(scale.x, scale.y);
        this.key = Synchronizer.getNewKey( Index.getIndexTypes() );
        this.backgroundImage = new ImageIcon();
        key.add( this );
        addMouseListener(new MouseListener1());
        addMouseMotionListener(new MotionListener1());
        addMouseWheelListener(new WheelListener1());
    }
    
    public void add(Graph graph)
    {
        graphs.add( graph );
        graph.key.add( this );
    }
    
    public boolean remove(Graph graph)
    {
        graph.key.remove( this );
        return graphs.remove( graph );
    }
    
    public void setBackgroundImage(ImageIcon backgroundImage)
    {
        if(backgroundImage != null)
        {
            this.backgroundImage = backgroundImage;
        }
        else
        {
            this.backgroundImage = new ImageIcon();
            this.backgroundImage.setImage( null );
        }
    }
    public ImageIcon getBackgroundImage()
    {
        return this.backgroundImage;
    }
    
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        size = new Point(this.getWidth(), this.getHeight());
        if(origin == null)
        {
            origin = new Point2D.Double(size.x/2.0, size.y/2.0);
        }
        g2.clearRect( 0, 0, size.x, size.y );
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont( BorderPanel.FONT );
        
        if(backgroundImage.getImage() != null)
        {
            double width = 500 * scale.x / defaultScale.x;
            double height = 500 * scale.y / defaultScale.y;
            g2.drawImage(backgroundImage.getImage(), 
                (int)(origin.x - width / 2.0), 
                (int)(origin.y - height / 2.0),
                (int)width,
                (int)height,
                backgroundImage.getImageObserver());
        }
        g2.setStroke( new BasicStroke((float)0.8) );
        
        Color minorColor = new Color(244, 244, 244);
        Color majorColor = new Color(224, 224, 224);
        Color fontColor = new Color(128, 128, 128);
        
        double x_min = -origin.x/scale.x;
        double x_max = (size.x - origin.x)/scale.x;
        
        double x_major = Math.pow( 2, Math.floor(Math.log( x_max - x_min ) / Math.log(2)) - 2);
        double x_minor = x_major / 4.0;
        
        g2.setColor(minorColor);
        for(double x = Math.floor(x_min/x_minor) * x_minor; x < x_max; x+= x_minor)
        {
            double x_p = x*scale.x + origin.x;
            g2.draw( new Line2D.Double(x_p, 0, x_p, size.y));
        }
        

        double y_max = origin.y / scale.y;
        double y_min = (origin.y - size.y) / scale.y;

        double y_major = Math.pow( 2, Math.floor(Math.log( y_max - y_min ) / Math.log(2)) - 2);
        double y_minor = y_major / 4.0;
        
        g2.setColor(minorColor);
        for(double y = Math.floor(y_min/y_minor) * y_minor; y < y_max; y+= y_minor)
        {
            double y_p = origin.y - y*scale.y ;
            g2.draw( new Line2D.Double(0, y_p, size.x, y_p));
        }
        
        for(double x = Math.floor(x_min/x_major) * x_major; x < x_max; x+= x_major)
        {
            double x_p = x*scale.x + origin.x;
            g2.setColor(majorColor);
            g2.draw( new Line2D.Double(x_p, 0, x_p, size.y));
            String x_string = Form.forOutput( x, 8 );
            g2.setColor( fontColor );
            int num_x_pos = (int)x_p - fm.stringWidth(x_string) / 2;
            g2.drawString( x_string, num_x_pos, fm.getAscent() );
            g2.drawString( x_string, num_x_pos, size.y - fm.getDescent());
        }
        
        
        for(double y = Math.floor(y_min/y_major) * y_major; y < y_max; y += y_major)
        {
            double y_p = origin.y - y*scale.y ;
            g2.setColor(majorColor);
            g2.draw( new Line2D.Double(0, y_p, size.x, y_p));
            String y_string = Form.forOutput( y );
            g2.setColor( fontColor );
            int num_y_pos = (int)(y_p + fm.getLineMetrics(y_string, g2).getAscent() / 2);
            g2.drawString( y_string, 2, num_y_pos );
            g2.drawString( y_string, size.x - 1 - fm.stringWidth( y_string), num_y_pos);
        }
        

        g2.setColor( Color.black );
        g2.setStroke( new BasicStroke(1) );
        g2.draw( new Line2D.Double(0, origin.y, size.x, origin.y ));
        g2.draw( new Line2D.Double( origin.x, 0, origin.x, size.y) );

        g2.setStroke( new BasicStroke((float)1.8) );
        for(Graph graph : graphs)
        {
            Shape curve = graph.getShape(size , origin, scale );
            Color c = graph.getColor();
            g2.setColor( c );
            if(curve != null)
            {
                g2.draw(curve);
                if(graph.getAxis())
                {
                    float[] dash = new float[2];
                    dash[0] = 10;
                    dash[1] = 10;
                    Stroke saveStroke = g2.getStroke();
                    g2.setStroke( new BasicStroke(3,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        (float)10.0,
                        dash,
                        (float)0.0) );
                    g2.draw(graph.getAxisShape( size, origin, scale ));
                    g2.setStroke( saveStroke );
                }
            }
            if(graph == focusedGraph)
            {
                Stroke saveStroke = g2.getStroke();
                g2.setStroke( new BasicStroke((float)0.7));
                double p_x = focusedPoint.x * scale.x + origin.x;
                double p_y = origin.y - focusedPoint.y * scale.y;
                g2.draw( new Ellipse2D.Double(p_x -7, p_y -7, 14, 14) );
                g2.draw(new Ellipse2D.Double(p_x - 1, p_y - 1, 2, 2));
                g2.setStroke( saveStroke );
                String x_s = Form.forOutput( focusedPoint.x, 4);
                String y_s = Form.forOutput( focusedPoint.y, 4);
                String s_s = Form.forOutput( focusedGraph.getSlope( focusedPoint.x ), 4);
                g2.drawString( "( " + x_s +", " + y_s + " )", (int)p_x + 10, (int)p_y);
                g2.drawString( "Slope: " + s_s, (int)p_x + 10, (int)p_y + fm.getAscent() );
            }
        }
    }

    private class MouseListener1 implements MouseListener
    {
        public void mousePressed(MouseEvent e)
        {
            last_mouse_x = e.getX();
            last_mouse_y = e.getY();
        }

        @Override
        public void mouseClicked( MouseEvent arg0 )
        {
            // do nothing
            
        }

        @Override
        public void mouseEntered( MouseEvent arg0 )
        {
            // do nothing
            
        }

        @Override
        public void mouseExited( MouseEvent arg0 )
        {
            focusedGraph = null;
            focusedPoint = null;
            repaint();
        }

        @Override
        public void mouseReleased( MouseEvent arg0 )
        {
            // do nothing
            
        }
    }
    private class MotionListener1 implements MouseMotionListener
    {
        public void mouseDragged(MouseEvent e)
        {
            origin.x += e.getX() - last_mouse_x;
            origin.y += e.getY() - last_mouse_y;
            last_mouse_x = e.getX();
            last_mouse_y = e.getY();
            repaint();
        }
        public void mouseMoved(MouseEvent e)
        {
            int x_p = e.getX();
            int y_p = e.getY();
            double x = (x_p - origin.x)/scale.x;
            double y = (origin.y - y_p)/scale.y;
            updatePoint(x, y);
            repaint();
        }
        
    }
    private class WheelListener1 implements MouseWheelListener
    {

        public void mouseWheelMoved( MouseWheelEvent e )
        {
            double num;
            if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
            {
                num = e.getScrollAmount() * (e.getWheelRotation() > 0 ? 1 : -1);
            }
            else
            {
                num = e.getWheelRotation();
            }
            zoom(num);
            updatePoint(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public HashMap<Synchronizer.Key, Object[]> pull()
    {
        HashMap<Synchronizer.Key, Object[]> map = new HashMap<Synchronizer.Key, Object[]>();
        
        Object[] data = new Object[Index.values().length];
        
        data[Index.BACKGROUNDIMAGE.ordinal()] = this.getBackgroundImage();
        
        map.put( key, data );
        
        return map;
    }

    @Override
    public void push( HashMap<Synchronizer.Key, Object[]> map )
    {
        if(map.containsKey( key ))
        {
            Object[] vals = map.get( key );
            
            if(vals != null)
            {
                ImageIcon new_backgroundImage = (ImageIcon)vals[Index.BACKGROUNDIMAGE.ordinal()];
                if(new_backgroundImage != null)
                {
                    this.setBackgroundImage( new_backgroundImage );
                }
            }
        }
        for(Graph g : graphs)
        {
            if(map.containsKey( g.key ))
            {
                g.push( map );
            }
        }
        repaint();
    }

    public void zoom(double num)
    {
        double f = Math.pow( scaling_factor, num );
        scale.x *= f;
        scale.y *= f;
        origin.x = (1 - f) * size.x / 2 + f * origin.x;
        origin.y = (1 - f) * size.y / 2 + f * origin.y;
        repaint();
        
    }

    public void restoreDefaults()
    {
        scale.x = defaultScale.x;
        scale.y = defaultScale.y;
        origin.x = size.x / 2.0;
        origin.y = size.y / 2.0;
        repaint();
        
    }
    private void updatePoint(double x, double y)
    {
        Point2D.Double mousePoint = new Point2D.Double(x, y);
        double minDist = Double.POSITIVE_INFINITY;
        focusedGraph = null;
        focusedPoint = null;
        for(Graph g : graphs)
        {
            Point2D.Double curPoint = g.minDistPoint(mousePoint);
            if(curPoint == null)
            {
                continue;
            }
            double dist = curPoint.distance( mousePoint );
            if(dist < minDist)
            {
                minDist = dist;
                focusedGraph = g;
                focusedPoint = curPoint;
            }
        }
    }
    @Override
    public Synchronizer.Key[] getKeys()
    {
        Synchronizer.Key[] keys = {this.key};
        return keys;
    }
}
