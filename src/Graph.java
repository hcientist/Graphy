import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ImageIcon;



public class Graph implements Synchronizer.Synchable
{
    Color color;
    double[] coeff;
    boolean axis = false;
    boolean focused = false;
    public final Synchronizer.Key key;

    public static enum Index {
        COEFFICIENTS (double[].class),
        COLOR (Color.class),
        AXIS (Boolean.class),
        FOCUSED (Boolean.class),
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
    
    
    
    public Graph(double a, double b, double c, Color color, Synchronizer.Key key)
    {
        this.coeff = new double[3];
        this.coeff[0] = a;
        this.coeff[1] = b;
        this.coeff[2] = c;
        this.color = color;
        this.key = key;
        key.add( this );
    }
    
    public Graph(double a, double b, double c, Color color)
    {
        this(a, b, c, color, Synchronizer.getNewKey( Index.getIndexTypes() ));
    }

    public Graph(double[] coeff, Color color, Synchronizer.Key key)
    {
        this(coeff[0], coeff[1], coeff[2], color, key);
    }
    
    public Graph(double[] coeff, Color color)
    {
        this(coeff[0], coeff[1], coeff[2], color);
    }
    
    public Color getColor()
    {
        return color;
    }
    public boolean getAxis()
    {
        return this.axis;
    }
    public void setAxis(boolean axis)
    {
        this.axis = axis;
    }
    public void setColor(Color color)
    {
        this.color = color;
    }
    public boolean getFocused()
    {
        return this.focused;
    }
    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }
    public double[] getCoefficients()
    {
        double[] ret = this.coeff.clone();
        return ret;
    }
    public void setCoefficients(double[] coeff)
    {
        this.coeff = coeff.clone();
        assert(this.coeff.length == 3);
    }
    public void setCoefficients(double a, double b, double c)
    {
        this.coeff[0] = a;
        this.coeff[1] = b;
        this.coeff[2] = c;
    }
    public Shape getShape(Point size, Point2D.Double origin, Point2D.Double scale)
    {
        Shape curve;
        double a = coeff[0];
        double b = coeff[1];
        double c = coeff[2];
        if(a > 0)
        {
            double y_top = origin.y / scale.y;
            double d = b*b - 4*a*(c-y_top);
            if(d <= 0)
            {
                return null;
            }
            double sqrt_d = Math.sqrt(d);
            double x1 = (-b + sqrt_d)/(2*a);
            double x2 = (-b - sqrt_d)/(2*a);
            double vx = -b/(2*a);
            double vy = -(b*b)/(4*a) + c;
            
            double x1_p = x1 * scale.x + origin.x;
            double x2_p = x2 * scale.x + origin.x;
            if(x1_p < 0 || x2_p >= size.x)
            {
                return null;
            }  
            double vx_p = vx * scale.x + origin.x;
            double vy_p = origin.y - vy * scale.y;
            curve = new QuadCurve2D.Double( x1_p, 0, vx_p, vy_p * 2, x2_p, 0 );
        }
        else if(a < 0)
        {
            double y_bot = (origin.y - size.y + 1) / scale.y;
            double d = b*b - 4*a*(c-y_bot);
            if(d <= 0)
            {
                return null;
            }
            double sqrt_d = Math.sqrt(d);
            double x1 = (-b + sqrt_d)/(2*a);
            double x2 = (-b - sqrt_d)/(2*a);
            double vx = -b/(2*a);
            double vy = -(b*b)/(4*a) + c;
            
            double x1_p = x1 * scale.x + origin.x;
            double x2_p = x2 * scale.x + origin.x;
            if(x2_p < 0 || x1_p >= size.x)
            {
                return null;
            }  
            double vx_p = vx * scale.x + origin.x;
            double vy_p = origin.y - vy * scale.y;
            curve = new QuadCurve2D.Double( x1_p, size.y - 1, vx_p, 2*vy_p - size.y, x2_p, size.y - 1 );
        }
        else 
        {
            double x_left = -origin.x / scale.x;
            double x_right = (size.x - origin.x) / scale.x;
            double y1 = x_left * b + c;
            double y2 = x_right * b + c;
            double y1_p = origin.y - y1 * scale.y;
            double y2_p = origin.y - y2 * scale.y;
            
            if((y1_p < 0 && y2_p < 0) || (y1_p >= size.y && y2_p >= size.y))
            {
                return null;
            }
            curve = new Line2D.Double(0, y1_p, size.x, y2_p);
        }
        return curve;
    }
    public Shape getAxisShape(Point size, Point2D.Double origin, Point2D.Double scale)
    {

        double a = coeff[0];
        double b = coeff[1];
        double x_p = ( -b / (2*a) ) * scale.x + origin.x;
        return new Line2D.Double(x_p, 0, x_p, size.y);
    }
    @Override
    public void push( HashMap<Synchronizer.Key, Object[]> map)
    {
        if(! map.containsKey( key ))
        {
            return;
        }
        
        Object[] vals = map.get( key );
        
        if(vals == null)
        {
            return;
        }
        
        double[] new_coeff = (double[])vals[Index.COEFFICIENTS.ordinal()];
        if(new_coeff != null)
        {
            this.setCoefficients( new_coeff );
        }
        
        Color new_color = (Color)vals[Index.COLOR.ordinal()];
        if(new_color != null)
        {
            this.setColor( new_color );
        }
        
        Boolean new_axis = (Boolean)vals[Index.AXIS.ordinal()];
        if(new_axis != null)
        {
            this.setAxis( new_axis );
        }
        
        
    }
    @Override
    public HashMap<Synchronizer.Key, Object[]> pull()
    {
        HashMap<Synchronizer.Key, Object[]> map = new HashMap<Synchronizer.Key, Object[]>();
        Object[] data = new Object[Index.values().length];
        
        data[Index.COEFFICIENTS.ordinal()] = this.getCoefficients();
        data[Index.COLOR.ordinal()] = this.getColor();
        data[Index.AXIS.ordinal()] = this.getAxis();
        
        map.put(this.key, data);
        
        return map;
    }

    public Point2D.Double minDistPoint(Point2D.Double mousePoint)
    {
        if(coeff[0] != 0)
        {
            double a = 2 * coeff[0] * coeff[0];
            double b = 3 * coeff[0] * coeff[1];
            double c = 2 * coeff[0] * coeff[2] 
                            + coeff[1] * coeff[1] 
                            - 2 * mousePoint.y *coeff[0]
                            + 1;
            double d = coeff[1] * coeff[2]
                       - mousePoint.y * coeff[1]
                       - mousePoint.x;
            
            double[] roots = getRealCubeRoots(a, b, c, d);
            double minDist = Double.POSITIVE_INFINITY;
            double curDist;
            Point2D.Double minPoint = null;
            for(int i = 0; i < roots.length; i++)
            {
                double x = roots[i];
                double y = coeff[0] * x * x + coeff[1] * x + coeff[2];
                curDist = mousePoint.distance( x, y );
                if(curDist < minDist)
                {
                    minDist = curDist;
                    minPoint = new Point2D.Double(x, y);
                }
            }
            return minPoint;
        }
        double x = ( mousePoint.x + coeff[1] * (mousePoint.y - coeff[2]))/(coeff[1] * coeff[1] + 1);
        double y = coeff[1] * x + coeff[2];
        return new Point2D.Double(x, y);
    }
    public static double[] shortcut(double c1, double c2, double c3, double py, double px)
    {
        double p_prime_y = (py + c2 / 2 + c3) / c1;
        double p_prime_x = px + c2 / (2 * c1);
        
        double a;
        double d = -p_prime_x;
        
        double[] ret = null;
        
        if(p_prime_y < 0)
        {

            a = 1 / -p_prime_y;
            
            double x = - Math.cbrt( d / a );
            ret = new double[1];
            ret[0] = x;
        }
        else if(p_prime_y > 0)
        {
            a = 1 / p_prime_y;
            double plusQ = 0;
        }
        else
        {
            a = 1;
        }
        return ret;
                        
    }
    public static double[] getRealCubeRoots(double a, double b, double c, double d)
    {
        double b_squared = b * b;
        double b_cubed = b_squared * b;
        double a_c = a * c;
        double a_d = a * d;
        double b_c = b * c;
        double a_b_c = a_c * b;
        
        double a_squared_d = a_d * a;
        double term1 = 2 * b_cubed - 9 * a_b_c + 27 * a_squared_d;
        double term2 = b_squared - 3 * a_c;
        double radicand = Math.pow(term1, 2)
                        - 4 * Math.pow( term2 , 3 );
        double Q;
        double C;
        if(radicand == 0)
        {
            C = Math.cbrt( term1 / 2 );
            if(term2 == 0)
            {
                double x = - b / (3 * a);
                double[] ret = {x};
                return ret;
            }
            double x1 = (b_c - 9 * a_d) / (-2 * (term2));
            double x2 = (9 * a_squared_d - 4 * a_b_c + b_cubed) / (-a * (term2));
            double[] ret = {x1, x2};
            return ret;
        }
        if(radicand > 0)
        {
            Q = Math.sqrt( radicand );
            C = Math.cbrt( (Q + term1) / 2 );
            if(C == 0 && term2 == 0)
            {
                Q = -Q;
                C = Math.cbrt( (Q + term1) / 2 );
                double x = (b + C + term2 / C) / (-3 * a);
                
                double[] ret = {x};
                return ret;
            }
            double x1 = (b + C + term2 / C) / (-3 * a);
            
            C = Math.cbrt( (-Q + term1) / 2 );
            double x2 = (b + C + term2 / C) / (-3 * a);
            
            double[] ret = {x1, x2};
            
            return ret;
        }
        //Finally, appeal to deMoivre
        Q = Math.sqrt( -radicand );
        double phi_1 = Math.atan2(Q, term1);
        double phi_2 = Math.atan2(-Q, term1);
        double Q_over_2 = Q / 2;
        double term1_over_2 = term1 / 2;
        double r = Math.pow(Q_over_2 * Q_over_2  + term1_over_2 * term1_over_2, 1.0 / 6.0);
        double r_squared = r * r;
        double root_3 = Math.sqrt(3);
        
        double[] C_r = new double[6];
        double[] C_i = new double[6];
        
        for(int i = 0; i < 3; i++)
        {
            C_r[i] = r * Math.cos((phi_1 + 2 * i * Math.PI) / 3);
            C_i[i] = r * Math.sin((phi_1 + 2 * i * Math.PI) / 3);
            C_r[3 + i] = r * Math.cos((phi_2 + 2 * i * Math.PI) / 3);
            C_i[3 + i] = r * Math.sin((phi_2 + 2 * i * Math.PI) / 3);
        }
        HashSet<Double> set = new HashSet<Double>();
        for(int i = 0; i < 6; i++)
        {
            double x1_r = ( -b 
                            - C_r[i] - C_r[i] * (term2) / r_squared
                          ) / ( 3 * a);
            //double x1_i = (term2 / r_squared - 1) * C_i[i] / (3 * a);
            
            set.add( x1_r );
            
            double x2_r = ( -b
                            + (C_r[i] - C_i[i] * root_3) / 2 
                            + (C_r[i] - C_i[i] * root_3) * term2 / ( 2 * r_squared)
                          ) / (3 * a);
            //double x2_i = (C_r[i] * root_3 + C_i[i] - (C_r[i] * root_3 + C_i[i]) * term2 / (r_squared)) / ( 6 * a);

            set.add( x2_r );
            
            double x3_r = ( -b 
                            + (C_r[i] + C_i[i] * root_3) / 2 
                            + (C_r[i] + C_i[i] * root_3) * term2 / ( 2 * r_squared)
                          ) / (3 * a);
            //double x3_i = (-C_r[i] * root_3 + C_i[i] + (C_r[i] * root_3 - C_i[i]) * term2 / (r_squared)) / ( 6 * a);

            set.add( x3_r );
            
        }
        double[] ret = new double[set.size()];
        int i = 0;
        for(Double x : set)
        {
            ret[i++] = x;
        }
        return ret;
    }
    public double getSlope(double x)
    {
        return 2 * coeff[0] * x + coeff[1];
    }

    @Override
    public Synchronizer.Key[] getKeys()
    {
        Synchronizer.Key[] keys = {this.key};
        return keys;
    }

}
