import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class C4Frame extends JFrame implements WindowListener, MouseListener {

    public C4Frame(){
        super("C4");
        setSize(600,560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);


        setVisible(true);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        // will indirectly call drop method which is similar to the makeMove method in the TTTFrame in tic tac toe
        int r=-1;
        int c=-1;
        //7 if statements for 7 columns
        //column 1
        if(x>=20 && x <=80){
            for(int i=6 ; i>=0 ; i--){
                if (gameData[i][0]==' '){
                    r=i;
                    c=0;
                    break;
                }
            }
        }
        //column 2
        if(x>=100 && x <=160){
            for(int i=6 ; i>=0 ; i--){
                if (gameData[i][1]==' '){
                    r=i;
                    c=1;
                    break;
                }
            }
        }
        //column 3
        if(x>=180 && x <=240){
            for(int i=6 ; i>=0 ; i--){
                if (gameData[i][2]==' '){
                    r=i;
                    c=2;
                    break;
                }
            }
        }
        //column 4
        if(x>=260 && x <=320){
            for(int i=6 ; i>=0 ; i--){
                if (gameData[i][3]==' '){
                    r=i;
                    c=3;
                    break;
                }
            }
        }
        //column 5
        if(x>=340 && x <=400){
            for(int i=6 ; i>=0 ; i--){
                if (gameData[i][4]==' '){
                    r=i;
                    c=4;
                    break;
                }
            }
        }
        //column 6
        if(x>=420 && x <=480){
            for(int i=6 ; i>=0 ; i--){
                if (gameData[i][5]==' '){
                    r=i;
                    c=5;
                    break;
                }
            }
        }
        //column 7
        if(x>=500 && x <=560){
            for(int i=6 ; i>=0 ; i--){
                if (gameData[i][6]==' '){
                    r=i;
                    c=6;
                    break;
                }
            }
        }

        if (c!=-1){
            try {
                os.writeObject(new CommandFromClient(CommandFromClient.MOVE, "" + c + r + player));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void drop(int c, int r, char letter)
    {
        gameData.getGrid()[r][c] = letter;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        //not finished
        g.setColor(Color.PINK);
        g.fillRect(0,0,getWidth(),getHeight());
        for(int c=0; c<6; c++){
            int y=40+(c*80);
            for(int i=0; i<7; i++){
                g.setColor(Color.WHITE);
                g.drawOval(20 + (i*80),y, 60, 60);
                g.fillOval(20 + (i*80),y,60,60);
            }
        }



    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}