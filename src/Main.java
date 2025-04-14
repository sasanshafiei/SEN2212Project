import javax.swing.JFrame;


public class Main {
    public static void main(String[] args) {
        int rowCount = 21;
        int colCount = 19;
        int tileSize = 32;
        int boardWidth= colCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame(" PacMan");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // peeer to center to screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();// Requests keyboard focus for pacmanGame so it can receive key events.
        frame.setVisible(true); // now we set visible to see all component in the window

    }
}
