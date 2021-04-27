package assignment01;

import java.util.ArrayList;

public class gridGraph {

    // Attributes
    ArrayList<cell> cells = new ArrayList<>();
    ArrayList<wall> walls = new ArrayList<>();

    // Constructor Methods
    gridGraph(int x, int y) {
        // constructor method that creates the data structure
        generateGraphStructure(x, y);
    }
    // Public Methods
    public cell getCell(int index) {
        // returns a cell object given a particular index on the cells ArrayList.
        return cells.get(index);
    }
    public int getCellsSize() {
        // returns the cardinality of the cells set
        return cells.size();
    }
    public wall getWall(int index) {
        // returns a wall object given a particular index on the walls ArrayList
        return walls.get(index);
    }
    public int getWallsSize() {
        // returns the cardinality of the walls set
        return walls.size();
    }
    // Private Methods
    private void generateGraphStructure(int xSize, int ySize) {
        /* this method takes a 2D size parameter (as two separate integer values) and populates the data structure with
            the following arrangement:
                Each "pixel" in the graph structure is a cell. Each cell has at most four walls. To generate every
                "pixel" we must iterate over the size of the canvas given as inputs to this method.
         */
        for (int yIndex = 1; yIndex <= ySize; yIndex++) {
            for (int xIndex = 1; xIndex <= xSize; xIndex++) {
                int[] currentPosition = {xIndex, yIndex};
                cells.add(new cell(currentPosition));
            }
        }
        /*
            The next step in the creation is to generate all the appropriate walls in the grid. There are four walls per
            cell, however the method will place only the right and bottom walls by default. This leaves the outside and
            corner cells requiring special consideration. This arrangement will avoid creation of duplicates and will
            prevent needing to run a search for a particular wall to avoid duplicates. Additionally, the adjacency
            lists for each cell are created. This is done through the cell.addNeighbors(cell c) method.
         */
            for (int listIndex = 0; listIndex < cells.size(); listIndex++) {
                // get the current cell from list
                cell workingCell = cells.get(listIndex);

                // get working cell's position on the grid
                int cellX = workingCell.getX();
                int cellY = workingCell.getY();

                // add walls and neighbors to working cell
                // add top wall and neighbor
                if (cellY == 1) {
                    wall workingTop = new wall(workingCell, null);
                    walls.add(workingTop);
                    workingCell.setTopWall(workingTop);
                    workingCell.setTopNeighbor(null);
                } else {
                    wall workingTop = new wall(workingCell, cells.get(listIndex - xSize));
                    if (walls.contains(workingTop)) {
                        workingCell.setTopWall(walls.get(walls.indexOf(workingTop)));
                    } else {
                        workingCell.setTopWall(workingTop);
                        walls.add(workingTop);
                    }
                    workingCell.setTopNeighbor(cells.get(listIndex - xSize));
                }
                // add right wall and neighbor
                if (cellX == xSize) {
                    wall workingRight = new wall(workingCell, null);
                    walls.add(workingRight);
                    workingCell.setRightWall(workingRight);
                    workingCell.setRightNeighbor(null);
                } else {
                    wall workingRight = new wall(workingCell, cells.get(listIndex + 1));
                    if (walls.contains(workingRight)) {
                        workingCell.setRightWall(walls.get(walls.indexOf(workingRight)));
                    } else {
                        workingCell.setRightWall(workingRight);
                        walls.add(workingRight);
                    }
                    workingCell.setRightNeighbor(cells.get(listIndex + 1));
                }
                // add bottom wall and neighbor
                if (cellY == ySize) {
                    wall workingBottom = new wall(workingCell, null);
                    walls.add(workingBottom);
                    workingCell.setBottomWall(workingBottom);
                    workingCell.setBottomNeighbor(null);
                } else {
                    wall workingBottom = new wall(workingCell, cells.get(listIndex + xSize));
                    if (walls.contains(workingBottom)) {
                        workingCell.setBottomWall(walls.get(walls.indexOf(workingBottom)));
                    } else {
                        workingCell.setBottomWall(workingBottom);
                        walls.add(workingBottom);
                    }
                    workingCell.setBottomNeighbor(cells.get(listIndex + xSize));
                }
                // add left wall and neighbor
                if (cellX ==1) {
                    wall workingLeft = new wall(workingCell, null);
                    walls.add(workingLeft);
                    workingCell.setLeftWall(workingLeft);
                    workingCell.setLeftNeighbor(null);
                } else {
                    wall workingLeft = new wall(workingCell, cells.get(listIndex - 1));
                    if (walls.contains(workingLeft)) {
                        workingCell.setLeftWall(walls.get(walls.indexOf(workingLeft)));
                    } else {
                        workingCell.setLeftWall(workingLeft);
                        walls.add(workingLeft);
                    }
                    workingCell.setLeftNeighbor(cells.get(listIndex - 1));
                }
            }
        }
    // Internal Classes
    public class cell {
        // Attributes
        private int[] position;
        private int visitCount = 0;
        private wall topWall = null;
        private wall leftWall = null;
        private wall rightWall = null;
        private wall bottomWall = null;
        private final cell[] neighbors = new cell[4];
        // Methods
        cell(int[] coordinates) {
            // sets the coordinates based on the input array. Once set, this cannot be changed from outside the object
            // scope.
            this.setPosition(coordinates);
        }
        private void setPosition(int[] orderedPair) {
            // this method is used solely in the constructor method as the position of individual cells should not
            // change after creation
            this.position = orderedPair;
        }
        protected int getX() {
            // returns only the X coordinate of the cell
            return this.position[0];
        }
        protected int getY() {
            // returns only the Y coordinate of the cell
            return this.position[1];
        }
        protected void setTopWall(wall inputWall) {
            // method to place the top wall into its appropriate place
            this.topWall = inputWall;
        }
        protected wall getTopWall() {
            // returns the top wall object
            return this.topWall;
        }
        protected void setLeftWall(wall inputWall) {
            // method to place the left wall into its appropriate place
            this.leftWall = inputWall;
        }
        protected wall getLeftWall() {
            // returns the left wall object
            return this.leftWall;
        }
        protected void setRightWall(wall inputWall) {
            // method to place the right wall into its appropriate place
            this.rightWall = inputWall;
        }
        protected wall getRightWall() {
            // returns the right wall------- object
            return this.rightWall;
        }
        protected void setBottomWall(wall inputWall) {
            // method to place the bottom wall into its appropriate place
            this.bottomWall = inputWall;
        }
        protected wall getBottomWall() {
            // returns the bottom wall object
            return this.bottomWall;
        }
        protected void visit() {
            // increases the visit count by 1 every time the method is called
            this.visitCount++;
        }
        protected int getVisitCount() {
            // returns the visit count when called
            return this.visitCount;
        }
        protected void setTopNeighbor(cell c) {
            // this method takes a cell as input and adds as the top neighbor
            this.neighbors[0] = c;
        }
        protected void setRightNeighbor(cell c) {
            // this method takes a cell as input and adds as the right neighbor
            this.neighbors[1] = c;
        }
        protected void setBottomNeighbor(cell c) {
            // this method takes a cell as input and adds as the bottom neighbor
            this.neighbors[2] = c;
        }
        protected void setLeftNeighbor(cell c) {
            // this method takes a cell as input and adds as the left neighbor
            this.neighbors[3] = c;
        }
        protected cell[] getNeighbors() {
            // returns the array list of neighboring cells
            // The array is of format:
            //  0 = top
            //  1 = right
            //  2 = bottom
            //  3 = left
            return this.neighbors;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof cell)) {
                return false;
            } else {
                cell compareCell = (cell) obj;
                return (this.getX() == compareCell.getX() && this.getY() == compareCell.getY());
            }
        }
    }
    public class wall {
        // Attributes
        private final cell cellOne;
        private final cell cellTwo;
        private boolean passage = false;
        // Methods
        wall(cell cellOne, cell cellTwo) {
            // constructor method assigns each cell to an end of an edge. These two cells exist on each side of the
            // "wall" and are divided by this object
            this.cellOne = cellOne;
            this.cellTwo = cellTwo;
        }
        public cell getCellOne() {
            // returns the first cell in the edge (or wall, such as it is)
            return this.cellOne;
        }
        public cell getCellTwo() {
            // returns the second cell in the edge (or wall)
            return this.cellTwo;
        }
        public void setPassage(boolean tf) {
            // sets the passage parameter in the wall, if the wall is meant to be "knocked down" use this to specify
            this.passage = tf;
        }
        public boolean isPassage() {
            // returns the value that determines whether this edge is a passage or not. The default is 'false' which
            // indicates this wall is non-passable.
            return this.passage;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof wall)) {
                return false;
            } else {
                return ((this.getCellOne().equals(((wall)obj).getCellOne()) || this.getCellOne().equals(((wall)obj).getCellTwo())) &&
                        (this.getCellTwo().equals(((wall)obj).getCellOne()) || this.getCellTwo().equals(((wall)obj).getCellTwo())));
            }
        }
    }
}
