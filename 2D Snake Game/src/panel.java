import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class panel extends JPanel implements ActionListener
{
    //height & width of the panel
    static int width=1200;
    static int height=600;
    //unit is the grid for spawning food
    static int unit=50;
    int totalunit=(width*height)/unit;
    int score;
    int fx,fy; //coordinates of food spawn;
    int length=3;//initial length of snake
    char dir='R'; //right direction
    boolean flag=false; //check if the game is running or not

    //for spawning food randomly throughout the screen
    Random random;
    Timer timer;
    int Delay =160; //delay in milliseconds to spawn food
    int xsnake[]=new int[totalunit];
    int ysnake[]=new int[totalunit];

    panel()
    {
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.BLACK); //background color
        this.addKeyListener(new MyKey());

        this.setFocusable(true);//enables keyboard input to the app.
        random = new Random();
        gamestart();
    }

    public void gamestart()
    {
        flag=true;
        spawnfood();
        //timer to check on the game state in each 160 ms
        timer= new Timer(Delay,this);
        timer.start(); //starts the timer
    }
    public void spawnfood()
    {
        //random function for spawning the food
        //to get fx & fy to be multiple of 50
        fx = random.nextInt((int) width/unit)* unit;
        fy = random.nextInt((int) height/unit)* unit;
    }

    //is implicitly called when the panel is created
    public void paintComponent(Graphics graphic)
    {

        //paint components will help in drawing graphics for the score , snake , food , in game-over screen
        super.paintComponent(graphic); //calling method from parent class(JPanel)
        draw(graphic);
    }
    public void draw(Graphics graphic)
    {
        if(flag)//true if game is running
        {
            //spawning food particle
           graphic.setColor(Color.orange);
           graphic.fillOval(fx,fy,unit,unit);

           //to spawn snake body
            for(int i=0;i<length;i++)
            {
                if(i==0)//head of snake
                {
                    graphic.setColor(Color.RED);
                    graphic.fillOval(xsnake[0],ysnake[0],unit,unit);
                }
                else
                {
                    graphic.setColor(Color.BLUE);
                    graphic.fillOval(xsnake[i],ysnake[i],unit,unit);
                }
            }
            //for the score display
            graphic.setColor(Color.GREEN);
            graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics fme = getFontMetrics(graphic.getFont());
            graphic.drawString("Score: "+score,(width- fme.stringWidth("Score: "+score))/2,graphic.getFont().getSize());


        }
        else
        {
            GameOver(graphic);
        }
    }
    public void GameOver(Graphics graphic)
    {
        //Score Display
        graphic.setColor(Color.GREEN);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Score: "+score,(width- fme.stringWidth("Score: "+score))/2,graphic.getFont().getSize());

        //Game Over Text
        graphic.setColor(Color.GREEN);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,80));
        FontMetrics fme1 = getFontMetrics(graphic.getFont());
        graphic.drawString("Game Over ",(width- fme1.stringWidth("Game Over"))/2,height/2);

        //Replay Prompt Display
        graphic.setColor(Color.WHITE);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics fme2 = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to Restart...",(width- fme2.stringWidth("Press R to Restart..."))/2,height/2+150);
    }
    public void move()
    {
        //for all other body parts
        for(int i=length;i>0;i--)
        {
            xsnake[i]=xsnake[i-1];
            ysnake[i]=ysnake[i-1];
        }

        //updating the head
        switch (dir)
        {
            case 'U':
                ysnake[0]=ysnake[0]-unit;
                break;
            case 'D':
                ysnake[0]=ysnake[0]+unit;
                break;
            case 'L':
                xsnake[0]=xsnake[0]-unit;
                break;
            case 'R':
                xsnake[0]=xsnake[0]+unit;
                break;
        }
    }
    //checking collision
    void check()
    {
        //check if the snake had collided with itself
        for(int i=length;i>0;i--)
        {
            if((xsnake[0]==xsnake[i])&&(ysnake[0]==ysnake[i]))
            {
                flag=false;
            }
        }
        //check if the snake had collided with walls
        if(xsnake[0]<0)
        {
            flag=false;
        }
        else if(xsnake[0]>=width)
        {
            flag=false;
        } else if (ysnake[0]<0)
        {
            flag=false;
        } else if (ysnake[0]>=height)
        {
            flag=false;
        }
        if(flag==false)
        {
            timer.stop();
        }
    }
    public void foodeaten()
    {
        if((xsnake[0]==fx)&&(ysnake[0]==fy))
        {
            length++;
            score++;
            spawnfood();
        }
    }
    public class MyKey extends KeyAdapter
    {
        //key adapter class helping us to interface with keyboard
        public void keyPressed(KeyEvent k)
        {
            switch (k.getKeyCode())
            {
                case VK_UP:
                    if(dir!='D')
                    {
                        dir='U';
                    }
                    break;
                case VK_DOWN:
                    if(dir!='U')
                    {
                        dir='D';
                    }
                    break;
                case VK_RIGHT:
                    if(dir!='L')
                    {
                        dir='R';
                    }
                    break;
                case VK_LEFT:
                    if(dir!='R')
                    {
                        dir='L';
                    }
                    break;
                case VK_R:
                    if(!flag)
                    {
                        score=0;
                        length=3;
                        dir='R';
                        Arrays.fill(xsnake,0);
                        Arrays.fill(ysnake,0);
                        gamestart();
                    }
                    break;

            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(flag)
        {
            move();
            foodeaten();
            check();
        }
        //explicitly calls the paintcomponent function
        repaint();
    }
}
