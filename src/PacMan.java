import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Point;
import java.util.ArrayList;//foods and BFS , both sasan anil
import java.util.Queue; //BFS , SASAN
import java.util.LinkedList; // BFS,SASAN
import java.util.HashSet; // easy to search , ANIL


public class PacMan extends JPanel implements ActionListener , KeyListener {

    HashSet<Block> walls ;
    HashSet<Block> foods ;
    HashSet<Block> ghosts ;
    Block pacman ;

    Timer gameLoop;

    int score = 0;
    int lives = 3;
    boolean GameOver = false;

    private int rowCount = 21;
    private int colCount = 19;
    private int tileSize = 32;
    private int boardWidth= colCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage ;
    private Image blueGhostImage ;
    private Image redGhostImage ;
    private Image orangeGhostImage ;
    private Image pinkGhostImage ;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    char nextDirection = 'R';  // Store the intended next direction


    private boolean [][] buildPassableGrid (){
        boolean [][] grid = new boolean [rowCount][colCount];
        for (int row = 0; row < rowCount; row++) {
            String rowStr = tileMap[row];
            for (int col = 0; col < colCount; col++) {
                //every cell is passable unless it is a wall (char 'X')
                grid[row][col]= (rowStr.charAt(col) != 'X');
            }
        }
        return grid;
    }

    private ArrayList<Point> bfs (Point start , Point target ,  boolean [][] grid ){
        int rows  = grid.length;
        int cols = grid[0].length;
        boolean [][] visited = new boolean [rows][cols];
        Point [][] prev = new Point [rows][cols]; // store the prev node for each cell

        Queue<Point>queue = new LinkedList<>();
        queue.add(start);
        visited[start.y][start.x] = true;
        //direction up down left right
        int[] dx = {0,0,-1,1};
        int[] dy = {1,-1,0,0};

        while (!queue.isEmpty()) {
            Point cur = queue.poll();
            if(cur.equals(target)){
                //reconstruct the path from target back to start
                ArrayList<Point>path = new ArrayList<>();
                for (Point p =target; p !=null ; p = prev[p.y][p.x] ){
                    path.add(0,p );//insert at begining
                }
                return path;
            }
            //check the for neighbors
            for (int i = 0; i < 4; i++) {
                int newx = cur.x + dx[i];
                int newy = cur.y + dy[i];
                if (newx >= 0 && newy >= 0 && newx <cols && newy < rows ){
                    if (grid[newy][newx] &&  !visited[newy][newx]){
                        visited[newy][newx] = true;
                        Point neghbor =  new Point(newx, newy);
                        prev[newy][newx] = cur;
                        queue.add(neghbor);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void keyTyped(KeyEvent e) {}//we dont use
    // Invoked when a key is typed (pressed and released), primarily for character input.

    @Override
    public void keyPressed(KeyEvent e) {

    }//we dont use


    // Invoked when a key is pressed down; ideal for detecting non-character keys or starting an action.

    @Override
    public void keyReleased(KeyEvent e) {
//System.out.println("keyEvent"+e.getKeyCode());
        if (GameOver){ // resraet game in the case of game over when you press bottom
            loadMap();
            resetposition();
            lives = 3;
            GameOver = false;
            score = 0;
            gameLoop.start();
        }



        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.setDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.setDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.setDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.setDirection('R');
        }// Invoked when a key is released; useful for ending an action or resetting states.

        if (pacman.direction == 'U'){
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D'){
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L'){
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R'){
            pacman.image = pacmanRightImage;
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move ();
        repaint();
        if ( GameOver){
            gameLoop.stop();
        }
    }// meaning that the system will update the component's display as soon as it can, ensuring that any visual changes you made are rendered on the screen


    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        // Optionally you can also keep these, but only if you really need them.
        int startX;
        int startY;
        char direction = 'U';  //U D L R
        int velocityY = 0;
        int velocityX = 0;


        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

            // If you also want to store the "start" positions:
            this.startX = x;
            this.startY = y;
        }

        // key moves + seting ghost move 2 times slower than pacman
        //PacMan moves at tileSize/4; ghosts at tileSize/8.
        void uptadevelocity() {
            // Determine the speed factor:
            int factor = (this == PacMan.this.pacman) ? tileSize / 4 : tileSize / 8;
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -factor;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = factor;
            } else if (this.direction == 'L') {
                this.velocityX = -factor;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = factor;
                this.velocityY = 0;
            }
        }



        void setDirection(char direction) {
            char prevDirction = this.direction; //
            this.direction = direction;
            uptadevelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;

            //Checks for collisions with any wall and, if a collision occurs, reverts the move and restores the previous direction.
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirction;
                    uptadevelocity();
                }
            }
        }

        void reset (){
            this.x = this.startX;
            this.y = this.startY;
        }
    }


    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "X       bpo       X",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };




    public PacMan() {
       setPreferredSize(new Dimension(boardWidth, boardHeight));
       setBackground(Color.black);

        //making key presses work , jpannel listens to key presses
        addKeyListener(this);
        setFocusable(true);

        this.requestFocusInWindow();

       // loading Images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();

        //how long it takes to start timer,milliseconds gone between frames
        gameLoop = new Timer(55, this);
        gameLoop.start();
/*        System.out.println(walls.size());
        System.out.println(foods.size());
        System.out.println(ghosts.size());*/
    }

    // going tro tile map and crating objects for walls , foods, ghost and pacman ;
    public void loadMap (){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap [r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') {//Block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall); // adding wall to hashset
                }
                else if (tileMapChar == 'b') { // blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);// adding blue ghost to hashset
                }
                else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);// adding orange ghost to hashset
                }
                else if (tileMapChar == 'r') { // red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);// adding red ghost to hashset
                }
                else if (tileMapChar == 'p') { // red ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);// adding pink ghost to hashset
                }
                else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') { // food
                    Block food = new Block(null, x+14, y+14, 4, 4); // we dont add an image we crate food
                    foods.add(food);// adding food to hashset
                }
            }
        }
    }
    // drawing all to the game ...
    public void paintComponent (Graphics g) {
        super.paintComponent(g); // invoking function of same name to jpanel
        draw (g);
    }
    public void draw (Graphics g) {
        g.drawImage(pacman.image,
                pacman.x,
                pacman.y,
                pacman.width,
                pacman.height,
                null
        );
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
        }
        for (Block wall : walls) {
            g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);
        }
        g.setColor(Color.white);
        for (Block food : foods) {
            g.fillRect(food.x,food.y,food.width,food.height);
        }
        // score
        g.setFont(new Font("Arial", Font.BOLD, 18));
        if (GameOver){
            g.drawString("GAME OVER :" + String.valueOf(score),tileSize/2 , tileSize/2);
        }else {
            g.drawString("x" +  String.valueOf(lives)+ " score: "+String.valueOf(score),tileSize/2 , tileSize/2);
        }


    }

    // In the Block class: update speed based on whether the block is PacMan or ghost.


    public void move (){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // chack wall collision (step bak if hapend)
        for (Block wall : walls ) {
            if (collision(pacman ,  wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break ;
            }
        }


        // Build the grid once per frame.
        boolean[][] passableGrid = buildPassableGrid();
        // Convert PacMan's current position to grid coordinates.
        int pacmanGridX = pacman.x / tileSize;
        int pacmanGridY = pacman.y / tileSize;
        Point pacPos = new Point(pacmanGridX, pacmanGridY);

        // Update ghost movement using BFS.
        for (Block ghost : ghosts) {
            // Convert ghost position to grid coordinates.
            int ghostGridX = ghost.x / tileSize;
            int ghostGridY = ghost.y / tileSize;
            Point ghostPos = new Point(ghostGridX, ghostGridY);

            // Compute the shortest path from ghost to PacMan.
            ArrayList<Point> path = bfs(ghostPos, pacPos, passableGrid);
            if (path != null && path.size() > 1) {
                // The first element is the ghost's current cell,
                // the second element is the next cell on the path.
                Point nextStep = path.get(1);
                if (nextStep.x > ghostGridX) {
                    ghost.setDirection('R');
                } else if (nextStep.x < ghostGridX) {
                    ghost.setDirection('L');
                } else if (nextStep.y > ghostGridY) {
                    ghost.setDirection('D');
                } else if (nextStep.y < ghostGridY) {
                    ghost.setDirection('U');
                }
            }

            // Move ghost by applying its velocity.
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            // Handle ghost collisions with walls as before.
            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    // If a collision occurs, you might choose to set a new direction.
                    // Optionally, you could fall back to a random direction, or simply ignore if BFS is active.
                }
            }
        }
        //  Check if any ghost has collided with PacMan.
        for (Block ghost : ghosts) {
            if (collision(pacman, ghost)) {
                lives -= 1;
                if (lives <= 0) {
                    GameOver = true;
                }
                resetposition();
                return; // Stop further updates in this frame.
            }
        }
        // check food collision
        ArrayList<Block> foodsToRemove = new ArrayList<>();
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodsToRemove.add(food);
                score += 10;
            }
        }
        foods.removeAll(foodsToRemove);

        // if user WIN just reset everyting
        if(foods.isEmpty()){
            loadMap();
            lives = 3;
            score = 0 ;
            resetposition();
        }

    }
    // making pacman not pass into walls
    public boolean collision(Block a ,  Block b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height&&
               a.y + a.height > b.y;
    }
    // reset function when pacman loos a life
    public void resetposition(){
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;

        for (Block ghost : ghosts) {
            ghost.reset();

        }

    }

}
