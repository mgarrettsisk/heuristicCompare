package mainApplication;

import java.util.ArrayList;
import java.util.LinkedList;

public class miniAgent {
    // this is a mini agent object that is spawned when doing the look ahead
    private int depthLimit;
    private double distanceToGoal;
    private gridGraph.cell startCell;
    private gridGraph.cell goalCell;
    private final LinkedList<gridGraph.cell> lookAheadStack = new LinkedList<>();
    private final LinkedList<gridGraph.cell> visitedCells = new LinkedList<>();
    private final ArrayList<gridGraph.cell> agentPossibleMoves = new ArrayList<>();

    miniAgent(gridGraph.cell startCell, gridGraph.cell goalCell, int depthLimit) {
        // constructor method that takes a look ahead depth limit as input
        setDepthLimit(depthLimit);
        setStartCell(startCell);
        setGoalCell(goalCell);
    }
    public int getDepthLimit() {
        // returns the depth limit as specified
        return this.depthLimit;
    }
    public void setDepthLimit(int input) {
        // permits the changing of the depth limit
        this.depthLimit = input;
    }
    public gridGraph.cell getStartCell() {
        // returns the start cell of the mini agent
        return this.startCell;
    }
    public void setStartCell(gridGraph.cell inputCell) {
        // sets the start cell of the mini agent
        this.startCell = inputCell;
    }
    public gridGraph.cell getGoalCell() {
        // returns the goal cell as specified
        return goalCell;
    }
    public void setGoalCell(gridGraph.cell goalCell) {
        // sets the goal cell attribute
        this.goalCell = goalCell;
    }
    public double getDistanceToGoal() {
        // returns the value of distance to goal of the final
        resetVisitedCells();
        return this.distanceToGoal;
    }
    public void setDistanceToGoal(double inputDistance) {
        // sets the distance to goal
        this.distanceToGoal = inputDistance;
    }
    public LinkedList<gridGraph.cell> getLookAheadStack() {

        return this.lookAheadStack;
    }

    public void traverseLookAhead() {
        // initialize look ahead limit counter
        int lookAheadStep = 1;

        // put start cell in look ahead stack
        lookAheadStack.push(this.startCell);

        // assign the current cell from the stack
        gridGraph.cell currentCell = lookAheadStack.peek();

        // set the conditions for look ahead operation
        while ((lookAheadStep <= this.depthLimit) && (lookAheadStep != 0) && (!currentCell.equals(goalCell))) {
            // visit the current cell
            System.out.println("Look ahead step: " + lookAheadStep);

            currentCell.visit();
            visitedCells.add(currentCell);
            determinePossibleMoves(currentCell);

            // determine whether to continue forward or back up
            if (this.agentPossibleMoves.isEmpty()) {
                lookAheadStep--;
                this.lookAheadStack.pop();
                currentCell = this.lookAheadStack.peek();
            } else {
                currentCell = computeBestMove(this.agentPossibleMoves, this.goalCell);
                lookAheadStack.push(currentCell);
                setDistanceToGoal(euclideanDistance(currentCell,this.goalCell));
                lookAheadStep++;
            }
        }
    }
    // private methods
    private void resetVisitedCells() {
        for (int i = 0; i < visitedCells.size(); i++) {
            visitedCells.get(i).resetVisits();
        }
    }
    private double euclideanDistance(gridGraph.cell cellOne, gridGraph.cell cellTwo) {
        // this method takes two cells as input and computes the straight line distance between them
        int oneXpos = cellOne.getX();
        int oneYpos = cellOne.getY();
        int twoXpos = cellTwo.getX();
        int twoYpos = cellTwo.getY();
        return Math.sqrt(Math.pow((twoXpos - oneXpos),2) + Math.pow((twoYpos - oneYpos),2));
    }
    private void determinePossibleMoves(gridGraph.cell inputCell) {
        // this method takes a cell as input, and adds the cells that are possible to move to to the possibleMoves array
        // clear the array first, such that there are no other cells present
        this.agentPossibleMoves.clear();
        // examine each wall and add the neighboring cell to the possible moves list if the wall is a passage
        if (inputCell.getTopWall().isPassage()) {
            gridGraph.cell topNeighbor = inputCell.getNeighbors()[0];
            if (topNeighbor.getVisitCount() == 0) {
                this.agentPossibleMoves.add(topNeighbor);
            }
        }
        if (inputCell.getRightWall().isPassage()) {
            gridGraph.cell rightNeighbor = inputCell.getNeighbors()[1];
            if (rightNeighbor.getVisitCount() == 0) {
                this.agentPossibleMoves.add(rightNeighbor);
            }
        }
        if (inputCell.getBottomWall().isPassage()) {
            gridGraph.cell bottomNeighbor = inputCell.getNeighbors()[2];
            if (bottomNeighbor.getVisitCount() == 0) {
                this.agentPossibleMoves.add(bottomNeighbor);
            }
        }
        if (inputCell.getLeftWall().isPassage()) {
            gridGraph.cell leftNeighbor = inputCell.getNeighbors()[3];
            if (leftNeighbor.getVisitCount() == 0) {
                this.agentPossibleMoves.add(leftNeighbor);
            }
        }
    }
    private gridGraph.cell computeBestMove(ArrayList<gridGraph.cell> inputCellList, gridGraph.cell goalCell) {
        // this method takes the current possible moves list, and the goal cell as inputs, and determines which cell
        // should be used next in the path
        gridGraph.cell outputCell = inputCellList.get(0);
        double shortestDistance = euclideanDistance(inputCellList.get(0), goalCell);
        // find the index of the cell with the shortest straight line distance to goal
        for (int listIndex = 1; listIndex < inputCellList.size(); listIndex++) {
            // compute the pythagorean distance between the current cell and the goal cell
            double currentDistance = euclideanDistance(inputCellList.get(listIndex), goalCell);
            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                outputCell = inputCellList.get(listIndex);
            }
        }
        return outputCell;
    }
}
