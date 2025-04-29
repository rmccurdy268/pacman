package Server;
import imgs.Model;
import imgs.PacmanModel;
import imgs.ghosts.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.lang.*;
import javax.swing.*;
import java.io.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class MyCanvas extends JComponent implements KeyListener {
  int winWidth, winHeight;
  double sqrWdth, sqrHght;
  Color gris = new Color(170,170,170);
  Color myWhite = new Color(220, 220, 220);
  int inputTrigger;
  Set<Model> movables;
  PacmanModel pacman;
  int pointsOnBoard;


  public static final int NORTH = 0;
  public static final int SOUTH = 1;
  public static final int EAST = 2;
  public static final int WEST = 3;

  int xpos, ypos;
  boolean pointsFound = false;
  World mundo;
  int gameStatus;
  Map<String, Model> ghostMap = new HashMap<>();
  public MyCanvas(int w, int h, World wld, int _x, int _y) {
    mundo = wld;
    winWidth = w;
    winHeight = h;
    pointsOnBoard = 0;

    gameStatus = 0;
    sqrWdth = (double)w / mundo.width;
    sqrHght = (double)h / mundo.height;



    movables = new HashSet<>();
    pacman = new PacmanModel(sqrWdth, sqrHght, 13,26);
    ghostMap.put("red", new RedGhost(mundo.pathingGrid, sqrWdth, sqrHght, 11,14, pacman));
    ghostMap.put("pink", new PinkGhost(mundo.pathingGrid, sqrWdth, sqrHght, 13,14, pacman));
    ghostMap.put("orange", new OrangeGhost(mundo.pathingGrid, sqrWdth, sqrHght, 15,14, pacman));
    ghostMap.put("blue", new BlueGhost(mundo.pathingGrid, sqrWdth, sqrHght, 12, 14, pacman, ghostMap.get("red")));

    movables.add(pacman);
    movables.addAll(ghostMap.values());

    updatePositions();

    inputTrigger = 0;
    addKeyListener(this);
  }

  public void updatePositions() {
    for (Model obj: movables){
      mundo.grid[obj.getX()][obj.getY()] = obj.gridID;
    }
    repaint();
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
          if(!pointsFound){
            pointsOnBoard++;
          }
          g2d.setColor(Color.black);
          g2d.fillRect((int)(x * sqrWdth) + (int) (sqrWdth / 4), (int)(y * sqrHght ) + (int) (sqrHght / 4), (int)(sqrWdth / 2), (int)(sqrHght / 2));
        }
        else if (mundo.grid[x][y] == 3) {
          this.pacman.drawModel(g2d, (int) (x * sqrWdth), (int) (y * sqrHght));
        }
        else if (mundo.grid[x][y] == 6) {
          this.ghostMap.get("red").drawModel(g2d, (int) (x * sqrWdth), (int) (y * sqrHght));
        }
        else if (mundo.grid[x][y] == 7) {
          this.ghostMap.get("pink").drawModel(g2d, (int) (x * sqrWdth), (int) (y * sqrHght));
        }
        else if (mundo.grid[x][y] == 8) {
          this.ghostMap.get("blue").drawModel(g2d, (int) (x * sqrWdth), (int) (y * sqrHght));
        }
        else if (mundo.grid[x][y] == 9) {
          this.ghostMap.get("orange").drawModel(g2d, (int) (x * sqrWdth), (int) (y * sqrHght));
        }
      }
      if (y != 0) {
        g2d.setColor(gris);
        g2d.drawLine(0, (int)(y * sqrHght), (int)winWidth, (int)(y * sqrHght));
      }
    }
    for (int x = 0; x < mundo.width; x++) {
      g2d.setColor(gris);
      g2d.drawLine((int)(x * sqrWdth), 0, (int)(x * sqrWdth), (int)winHeight);
    }
    g2d.setColor(Color.green);


    if (gameStatus == 1) {
      g2d.setColor(Color.green);
      g2d.drawString("You Won!", 8, 25);
    }
    else if (gameStatus == 2) {
      g2d.setColor(Color.red);
      g2d.drawString("You're a Loser!", 8, 25);
    }
    pointsFound = true;
  }

  public void setWin(){
    gameStatus = 1;
    repaint();
  }

  public void setLoss(){
    gameStatus = 2;
    repaint();
  }

  public void addNotify() {
    super.addNotify();
    requestFocus();
  }

  public void keyPressed(KeyEvent e) {
    //donothing
  }
  public void keyReleased(KeyEvent e) {
    int input;
    switch (e.getKeyCode()) {
      case 38:
        input = NORTH;
        break;
      case 40:
        input = SOUTH;
        break;
      case 37:
        input = WEST;
        break;
      case 39:
        input = EAST;
        break;
      default:
        input = -1;
    }
    if(input >=0){
      this.pacman.orient(input);
    }
  }
  public void keyTyped(KeyEvent e) {
      // mm yes do nothing
  }
}

public class PacManWorld extends JFrame {
  public static final int NORTH = 0;
  public static final int SOUTH = 1;
  public static final int EAST = 2;
  public static final int WEST = 3;

  Color bkgroundColor = new Color(230,230,230);
  static MyCanvas canvas;
  World mundo;
  int xpos, ypos;
  int pacManPoints;

  int currDirection;
  Timer pacmanTimer;
  Timer ghostTimer;

  BufferedReader sin;

  public PacManWorld(String worldName) {

    mundo = new World(worldName);
    int width = 500;
    int height = 500;
    pacManPoints = 0;
    currDirection = 0;
    initPacManPosition();

    int bar = 20;
    setSize(width,height+bar);
    getContentPane().setBackground(bkgroundColor);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(0, 0, width, height+bar);
    canvas = new MyCanvas(width, height, mundo, xpos, ypos);
    getContentPane().add(canvas);

    setVisible(true);
    setTitle("PacManWorld");

    getConnection();
    survive();
  }

  private void getConnection() {
    sin = new BufferedReader(new InputStreamReader(System.in));
  }

  void initPacManPosition() {
      xpos = 13;
      ypos = 26;
 }

  void survive() {
    pacmanTimer = new Timer(150, e -> {
      canvas.pacman.move(mundo.grid);
      if(checkCollision()){
        canvas.setLoss();
        stopTimers();
      }
      canvas.updatePositions();
      if(checkWin()){
        canvas.setWin();
        stopTimers();
      }
    });

    ghostTimer = new Timer(300, e -> {
      for (Model ghost: canvas.ghostMap.values()){
        ghost.move(mundo.grid);
      }
      if(checkCollision()){
        canvas.setLoss();
        stopTimers();
      }
      canvas.updatePositions();
      if(checkWin()){
        canvas.setWin();
        stopTimers();
      }
    });
    pacmanTimer.start();
    ghostTimer.start();
  }


  void stopTimers(){
    pacmanTimer.stop();
    ghostTimer.stop();
  }

  public boolean checkWin(){
    return canvas.pointsOnBoard == canvas.pacman.points;
  }

  public boolean checkCollision(){
    for (Model ghost: canvas.ghostMap.values()){
      if (ghost.getX() == canvas.pacman.getX() && ghost.getY() == canvas.pacman.getY()){
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) {
    PacManWorld myWorld = new PacManWorld(args[0]);
  }
}
