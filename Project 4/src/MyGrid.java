import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import java.util.*;
/**
 * Updated by Yuha Yoo and Austin Franzen 3.3.2022
 * Written by Cormac Pearce on 11.10.2021
 * Significant portions of code taken from Noah Park's Spring 2021 solution
 */
public class MyGrid {

    Cell[][] grid;
    int rows, cols, startRow, endRow;

    public MyGrid(int r, int c, int start, int end) {//constructor which initializes grid, startRow, endRow rows and cols
        grid = new Cell[r][c];
        startRow = start;
        endRow = end;
        rows = r;
        cols = c;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                grid[i][j] = new Cell();
            }
        }
    }

    /**
     * Draws grid and displays to the user.
     *
     */
    public void drawGrid() {
        int y = 0;
        Canvas canvas = new Canvas();
        for (int i = 0; i < grid.length * 2 + 1; i++){
            int x = 0;
            y += 27; // y increases by 27 instead of 25 so that there is space between the squares
            for (int j = 0; j < grid[0].length * 2 + 1; j++) {
                if (i % 2 != 0 && ((i-1)/2) == startRow && j == 0) {
                    Square square = new Square(x, y, 25, 25, Color.green); // if statements sets the start square to green
                    canvas.drawShape(square);
                    x += 27;
                }
                else if (i % 2 != 0 && ((i-1)/2) == endRow && j == grid[0].length * 2) { // if statement sets the end square to green
                    Square square = new Square(x, y, 25, 25, Color.green);
                    canvas.drawShape(square);
                    x += 27;
                }
                else if (i % 2 != 0 && j % 2 != 0) { // this if statement checks odd row and col numbers and sets those automatically to darkGray or cyan if the square is visited when you call solveGrid on it
                    if (grid[(i-1)/2][(j-1)/2].getVisited()) {
                        Square square = new Square(x, y, 25, 25, Color.cyan);
                        canvas.drawShape(square);
                        x += 27;
                    }
                    else{
                        Square square = new Square(x, y, 25, 25, Color.darkGray);
                        canvas.drawShape(square);
                        x += 27;
                    }
                }
                else if (i % 2 == 0 && j % 2 == 0) { // this if statement sets all even by even rows and cols to black because they are always walls
                    Square square = new Square(x, y, 25, 25, Color.black);
                    canvas.drawShape(square);
                    x += 27;
                }
                else if (i % 2 == 0 && i > 0) { // this if statement checks even rows and odd cols
                    if (!grid[(i-2)/2][(j-1)/2].getBottom()) {// checks if bottom of cell above the current cell is open
                        if (grid[(i-2)/2][(j-1)/2].getVisited()) { // checks if square is visited
                            Square square = new Square(x, y, 25, 25, Color.cyan);
                            canvas.drawShape(square);
                            x += 27;
                        }
                        else{
                            Square square = new Square(x, y, 25, 25, Color.darkGray);
                            canvas.drawShape(square);
                            x += 27;
                        }
                    }
                    else {
                        Square square = new Square(x, y, 25, 25, Color.black);
                        canvas.drawShape(square);
                        x += 27;
                    }
                }
                else if (j % 2 == 0 && j > 0) { // this if statement checks odd rows and even cols
                    if (!grid[(i-1)/2][(j-2)/2].getRight()) { // checks if the square to the left of the current cell has an open right wall
                        if (grid[(i-1)/2][(j-2)/2].getVisited()) {
                            Square square = new Square(x, y, 25, 25, Color.cyan);
                            canvas.drawShape(square);
                            x += 27;
                        }
                        else{
                            Square square = new Square(x, y, 25, 25, Color.darkGray);
                            canvas.drawShape(square);
                            x += 27;
                        }
                    }
                    else {
                        Square square = new Square(x, y, 25, 25, Color.black);
                        canvas.drawShape(square);
                        x += 27;
                    }
                }
                else {// final statement that sets the remaining squares to black
                    Square square = new Square(x, y, 25, 25, Color.black);
                    canvas.drawShape(square);
                    x += 27;
                }
            }
        }
    }

    /**
     * Generates a random maze using the algorithm from the write up.
     *
     * @param level difficulty level for maze (1-3) that decides maze dimensions
     *              level 1 -> 5x5, level 2 -> 5x20, level 3 -> 20x20
     * @return MyMaze object fully generated.
     */
    public static MyGrid makeGrid(int level) {
        MyGrid myMaze;
        Random rand = new Random();
        int randInt = 0;
        Stack1Gen<int[]> stack = new Stack1Gen<>(); // creates the stack
        if (level == 1){ // creates grid based on level entered
            myMaze = new MyGrid(5, 5, rand.nextInt(5), rand.nextInt(5));
        }
        else if (level == 2){
            myMaze = new MyGrid(5, 12, rand.nextInt(5), rand.nextInt(5));
        }
        else{
            myMaze = new MyGrid(12, 12, rand.nextInt(12), rand.nextInt(12));
        }
        int[] holder = new int[2];
        holder[0] = myMaze.startRow;
        holder[1] = 0;
        stack.push(holder);
        myMaze.grid[myMaze.startRow][0].setVisited(true);
        while (stack.top() != null){
            int[][] temp = new int[4][2];
            int counter = 0;// keeps track of how many directions are possible
            int[] array = stack.top();
            myMaze.grid[array[0]][array[1]].setVisited(true);
            // checks every direction to see if they are open
            if (array[0] + 1 < myMaze.grid.length && !myMaze.grid[array[0] + 1][array[1]].getVisited()){
                temp[counter][0] = array[0] + 1;
                temp[counter][1] = array[1];
                counter++;
            }
            if (array[0] - 1 >= 0 && !myMaze.grid[array[0] - 1][array[1]].getVisited()){
                temp[counter][0] = array[0] - 1;
                temp[counter][1] = array[1];
                counter++;
            }
            if (array[1] + 1 < myMaze.grid[0].length && !myMaze.grid[array[0]][array[1] + 1].getVisited()){
                temp[counter][0] = array[0];
                temp[counter][1] = array[1] + 1;
                counter++;
            }
            if (array[1] - 1 >= 0 && !myMaze.grid[array[0]][array[1] - 1].getVisited()){
                temp[counter][0] = array[0];
                temp[counter][1] = array[1] - 1;
                counter++;
            }
            if (counter == 0){
                stack.pop();
                continue;
            }
            randInt = rand.nextInt(counter);
            stack.push(new int[]{temp[randInt][0], temp[randInt][1]});
            // randomly chooses a direction to go
            if (temp[randInt][0] > array[0]){
                myMaze.grid[array[0]][array[1]].setBottom(false);
            }
            else if(temp[randInt][0] < array[0]){
                myMaze.grid[temp[randInt][0]][array[1]].setBottom(false);
            }
            else if(temp[randInt][1] < array[1]){
                myMaze.grid[array[0]][temp[randInt][1]].setRight(false);
            }
            else{
                myMaze.grid[array[0]][array[1]].setRight(false);
            }
        }
        for (int i = 0; i < myMaze.grid.length; i++){
            for (int j = 0; j < myMaze.grid[0].length; j++){
                myMaze.grid[i][j].setVisited(false);
            }
        }
        return myMaze;
    }

    /**
     * Solves the maze using the algorithm from the writeup.
     */
    public void solveGrid() {
        Q1Gen<int[]> queue = new Q1Gen<>();
        int[] holder = new int[2];
        holder[0] = startRow;
        holder[1] = 0;
        queue.add(holder);
        int[] array;
        while (queue.length() > 0){
            array = queue.remove();
            grid[array[0]][array[1]].setVisited(true);
            if (array[0] == endRow && array[1] == grid[0].length - 1){// breaks the loop if the end is reached
                break;
            }
            if(array[0] < grid.length - 1 && !grid[array[0] + 1][array[1]].getVisited() && !grid[array[0]][array[1]].getBottom()){
                queue.add(new int[]{array[0]+1,array[1]});
            }
            if(array[0] > 0 && !grid[array[0] - 1][array[1]].getVisited() && !grid[array[0] - 1][array[1]].getBottom()){
                queue.add(new int[]{array[0]-1,array[1]});
            }
            if(array[1] < grid[0].length - 1 && !grid[array[0]][array[1] + 1].getVisited() && !grid[array[0]][array[1]].getRight()){
                queue.add(new int[]{array[0],array[1] + 1});
            }
            if(array[1] > 0 && !grid[array[0]][array[1] - 1].getVisited() && !grid[array[0]][array[1] - 1].getRight()){
                queue.add(new int[]{array[0],array[1] - 1});
            }
        }
        drawGrid();
    }
    public static <T> void main(String[] args) {
        MyGrid grid;
        grid = makeGrid(3);
        grid.solveGrid();
    }
}