import javax.swing.JApplet;


public class AppletDriver extends JApplet
{
    public void init()
    {
        setSize(839, 590);
        setContentPane(new MainPanel());
    }
}
