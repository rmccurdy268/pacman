package imgs;

import java.awt.*;

public abstract class Model {
  protected int width;
  protected int height;
  protected int direction;
  protected int lastx;
  protected int lasty;
  protected int prevx;
  protected int prevy;
  public int gridID;
  public int moveCount;
  protected int prevVal = 0;
  protected int nextPrevVal = 0;


  // model class for characters
  protected Model(double height, double width, int x, int y){
    this.width = (int) width;
    this.height = (int) height;
    this.lastx = x;
    this.lasty = y;
    this.direction = 3;
    this.moveCount = 0;
  }

  // paints - delegated to subclass
  public void drawModel(Graphics2D g2d, int x, int y){
    this.draw(g2d, x, y);
  }

  public void move(int[][] grid){
    this.prevx = this.lastx;
    this.prevy = this.lasty;
    switch(this.direction){
      case 0:
        this.prevVal = grid[lastx][lasty-1];
        this.setY(this.lasty-1);
        break;
      case 1:
        this.prevVal = grid[lastx][lasty+1];
        this.setY(this.lasty+1);
        break;
      case 3:
        this.prevVal = grid[lastx-1][lasty];
        this.setX(this.lastx-1);
        break;
      case 2:
        this.prevVal = grid[lastx+1][lasty];
        this.setX(this.lastx+1);
        break;
      default:
        break;
    }
    if(grid[this.lastx][this.lasty] == 1){
      this.prevVal = gridID;
      this.setX(prevx);
      this.setY(prevy);
    }
  }

  public void orient(int direction){
    this.direction = direction;
  }

  public int getX(){
    return this.lastx;
  }
  public void setX(int x){
    this.lastx = x;
  }

  public int getY(){
    return this.lasty;
  }
  public void setY(int y){
    this.lasty = y;
  }

  public void draw(Graphics2D g2d, int x, int y){}
}
