package imgs.ghosts;

import imgs.Model;
import imgs.PacmanModel;

import java.awt.*;

public class PinkGhost extends Ghost{

  public PinkGhost(int[][] grid, double height, double width, int x, int y, PacmanModel pacman){
    super(grid, height, width, x, y, pacman);
    this.color = Color.pink;
    this.gridID = 7;
  }

  @Override
  public void setTarget(){
    int pcDirection = this.pacman.getDirection();
    int pacX = this.pacman.getX();
    int pacY = this.pacman.getY();
    tryToSetInFront(4, pcDirection,pacX,pacY);
  }
  private void tryToSetInFront(int dist, int direction, int pacX, int pacY){
    if(direction == 0){
      if(isInBounds(pacX,pacY - dist)) {
        this.targetY=pacY - dist;
        this.targetX=pacX;
        return;
      }
    }
    if(direction == 1){
      if(isInBounds(pacX,pacY + dist)){
        this.targetY = pacY + dist;
        this.targetX = pacX;
        return;
      }
    }
    if(direction == 3){
      if(isInBounds(pacX - dist,pacY)){
        this.targetY = pacY;
        this.targetX = pacX - dist;
        return;
      }
    }
    if(direction == 2){
      if(isInBounds(pacX + dist,pacY)){
        this.targetY = pacY;
        this.targetX = pacX + dist;
        return;
      }
    }
    tryToSetInFront(dist-1, direction,pacX,pacY);
  }

  private boolean isInBounds(int x, int y){
    return this.pathingGrid[x][y] != 1;
  }
}