package imgs.ghosts;

import imgs.Model;
import imgs.PacmanModel;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Ghost extends Model {
  static class Node {
    int x, y;
    Node parent;

    Node(int x, int y, Node parent) {
      this.x = x;
      this.y = y;
      this.parent = parent;
    }
  }
  private static final int[][] DIRECTIONS = {
          {-1, 0}, {1, 0}, {0, -1}, {0, 1}
  };
  protected Color color;
  protected int targetX;
  protected int targetY;
  protected final int[][] pathingGrid;
  protected PacmanModel pacman;
  private Stack<int[]> nextMoves;

  protected Ghost(int[][] grid, double height, double width, int x, int y, PacmanModel pacman){
    super(height,width,x,y);
    this.pathingGrid = grid;
    this.pacman = pacman;
    this.nextMoves = new Stack<>();
  }

  @Override
  public void draw(Graphics2D g2d, int x, int y){
    g2d.setColor(this.color);
    g2d.fillRect(x,y, this.height, this.width);
  }

  @Override
  public void move(int[][] grid){
    this.setMove();
    super.move(grid);
    if (this.lastx != this.prevx || this.lasty != this.prevy){
      grid[this.prevx][this.prevy] = this.prevVal;
      this.prevVal = this.nextPrevVal;
    }
  }

  protected void setMove(){
    if(this.nextMoves.empty()){
      reAdjust();
      this.direction = convertVectorToInt(this.nextMoves.pop());
    }
    else{
      this.direction = convertVectorToInt(this.nextMoves.pop());
    }
  }

  private int convertVectorToInt(int[] dir){
    int dy = dir[1];
    int dx = dir[0];

    if (dy - this.lasty == -1 && dx - this.lastx == 0) return 0; // Up
    if (dy - this.lasty == 1 && dx - this.lastx == 0)  return 1; // Down
    if (dy - this.lasty  == 0 && dx - this.lastx == -1) return 3; // Left
    if (dy - this.lasty == 0 && dx - this.lastx == 1)  return 2; // Right
    return 0;
  }

  protected void reAdjust(){
    setTarget();
    this.nextMoves =  bfs();
  }

  protected void setTarget(){}

  Stack<int[]> bfs(){
    int rows = pathingGrid.length;
    int cols = pathingGrid[0].length;

    boolean[][] visited = new boolean[rows][cols];
    Queue<Node> queue = new LinkedList<>();

    queue.add(new Node(this.lastx, this.lasty, null));
    visited[this.lastx][this.lasty] = true;

    while (!queue.isEmpty()) {
      Node current = queue.poll();

      // Reached target
      if (current.x == targetX && current.y == targetY) {
        return reconstructPath(current);
      }

      for (int[] dir : DIRECTIONS) {
        int newX = current.x + dir[0];
        int newY = current.y + dir[1];

        if (isInBounds(newX, newY, rows, cols) &&
                pathingGrid[newX][newY] == 0 && !visited[newX][newY]) {

          visited[newX][newY] = true;
          queue.add(new Node(newX, newY, current));
        }
      }
    }

    // No path found
    return null;
  }

  // Backtrack to get the first step
  private Stack<int[]> reconstructPath(Node targetNode) {
    Node current = targetNode;
    Stack<int[]> returnStack = new Stack<>();

    // Go backward to the first move from start
    while (current.parent != null) {
      if(isInBounds(current.x, current.y, pathingGrid.length, pathingGrid[0].length) && pathingGrid[current.x][current.y] != 1){
        returnStack.push(new int[]{current.x, current.y});
      }

      current = current.parent;
    }

    return returnStack;
  }

  private boolean isInBounds(int x, int y, int rows, int cols) {
    return x >= 0 && y >= 0 && x < rows && y < cols;
  }
}
