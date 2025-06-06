package PacMan;
import java.lang.*;
import java.io.*;

public class World {
  int width, height;
  int[][] grid;

  World(String worldFile) {
    try {
      FileReader fileReader = new FileReader("../worlds/" + worldFile);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      width = Integer.parseInt(bufferedReader.readLine());
      height = Integer.parseInt(bufferedReader.readLine());

      grid = new int[width][height];
      for (int y = 0; y < height; y++) {
        String line = bufferedReader.readLine();
        for (int x = 0; x < width; x++) {
          if (line.charAt(x) == '0')
            grid[x][y] = 0;
          else if (line.charAt(x) == '1')
            grid[x][y] = 1;
          else if (line.charAt(x) == '-')
            grid[x][y] = 2;
          else if (line.charAt(x) == '<')
            grid[x][y] = 3;
          else if (line.charAt(x) == 'R')
            grid[x][y] = 6;
          else if (line.charAt(x) == 'P')
            grid[x][y] = 7;
          else if (line.charAt(x) == 'B')
            grid[x][y] = 8;
          else if (line.charAt(x) == 'O')
            grid[x][y] = 9;
        }
      }

      bufferedReader.close();
      fileReader.close();
    }
    catch(IOException e) {
      System.out.println(e);
    }
  }

}
