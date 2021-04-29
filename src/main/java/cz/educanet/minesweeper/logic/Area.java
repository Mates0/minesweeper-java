package cz.educanet.minesweeper.logic;

public class Area {
    private int state;
    private boolean bomb;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }
}
