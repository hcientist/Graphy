import javax.swing.JFrame;


public class Driver
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Graphy");
        frame.setSize(750, 500);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainPanel());
        frame.setVisible(true);
    }
}
