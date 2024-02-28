 import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientsListener implements Runnable{
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private C4Frame frame = null;

    public ClientsListener(ObjectInputStream is, ObjectOutputStream os, C4Frame frame) {
        this.is = is;
        this.os = os;
        this.frame = frame;
    }

    @Override
    public void run() {
        try
        {
            while(true)
            {
                CommandFromServer cfs = (CommandFromServer)is.readObject();
                //System.out.println(cfs.getCommand());
                //quit
                if(cfs.getCommand() == CommandFromServer.QUIT){
                    //might need an if statement to check which player quit
                    frame.setText("Your opponent quit ");
                }

                // processes the received command
                if(cfs.getCommand() == CommandFromServer.Y_TURN)
                    frame.setTurn('Y');
                else if(cfs.getCommand() == CommandFromServer.R_TURN)
                    frame.setTurn('R');
                else if(cfs.getCommand() == cfs.MOVE)
                {
                    String data = cfs.getData();
                    // pulls data for the move from the data field
                    int c = data.charAt(0) - '0';
                    int r = data.charAt(1) - '0';

                    // changes the board and redraw the screen
                    frame.drop(c,r,data.charAt(2));
                }
                // handles the various end game states
                else if(cfs.getCommand() == CommandFromServer.TIE)
                {
                    frame.setText("Tie game.(Right click to restart)");
                }
                else if(cfs.getCommand() == CommandFromServer.Y_WIN)
                {
                    frame.setText("Yellow wins!(Right click to restart)");
                }
                else if(cfs.getCommand() == CommandFromServer.R_WIN)
                {
                    frame.setText("Red wins!(Right click to restart)");
                }
            }
        }
        catch(Exception error){
            error.printStackTrace();

        }
    }

}



