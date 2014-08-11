package sopa;

/**
 * David Schilling - davejs92@gmail.com
 */
public class GameField {

    Tile[][] field;
    private PathState[][] pathStates;
    private int[] directionsX = new int[]{0, 1, 0, -1};
    private int[] directionsY = new int[]{1, 0, -1, 0};


    public GameField(Tile[][] field) {
        this.field = field;
        pathStates = new PathState[field.length][field[0].length];
    }

    public boolean solvedPuzzle() {
        initializePathStates();
        return searchFinish(0, 1);
    }

    private void initializePathStates() {
        for(int i = 0; i < pathStates.length; i++){
            for(int j = 0; j < pathStates[0].length; j++) {
                pathStates[i][j] = PathState.UNDEFINED;
            }
        }
    }

    public boolean searchFinish(int x, int y){
        for(int direction = 0; direction < 4; direction++){
            int xNew = x + directionsX[direction];
            int yNew = y + directionsY[direction];
            if(possibleTile(x, y ,xNew, yNew, direction)) {
                extendSolution(xNew, yNew);
                if(!foundFinish(xNew, yNew)){
                    if(searchFinish(xNew, yNew)) {
                        return true;
                    } else {
                        markAsImpossible(xNew, yNew);
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean possibleTile(int x, int y, int xNew, int yNew, int direction) {
        if(xNew >= 0 && xNew < field[0].length && yNew >= 0 && yNew < field.length) {
            Tile tileNew = field[xNew][yNew];
            Tile tile = field[x][y];
            if(tileNew.getTileType() != TileType.NONE && pathStates[xNew][yNew] == PathState.UNDEFINED){
                if(direction == 0) {
                    if (tile.isBottom() && tileNew.isTop()) {
                        return true;
                    }
                } else if(direction == 1){
                    if(tile.isRight() && tileNew.isLeft()) {
                        return true;
                    }
                } else if (direction == 2) {
                    if(tile.isTop() && tileNew.isBottom()) {
                        return true;
                    }
                } else {
                    if(tile.isLeft() && tile.isRight()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void extendSolution(int xNew, int yNew) {
        pathStates[xNew][yNew] = PathState.POSSIBLE;
    }

    private boolean foundFinish(int xNew, int yNew) {
        if(field[xNew][yNew].getTileType() == TileType.FINISH) {
            return true;
        }
        return false;
    }

    private void markAsImpossible(int xNew, int yNew) {
        pathStates[xNew][yNew] = PathState.IMPOSSIBLE;
    }

    public void printBacktracking(){
        System.out.println("Backtracking Result :");
        for (int i = 0; i < pathStates[0].length; i++){
            for(int j = 0; j < pathStates.length; j++){
                System.out.print(pathStates[j][i] + "\t");
            }
            System.out.println();
        }
    }
    public void printField(){
        System.out.println("Field:");
        for(int i = 0; i < field[0].length; i++){
            for(int j = 0; j < field.length; j++){
                System.out.print(field[j][i].getTileType() + "\t");
            }
            System.out.println();
        }
    }

    public void shiftLine(boolean horizontal, int row, int steps) {
        if(horizontal) {
            Tile line[] = new Tile[field.length-2];
            for(int i = 0; i < field.length - 2; i++) {
                line[i] = field[i+1][row+1];
            }
            for(int i = 0; i < field.length-2; i++) {
                int newPosition = i + steps;
                newPosition = shiftToPositive(newPosition,field.length-2);
                newPosition = newPosition%(field.length-2);
                field[newPosition+1][row+1] = line[i];
            }
        } else {
            Tile line[] = new Tile[field[0].length-2];
            for(int i = 0; i < field[0].length-2; i++) {
                line[i] = field[row+1][i+1];
            }
            for(int i = 0; i < field[0].length-2; i++) {
                int newPosition = (i+steps);
                newPosition = shiftToPositive(newPosition,field[0].length-2);
                newPosition = newPosition%(field[0].length-2);
                field[row+1][newPosition+1] = line[i];
            }
        }
    }

    public void setField(Tile[][] field) {
        this.field = field;
    }

    public Tile[][] getField() {
        return field;
    }

    private int shiftToPositive(int number, int steps) {
        int shifted = number;
        while (shifted < 0) {
            shifted = shifted + steps;
        }
        return shifted;
    }
}