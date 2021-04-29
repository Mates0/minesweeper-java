package cz.educanet.minesweeper.logic;

import java.util.Random;

public class Minesweeper {

    private int rowsCount;
    private int columnsCount;
    private int oneblock;
    private Area[][] field;
    private int bombs = 20;
    private boolean clicked = false;
    private int flags = bombs;


    public Minesweeper(int rows, int columns) {
        field = new Area[columns][rows];
        this.rowsCount = rows;
        this.columnsCount = columns;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                field[i][j] = new Area();
            }
        }
        oneblock = (rows * columns) - bombs;
        makeFields(rows, columns);
    }

    /**
     * 0 - Hidden
     * 1 - Visible
     * 2 - Flag
     * 3 - Question mark
     *
     * @param x X
     * @param y Y
     * @return field type
     */
    public int getField(int x, int y) {
        return field[x][y].getState();
    }

    /**
     * Toggles the field state, ie.
     * 0 -> 1,
     * 1 -> 2,
     * 2 -> 3 and
     * 3 -> 0
     *
     * @param x X
     * @param y Y
     */
    public void toggleFieldState(int x, int y) {
        if (field[x][y].getState() != 2){
            field[x][y].setState(2);
        }
        else {
            field[x][y].setState(0);
        }
        flags--;
    }

    public void makeFields(int rows, int columns) {
        int counter = 0;
        Random rd = new Random();
        while (bombs != counter) {
            int x = rd.nextInt(columns);
            int y = rd.nextInt(rows);
            while (field[x][y].getBomb()) {
                x = rd.nextInt(columns);
                y = rd.nextInt(rows);
            }
            field[x][y].setBomb(true);
            counter++;
        }
    }

    /**
     * Reveals the field and all fields adjacent (with 0 adjacent bombs) and all fields adjacent to the adjacent fields... ect.
     *
     * @param x X
     * @param y Y
     */
    public void reveal(int x, int y) {
        if (field[x][y].getBomb()) {
            clicked = true;
        }
        field[x][y].setState(1);

        if (getAdjacentBombCount(x, y) == 0) {
            boolean topleft = x != 0 && y != 0;
            boolean topright = x != columnsCount - 1 && y != 0;
            boolean bottomleft = x != 0 && y != rowsCount - 1;
            boolean bottomright = x != columnsCount - 1 && y != rowsCount - 1;
            if (bottomleft && !field[x - 1][y + 1].getBomb() && field[x - 1][y + 1].getState() != 1){
                oneblock--;
                reveal(x - 1, y + 1);
            }
            if (bottomleft || topleft)
                if (!field[x - 1][y].getBomb() && field[x - 1][y].getState() != 1) {
                    oneblock--;
                    reveal(x - 1, y);
                }

            if (bottomright && !field[x + 1][y + 1].getBomb() && field[x + 1][y + 1].getState() != 1){
                oneblock--;
                reveal(x + 1, y + 1);
            }
            if (bottomright || bottomleft)
                if (!field[x][y + 1].getBomb() && field[x][y + 1].getState() != 1) {
                    oneblock--;
                    reveal(x, y + 1);
                }

            if (topleft && !field[x - 1][y - 1].getBomb() && field[x - 1][y - 1].getState() != 1){
                oneblock--;
                reveal(x - 1, y - 1);
            }

            if (topleft || topright)
                if (!field[x][y - 1].getBomb() && field[x][y - 1].getState() != 1) {
                    oneblock--;
                    reveal(x, y - 1);
                }

            if (topright && !field[x + 1][y - 1].getBomb() && field[x + 1][y - 1].getState() != 1){
                oneblock--;
                reveal(x + 1, y - 1);
            }
            if (topright || bottomright)
                if (!field[x + 1][y].getBomb() && field[x + 1][y].getState() != 1) {
                    oneblock--;
                    reveal(x + 1, y);
                }
        }
        oneblock--;
    }

    /**
     * Returns the amount of adjacent bombs
     *
     * @param x X
     * @param y Y
     * @return number of adjacent bombs
     */
    public int getAdjacentBombCount(int x, int y) {
        int bombs = 0;

        boolean topleft = x != 0 && y != 0;
        boolean topright = x != columnsCount - 1 && y != 0;
        boolean bottomleft = x != 0 && y != rowsCount - 1;
        boolean bottomright = x != columnsCount - 1 && y != rowsCount - 1;

        if (bottomleft && field[x][y + 1].getBomb() || bottomright && field[x][y + 1].getBomb())
            bombs++;
        if (bottomleft && field[x - 1][y + 1].getBomb())
            bombs++;
        if (bottomleft && field[x - 1][y].getBomb() || topleft && field[x - 1][y].getBomb())
            bombs++;
        if (topleft && field[x - 1][y - 1].getBomb())
            bombs++;
        if (topleft && field[x][y - 1].getBomb() || topright && field[x][y - 1].getBomb())
            bombs++;
        if (topright && field[x + 1][y - 1].getBomb())
            bombs++;
        if (topright && field[x + 1][y].getBomb() || bottomright && field[x + 1][y].getBomb())
            bombs++;
        if (bottomright && field[x + 1][y + 1].getBomb())
            bombs++;

        return bombs;
    }

    /**
     * returns true if every flag is on a bomb, else false
     *
     * @return if player won
     */
    public boolean didWin() {
        return oneblock == 0;
    }

    /**
     * returns true if player revealed a bomb, else false
     *
     * @return if player lost
     */
    public boolean didLoose() {
        return clicked;
    }

    public int getRows() {
        return rowsCount;
    }

    public int getColumns() {
        return columnsCount;
    }
}