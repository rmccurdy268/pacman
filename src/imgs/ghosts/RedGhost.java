package imgs.ghosts;

import imgs.PacmanModel;

import java.awt.*;

public class RedGhost extends Ghost{

  public RedGhost(int[][] grid, double height, double width, int x, int y, PacmanModel pacman){
    super(grid, height, width, x, y, pacman);
    this.color = Color.red;
    this.gridID = 6;
  }

  @Override
  public void setTarget(){
    this.targetX = this.pacman.getX();
    this.targetY = this.pacman.getY();
  }
}
