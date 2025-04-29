package imgs.ghosts;

import imgs.PacmanModel;

import java.awt.*;

public class OrangeGhost extends Ghost{

  public OrangeGhost(int[][] grid, double height, double width, int x, int y, PacmanModel pacman){
    super(grid, height, width, x, y, pacman);
    this.color = Color.orange;
    this.gridID = 9;
  }

  @Override
  public void setTarget(){
    int xdist = Math.abs(this.lastx - this.pacman.getX());
    int ydist = Math.abs(this.lasty - this.pacman.getY());
    if(xdist > 8 || ydist > 8){
      targetX = this.pacman.getX();
      targetY = this.pacman.getY();
    }
    else{
      targetX = 1;
      targetY = 32;
    }
  }
}
