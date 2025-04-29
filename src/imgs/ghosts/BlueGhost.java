package imgs.ghosts;

import imgs.Model;
import imgs.PacmanModel;

import java.awt.*;

public class BlueGhost extends Ghost{
  Model blinky;
  public BlueGhost(int[][] grid, double height, double width, int x, int y, PacmanModel pacman, Model blinky){
    super(grid, height, width, x, y, pacman);
    this.color = Color.blue;
    this.gridID = 8;
    this.blinky = blinky;
  }

  @Override
  public void setTarget(){
    int[] blinkyVector = this.getBlinkyVector();
    if(isInBounds(blinky.getX() + blinkyVector[0]*2, blinky.getY() + blinkyVector[1]*2)){
      this.targetX = this.blinky.getX() + blinkyVector[0] * 2;
      this.targetY = this.blinky.getY() + blinkyVector[1] * 2;
    }
    else {
      this.targetX=this.pacman.getX();
      this.targetY=this.pacman.getY();
    }
  }

  public int[] getBlinkyVector(){
    return new int[]{this.pacman.getX() - this.blinky.getX(), this.pacman.getY() - this.blinky.getY()};
  }

  private boolean isInBounds(int x, int y){
    if(x < 0 || y < 0 || y > this.pathingGrid[0].length || x > this.pathingGrid.length){
      return false;
    }
    return this.pathingGrid[x][y] != 1;
  }
}