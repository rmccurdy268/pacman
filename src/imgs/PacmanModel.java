package imgs;
import java.awt.*;

public class PacmanModel extends Model{
  public int points;
  public PacmanModel(double height, double width, int x, int y){
    super(height,width,x,y);
    this.gridID = 3;
    this.points = 0;
  }

  @Override
  public void draw(Graphics2D g2d, int x, int y) {
    int startAngle = 30;
    switch(this.direction){
      case 0:
        startAngle = 120;
        break;
      case 1:
        startAngle = 300;
        break;
      case 3:
        startAngle = 210;
        break;
      case 2:
        startAngle = 30;
        break;
    }
    g2d.setColor(Color.black);
    g2d.fillRect(x, y, this.height, this.width);
    g2d.setColor(Color.yellow);
    g2d.fillArc(x, y, this.height, this.width, startAngle, 300);
  }

  @Override
  public void move(int[][] grid){
    super.move(grid);
    if (grid[this.lastx][this.lasty] == 2){
      this.points++;
    }
    if (this.lastx != this.prevx || this.lasty != this.prevy){
      grid[this.prevx][this.prevy] = 0;
    }
  }

  public int getDirection(){
    return this.direction;
  }
}
