package PacMan;

import imgs.PacmanModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.io.*;
import java.util.*;
import java.net.*;


// This class draws the probability map and value iteration map that you create to the window
// You need only call updateProbs() and updateValues() from your theRobot class to update these maps
class mySmartMap extends JComponent implements ActionListener, KeyListener {
  public static final int NORTH = 0;
  public static final int SOUTH = 1;
  public static final int EAST = 2;
  public static final int WEST = 3;

  int currentKey;

  int winWidth, winHeight;
  double sqrWdth, sqrHght;
  Color gris = new Color(170,170,170);
  Color myWhite = new Color(220, 220, 220);
  World mundo;
  PacmanModel pacman;

  int gameStatus;

  public mySmartMap(int w, int h, World wld) {
    mundo = wld;
    winWidth = w;
    winHeight = h;

    sqrWdth = (double)w / mundo.width;
    sqrHght = (double)h / mundo.height;
    currentKey = -1;

    addKeyListener(this);

    gameStatus = 0;
    pacman = new PacmanModel((int) sqrWdth, (int) sqrHght, 13,26);

    setFocusable(true);
    requestFocusInWindow();
    addKeyListener(this);
  }



  public void setWin() {
    gameStatus = 1;
    repaint();
  }

  public void setLoss() {
    gameStatus = 2;
    repaint();
  }


  public void keyPressed(KeyEvent e) {
    //System.out.println("keyPressed");
  }
  public void keyReleased(KeyEvent e) {
    //System.out.println("keyReleased");
  }
  public void keyTyped(KeyEvent e) {
    char key = e.getKeyChar();
    //System.out.println(key);

    switch (key) {
      case 'w':
        currentKey = NORTH;
        break;
      case 's':
        currentKey = SOUTH;
        break;
      case 'a':
        currentKey = WEST;
        break;
      case 'd':
        currentKey = EAST;
        break;
    }
  }
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    for (int y = 0; y < mundo.height; y++) {
      for (int x = 0; x < mundo.width; x++) {
        if (mundo.grid[x][y] == 1) {
          g2d.setColor(Color.black);
          g2d.fillRect((int)(x * sqrWdth), (int)(y * sqrHght), (int)sqrWdth, (int)sqrHght);
        }
        else if (mundo.grid[x][y] == 0) {
          g2d.setColor(myWhite);
          g2d.fillRect((int)(x * sqrWdth), (int)(y * sqrHght), (int)sqrWdth, (int)sqrHght);
        }
        else if (mundo.grid[x][y] == 2) {
          g2d.setColor(Color.black);
          g2d.fillRect((int)(x * sqrWdth) + (int) (sqrWdth / 4), (int)(y * sqrHght ) + (int) (sqrHght / 4), (int)(sqrWdth / 2), (int)(sqrHght / 2));
        }
        else if (mundo.grid[x][y] == 3) {
          pacman.drawModel(g2d, (int) (x * sqrWdth), (int) (y * sqrHght));
        }
        else if (mundo.grid[x][y] == 6) {
          g2d.setColor(Color.red);
          g2d.fillRect((int)(x * sqrWdth), (int)(y * sqrHght), (int)sqrWdth, (int)sqrHght);
        }
        else if (mundo.grid[x][y] == 7) {
          g2d.setColor(Color.pink);
          g2d.fillRect((int)(x * sqrWdth), (int)(y * sqrHght), (int)sqrWdth, (int)sqrHght);
        }
        else if (mundo.grid[x][y] == 8) {
          g2d.setColor(Color.blue);
          g2d.fillRect((int)(x * sqrWdth), (int)(y * sqrHght), (int)sqrWdth, (int)sqrHght);
        }
        else if (mundo.grid[x][y] == 9) {
          g2d.setColor(Color.orange);
          g2d.fillRect((int)(x * sqrWdth), (int)(y * sqrHght), (int)sqrWdth, (int)sqrHght);
        }
      }
      if (y != 0) {
        g.setColor(gris);
        g.drawLine(0, (int)(y * sqrHght), (int)winWidth, (int)(y * sqrHght));
      }
    }
    for (int x = 0; x < mundo.width; x++) {
      g.setColor(gris);
      g.drawLine((int)(x * sqrWdth), 0, (int)(x * sqrWdth), (int)winHeight);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }
}


// This is the main class that you will add to in order to complete the lab
public class PacMan extends JFrame {
  // Mapping of actions to integers
  public static final int NORTH = 0;
  public static final int SOUTH = 1;
  public static final int EAST = 2;
  public static final int WEST = 3;

  Color bkgroundColor = new Color(230,230,230);

  static mySmartMap myMaps; // instance of the class that draw everything to the GUI
  String mundoName;

  World mundo; // mundo contains all the information about the world.  See World.java
  // and the probability that a sonar reading is correct, respectively

  // variables to communicate with the Server via sockets
  public Socket s;
  public BufferedReader sin;
  public PrintWriter sout;

  // variables to store information entered through the command-line about the current scenario
  int startX = -1, startY = -1;




  public PacMan() {

    initClient();

    // Read in the world
    mundo = new World(mundoName);

    // set up the GUI that displays the information you compute
    int width = 500;
    int height = 500;
    int bar = 20;
    setSize(width,height+bar);
    getContentPane().setBackground(bkgroundColor);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(0, 0, width, height+bar);
    myMaps = new mySmartMap(width, height, mundo);
    getContentPane().add(myMaps);

    setVisible(true);
    setTitle("PacMan");

    doStuff(); // Function to have the robot move about its world until it gets to its goal or falls in a stairwell
  }

  // this function establishes a connection with the server and learns
  //   1 -- which world it is in
  //   2 -- it's transition model (specified by moveProb)
  //   3 -- it's sensor model (specified by sensorAccuracy)
  //   4 -- whether it's initial position is known.  if known, its position is stored in (startX, startY)
  public void initClient() {
    int portNumber = 3333;
    String host = "localhost";

    try {
      s = new Socket(host, portNumber);
      sout = new PrintWriter(s.getOutputStream(), true);
      sin = new BufferedReader(new InputStreamReader(s.getInputStream()));

      mundoName = sin.readLine();
      System.out.println("Need to open the mundo: " + mundoName);

      // find out of the robots position is know
    } catch (IOException e) {
      System.err.println("Caught IOException: " + e.getMessage());
    }
  }

  // function that gets human-specified actions
  // 'w' specifies the movement up
  // 's' specifies the movement down
  // 'd' specifies the movement right
  // 'a' specifies the movement left

  int getHumanAction() {
    System.out.println("Reading the action selected by the user");
    while (myMaps.currentKey < 0) {
      try {
        Thread.sleep(50);
      }
      catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
    int a = myMaps.currentKey;
    myMaps.currentKey = -1;

    System.out.println("Action: " + a);

    return a;
  }

  void doStuff() {
    int action;

    while (true) {
      try {
        action = getHumanAction();  // get the action selected by the user (from the keyboard
        sout.println(action); // send the action to the Server

        String sonars = sin.readLine();
        //System.out.println("Sonars: " + sonars);

        if (sonars != null){
          System.out.println(sonars);
          myMaps.setWin();
          break;
        }
      }
      catch (IOException e) {
        System.out.println(e);
      }
    }
  }

  // java theRobot [manual/automatic] [delay]
  public static void main(String[] args) {
    PacMan robot = new PacMan();  // starts up pac man
  }
}

