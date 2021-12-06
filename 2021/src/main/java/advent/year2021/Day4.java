package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.*;


public class Day4 {

    public static void main(String[] args) {
        final String DAY = "day4";

        Day4 day4 = new Day4();
        System.out.println("SAMPLE 1: " + day4.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + day4.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2: " + day4.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + day4.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private void parseBoardRow(String line, Context context) {
        String[] row = line.trim().split("\\s+");
        Board board = context.parsedBoard;
        List<String> boardRow = new ArrayList<>();
        int rowNum = board.boardArr.size();
        for (int i=0; i<row.length; i++) {
            boardRow.add(row[i]);
            Set<Integer[]> fs = board.frequencies.get(row[i]);
            if (fs == null) {
                fs = new HashSet<>();
            }
            Integer[] p = new Integer[2];
            p[0] = rowNum;
            p[1] = i;
            fs.add(p);
            board.frequencies.put(row[i], fs);
        }
        board.boardArr.add(boardRow);
    }

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();

                if(this.getState() == -1) {
                    // parse winners
                    String[] winnersArr = line.split(",");
                    context.winners = new ArrayList<>(Arrays.asList(winnersArr));
                    this.setState(1);
                } else if(this.getState() == 1) {
                    // this is the first line of a board
                    context.parsedBoard = new Board();
                    this.setState(2);
                } else if(this.getState() == 2) {
                    if(line == null || line.isEmpty()) {
                        // This is the end of a board
                        List<Board> boards = context.boards;
                        boards.add(context.parsedBoard);
                        context.parsedBoard = new Board();
                        this.setState(2);
                    } else {
                        parseBoardRow(line, context);
                    }
                }
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        context.boards.add(context.parsedBoard);
        context.parsedBoard = new Board();
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);
        //System.out.println(context);

        for(String winner :  context.winners) {
            //System.out.println("WINNER: " + winner);
            for(Board board : context.boards) {
                boolean win = board.play(winner);
                if(win) {
                    return Integer.parseInt(winner) * board.total();
                }
            }
            //System.out.println(context);
        }
        return -1;
    }

    public Integer part2(String fileName) {
        Context context = parseContext(fileName);
        //System.out.println(context);

        int boardsWon = 0;
        for(String winner :  context.winners) {
            //System.out.println("WINNER: " + winner);
            for(Board board : context.boards) {
                if(!board.won) {
                    boolean win = board.play(winner);
                    if (win) {
                        boardsWon += 1;
                        if (boardsWon == context.boards.size()) {
                            return Integer.parseInt(winner) * board.total();
                        }
                    }
                }
            }
            //System.out.println(context);
        }
        return -1;
    }

    private static class Board {
        List<List<String>> boardArr;
        Map<String, Set<Integer[]>> frequencies;
        List<Integer> rowWinners;
        List<Integer> colWinners;
        boolean won = false;

        public Board() {
            boardArr = new ArrayList<>();
            frequencies = new HashMap<>();
        }

        private void init() {
            rowWinners = new ArrayList<>();
            colWinners = new ArrayList<>();
            for(int i=0; i<boardArr.size(); i++) {
                rowWinners.add(0);
                colWinners.add(0);
            }
        }

        // return true if this board is a winner
        public boolean play(String winner) {
            if(rowWinners == null) {
                init();
            }
            Set<Integer[]> boardWinners = frequencies.get(winner);
            if(boardWinners != null) {
                // this board contains a winning number. Mark the winners by setting them to null
                for(Integer[] coords : boardWinners) {
                    boardArr.get(coords[0]).set(coords[1], null);

                    int rowTotal = rowWinners.get(coords[0]);
                    rowWinners.set(coords[0], rowTotal+1);

                    int colTotal = colWinners.get(coords[1]);
                    colWinners.set(coords[1], colTotal + 1);

                    if(rowTotal+1 == boardArr.size() || colTotal+1 == boardArr.size()) {
                        won = true;
                        return true;
                    }
                }
            }
            return false;
        }

        public int total() {
            int total = 0;
            for(List<String> row : boardArr) {
                for(String v : row) {
                    if(v != null) {
                        total += Integer.parseInt(v);
                    }
                }
            }
            return total;
        }

        @Override
        public String toString() {
            return "Board{" +
                    "boardArr=" + boardArr +
                    '}';
        }
    }

    private static class Context {

        List<String> winners;
        List<Board> boards;
        Board parsedBoard;

        public Context() {
            boards = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Context{" +
                    "winners=" + winners +
                    ", boards=" + boards +
                    ", parsedBoard=" + parsedBoard +
                    '}';
        }
    }
}
