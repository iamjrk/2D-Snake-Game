import javax.swing.*;

public class frame extends JFrame
{
    frame()
    {
        this.add(new panel());
        this.setTitle("2D Snake Game"); //Title of game
        this.setResizable(false);
        //pack() : it set the window to a preferable size
        this.pack();
        this.setVisible(true);
    }
}
