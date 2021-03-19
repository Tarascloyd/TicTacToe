import java.util.*;

abstract class AI {
    protected Board board;
    public abstract String nextMove();
    public abstract void printWho();
    public void setBoard(Board board) {
        this.board = board;
    }
}

class EasyAI extends AI {
    @Override
    public String nextMove() {
        Random random = new Random();
        String first = String.valueOf(random.nextInt(3)+1); 
        String second = String.valueOf(random.nextInt(3)+1);
        return first + second;          
    }
    @Override
    public void printWho() {       
        System.out.println("Making move level \"easy\"");
    }  
}
class MediumAI extends AI {
    @Override
    public String nextMove() {
        String forWin = board.analizeMoveForWin();
        if (!"".equals(forWin)) {
            return forWin;
        }
        String forBlock = board.analizeMoveForBlock();
        if (!"".equals(forBlock)) {
            return forBlock;
        }
        Random random = new Random();
        String first = String.valueOf(random.nextInt(3)+1); 
        String second = String.valueOf(random.nextInt(3)+1);
        return first + second;          
    }
    @Override
    public void printWho() {       
        System.out.println("Making move level \"medium\"");
    }  
}
class Game {
    private Board board;
    private AI ai1;
    private AI ai2;
    private AI ai;
    private boolean isX;
    public Game(boolean isClear, String player1, String player2) {
        if (isClear) {
            board = new Board();
            isX = true;
            if ("easy".equals(player1)) {
                ai1 = new EasyAI();    
            } else if ("medium".equals(player1)) {
                ai1 = new MediumAI();
                ai1.setBoard(board);   
            }
            if ("easy".equals(player2)) {
                ai2 = new EasyAI();    
            } else if ("medium".equals(player2)) {
                ai2 = new MediumAI();
                ai2.setBoard(board);   
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the cells:");
            String line = scanner.nextLine();
            board = new Board(line);
        }
    }
    public void start() {
        board.print();
        boolean isCurrent1 = false;
        while (true) {
            isCurrent1 = isCurrent1 ? false : true; 
            if (isCurrent1) {
                ai = ai1;        
            } else {
                ai = ai2;    
            }
            if (ai != null) {
                readCoordinatesFromAI();        
            } else {
                readCoordinates();    
            }    
            board.print();
            if (board.analize()) {
                break;
            }
            isX = isX ? false : true;
        }
    }
    public void readCoordinates() {
        int first = 0;
        int second = 0;
        while (true) {
            System.out.println("Enter the coordinates:");
            try {
                Scanner scanner = new Scanner(System.in);
                first = scanner.nextInt();
                second = scanner.nextInt();
                if (first < 1 || first > 3 || second < 1 || second > 3) {
                  System.out.println("Coordinates should be from 1 to 3!");
                } else {
                  if (board.isOccupied(first, second)) {
                    System.out.println("This cell is occupied! Choose another one!");    
                  } else {
                    board.set(first, second, isX ? 'X' : 'O');
                    break;    
                  }
                }  
            } catch (Exception e) {
                System.out.println("You should enter numbers!");
            }
        }  
    }
    public void readCoordinatesFromAI() {
        ai.printWho();
        while (true) {
            String move = ai.nextMove();
            int first = Integer.parseInt(Character.toString(move.charAt(0)));
            int second = Integer.parseInt(Character.toString(move.charAt(1)));
            if (setIfValid(first, second)) {
                break;
            }    
        }  
    }
    public boolean setIfValid(int first, int second) {
        if (first < 1 || first > 3 || second < 1 || second > 3) {
            return false;    
        } else {
            if (board.isOccupied(first, second)) {
                return false;    
            } else {
                board.set(first, second, isX ? 'X' : 'O');
                return true;    
            }
        }             
    }
}
class Board {
    private char[][] chars = {{'_', '_', '_',}, {'_', '_', '_',}, {'_', '_', '_',}};
    public Board(String line) {
        int count = 0;
        for (int j=0; j<3; j++) {
            for (int i=0; i<3; i++) {
                chars[j][i] = line.charAt(count);
                count++;       
            }
        }    
    }
    public Board() {
               
    }
    public void print() {
        System.out.println ("---------" + " ");
        for (int j=0; j<3; j++) {
            for (int i=0; i<5; i++) {
                if (i==0) {
                    System.out.print ("|" + " ");    
                } else if (i==4) {
                    System.out.println ("|" + " ");
                } else {
                    System.out.print(chars[j][i-1] == 'X' || chars[j][i-1] == 'O' ? chars[j][i-1] + " " : "  ");
                }
            }  
        }
        System.out.println ("\n---------" + " ");
    }
    public void set(int first, int second, char charNow) {
        chars[first-1][second-1] = charNow;               
    }
    public boolean isOccupied(int first, int second) {
        return chars[first-1][second-1] != '_';               
    }
    public boolean setIsX() {
        int xs = 0;
        int os = 0;
        for (int j=0; j<3; j++) {
            for (int i=0; i<3; i++) {
                if (chars[j][i] == 'X') {
                    xs++;    
                } else if (chars[j][i] == 'O') {
                    os++;
                }  
            }
        }
        if (xs > os) {
            return false;
        } else {
            return true;
        }
    }
    public boolean analize() {
        int xs = 0;
        int os = 0;
        int[] rows = new int[3];
        int[] cols = new int[3];
        int d1 = 0;
        int d2 = 0;
        for (int j=0; j<3; j++) {
            for (int i=0; i<3; i++) {
                if (chars[j][i] == 'X') {
                    xs++;    
                } else if (chars[j][i] == 'O') {
                    os++;
                } else {
                    
                }
                rows[j] += (int) chars[j][i];
                cols[i] += (int) chars[j][i];
                if (j==0 && i==2 || j==1 && i==1 || j==2 && i==0) {
                    d1 += (int) chars[j][i];
                }
                if (j==0 && i==0 || j==1 && i==1 || j==2 && i==2) {
                    d2 += (int) chars[j][i];
                }     
            }
        }
        boolean isXwins = false;
        boolean isOwins = false;
        if (Math.abs(xs - os) > 1) {
            System.out.println("Impossible");
            return false;    
        } else {
            if (rows[0] == 264 || rows[1] == 264 || rows[2] == 264
                    || cols[0] == 264 || cols[1] == 264 || cols[2] == 264
                    || d1 == 264 || d2 == 264) {
                isXwins = true;
            } 
            if (rows[0] == 237 || rows[1] == 237 || rows[2] == 237
                    || cols[0] == 237 || cols[1] == 237 || cols[2] == 237
                    || d1 == 237 || d2 == 237) {
                isOwins = true;
            }
            if (isXwins && isOwins) {
                System.out.println("Impossible");
                return false;
            } else if (isXwins) {
                System.out.println("X wins");
                return true;     
            } else if (isOwins) {
                System.out.println("O wins");
                return true;     
            } else if ((xs + os) == 9) {
                System.out.println("Draw");
                return true;
            } else {
                return false;
            }
        }
    }
    public String analizeMoveForWin() {
        int winGrade = 0;
        if (setIsX()) {
            winGrade = 88 * 2;    
        } else {
            winGrade = 79 * 2;
        }
        return analizeMoveForWinOrBlock(winGrade); 
    }
    public String analizeMoveForBlock() {
        int winGrade = 0;
        if (setIsX()) {
            winGrade = 79 * 2;    
        } else {
            winGrade = 88 * 2;
        }
        return analizeMoveForWinOrBlock(winGrade); 
    }
    public String analizeMoveForWinOrBlock(int winGrade) {
        int[][][] winState = getWinState();
        for (int[][] line : winState) {
            if (line[0][2] + line[1][2] + line[2][2] ==  winGrade+95) {
                for (int[] cell : line) {
                    if (cell[2] == 95) {
                        String first = String.valueOf(cell[0]+1); 
                        String second = String.valueOf(cell[1]+1);
                        return first + second;    
                    }
                }
            }
        }
        return "";   
        
    }
    
    
    public int[][][] getWinState() {
        int[][][] winState = new int[8][3][3];
        for (int j=0; j<3; j++) {
            for (int i=0; i<3; i++) {
                winState[j][i][2] = (int) chars[j][i];
                winState[j][i][0] = j;
                winState[j][i][1] = i;
                winState[i+3][j][2] = (int) chars[j][i];
                winState[i+3][j][0] = j;
                winState[i+3][j][1] = i;
                if (j==0 && i==2 || j==1 && i==1 || j==2 && i==0) {
                    winState[6][j][2] = (int) chars[j][i];
                    winState[6][j][0] = j;
                    winState[6][j][1] = i;
                }
                if (j==0 && i==0 || j==1 && i==1 || j==2 && i==2) {
                    winState[7][j][2] = (int) chars[j][i];
                    winState[7][j][0] = j;
                    winState[7][j][1] = i;
                }     
            }
        }
        return winState;    
    }        
}
public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Input command:");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            String[] splitLine = line.split(" ");
            if ("exit".equals(splitLine[0])) {
                break;
            } else {
                if (splitLine.length == 3) {
                    if ("start".equals(splitLine[0])) {
                        if (("easy".equals(splitLine[1]) || "medium".equals(splitLine[1]) || "user".equals(splitLine[1])) 
                            && ("easy".equals(splitLine[2]) || "medium".equals(splitLine[2]) || "user".equals(splitLine[1]))) {
                            Game game = new Game(true, splitLine[1], splitLine[2]);
                            game.start();
                        } else {
                            System.out.println("Bad parameters!");
                        }
                    } else if ("exit".equals(splitLine[0])) {
                        break;    
                    } else {
                        System.out.println("Bad parameters!");    
                    }
                } else {
                    System.out.println("Bad parameters!");    
                }
            }
        }
    }
}