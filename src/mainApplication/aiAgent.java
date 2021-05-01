package mainApplication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class aiAgent {
    // attributes
    private gridGraph.cell goalCell;
    private gridGraph.cell startCell;
    private final LinkedList<gridGraph.cell> solutionPathStack = new LinkedList<>();
    private final ArrayList<gridGraph.cell> possibleMoves = new ArrayList<>();

    // constructor methods
    aiAgent(){
        // null constructor
    }
    aiAgent(gridGraph.cell startCell, gridGraph.cell goalCell) {
        // this constructor takes a start cell and goal cell and solves the maze, producing a solutionPath
        // first, push the start cell onto the solutionPathStack list
        this.startCell = startCell;
        this.solutionPathStack.push(startCell);
        // second, set the goal cell attribute
        this.setGoalCell(goalCell);
    }
    // public methods
    public void setGoalCell(gridGraph.cell inputCell) {
        // method sets the attribute for the goal cell
        this.goalCell = inputCell;
    }
    public gridGraph.cell getGoalCell() {
        // returns the cell object of the goal as provided in the input
        return this.goalCell;
    }
    public void setCurrentCell(gridGraph.cell inputCell) {
        // method to set the current cell attribute
        this.solutionPathStack.push(inputCell);
    }
    public gridGraph.cell getCurrentCell() {
        // returns the current cell on which the AI agent is acting
        return this.solutionPathStack.peekLast();
    }
    public void setPreviousCell(gridGraph.cell inputCell) {
        // method to keep track of previous cell. Adds the inputCell to the top of the solutionPathStack
        this.solutionPathStack.push(inputCell);
    }
    public gridGraph.cell getPreviousCell() {
        // method pops the top cell off the solutionPath stack
        return this.solutionPathStack.pop();
    }
    public LinkedList<gridGraph.cell> getSolutionPath() {
        // method returns the list of cells that compose the solution path from start to finish
        return this.solutionPathStack;
    }
    // SOLVER METHODS
    public void solveMaze() {
        // this method actually does the solving, and creates the solution path along the stack
        // define starting cell
        gridGraph.cell currentCell = this.solutionPathStack.peek();
        while (!(currentCell.equals(goalCell))) {
            // determine which cells can be moved to
            currentCell.visit();
            determinePossibleMoves(currentCell);
            if (possibleMoves.isEmpty()) {
                    // if no possible moves at this cell, pop off current cell from stack and repeat for previous cell
                    solutionPathStack.pop();
                    currentCell = solutionPathStack.peek();
                //System.out.println("Stack size reduced by one. New Size: " + solutionPathStack.size());
            } else {
                // else choose the best possible move, and add the new cell to the stack and redo loop
                currentCell = computeBestMove(possibleMoves, goalCell);
                solutionPathStack.push(currentCell);
                //System.out.println("X: " + currentCell.getX() + "; Y: " + currentCell.getY());
                //System.out.println("Stack size increased by one. New Size: " + solutionPathStack.size());
            }
        }
    }
    public void dfsSolve() {
        gridGraph.cell currentCell = this.solutionPathStack.peek();
        while (!(currentCell.equals(goalCell))) {
            // determine which cells can be moved to
            currentCell.visit();
            determinePossibleMoves(currentCell);
            if (possibleMoves.isEmpty()) {
                // if no possible moves at this cell, pop off current cell from stack and repeat for previous cell
                solutionPathStack.pop();
                currentCell = solutionPathStack.peek();
                //System.out.println("Stack size reduced by one. New Size: " + solutionPathStack.size());
            } else {
                // else choose the best possible move, and add the new cell to the stack and redo loop
                currentCell = possibleMoves.get(0);
                solutionPathStack.push(currentCell);
                //System.out.println("X: " + currentCell.getX() + "; Y: " + currentCell.getY());
                //System.out.println("Stack size increased by one. New Size: " + solutionPathStack.size());
            }
        }
    }
    public void randomWalkSolve() {
        // this method uses the depth first search to solve the maze
        gridGraph.cell currentCell = this.solutionPathStack.peek();
        while (!(currentCell.equals(goalCell))) {
            // determine which cells can be moved to
            currentCell.visit();
            determinePossibleMoves(currentCell);
            if (possibleMoves.isEmpty()) {
                // if no possible moves at this cell, pop off current cell from stack and repeat for previous cell
                solutionPathStack.pop();
                currentCell = solutionPathStack.peek();
                //System.out.println("Stack size reduced by one. New Size: " + solutionPathStack.size());
            } else {
                // else choose the best possible move, and add the new cell to the stack and redo loop
                currentCell = randomNextMove(possibleMoves);
                solutionPathStack.push(currentCell);
                //System.out.println("X: " + currentCell.getX() + "; Y: " + currentCell.getY());
                //System.out.println("Stack size increased by one. New Size: " + solutionPathStack.size());
            }
        }
    }
    public void euclideanSolve() {
        // this method uses the original heuristic to solve the maze
        this.solveMaze();
    }
    public void lookAheadSolve() {
        // this method uses the look ahead addition to the heuristic to solve the maze
        LinkedList<gridGraph.cell> workingList = new LinkedList<>();
        gridGraph.cell currentCell = this.solutionPathStack.peek();
        while (!(currentCell.equals(this.goalCell))) {
            currentCell.visit();
            determinePossibleMoves(currentCell);
            if (possibleMoves.isEmpty()) {
                solutionPathStack.pop();
                System.out.println("One element removed from solutionPathStack. New Size: " + solutionPathStack.size());
                currentCell = solutionPathStack.peek();
            } else {
                LinkedList<gridGraph.cell>[] lookAheadPathArray = new LinkedList[possibleMoves.size()];
                for (int j = 0; j < possibleMoves.size(); j++) {
                    miniAgent workingAgent = new miniAgent(currentCell, this.goalCell, 3);
                    workingAgent.traverseLookAhead();
                    //System.out.println("Path Array Index " + j + ", Look Ahead Stack Size: " + workingAgent.getLookAheadStack().size());
                    lookAheadPathArray[j] = workingAgent.getLookAheadStack();
                }
                // define the output index of the best stack to add to the path stack
                int outputIndex = 0;
                gridGraph.cell workingCell;
                double workingDistance = Double.MAX_VALUE;
                for (int k = 0; k < lookAheadPathArray.length; k++) {
                    workingCell = lookAheadPathArray[k].peek();
                    if (Objects.nonNull(workingCell)) {
                        double loopDistance = euclideanDistance(workingCell, this.goalCell);
                        if (loopDistance < workingDistance) {
                            outputIndex = k;
                        }
                    }
                }
                System.out.println("Output Index = " + outputIndex);
                LinkedList<gridGraph.cell> chosenList = lookAheadPathArray[outputIndex];
                System.out.println("Before adding look ahead stack: " + solutionPathStack.size());
                System.out.println("Chosen List SIze: " + chosenList.size());
                solutionPathStack.add(possibleMoves.get(outputIndex));
                System.out.println("After adding look ahead stack: " + solutionPathStack.size());
            }
        }
    }

    public void secondLookAhead() {
        gridGraph.cell currentCell = this.solutionPathStack.peek();

        while (!(currentCell.equals(goalCell))) {
            // determine which cells can be moved to
            currentCell.visit();
            determinePossibleMoves(currentCell);
            if (possibleMoves.isEmpty()) {
                // if no possible moves at this cell, pop off current cell from stack and repeat for previous cell
                solutionPathStack.pop();
                currentCell = solutionPathStack.peek();
                //System.out.println("Stack size reduced by one. New Size: " + solutionPathStack.size());
            } else {
                // else choose the best possible move, and add the new cell to the stack and redo loop
                currentCell = chooseBestLookAhead(possibleMoves, goalCell);
                solutionPathStack.push(currentCell);
                System.out.println("Solution Path Stack Size: " + solutionPathStack.size());
                //System.out.println("X: " + currentCell.getX() + "; Y: " + currentCell.getY());
                //System.out.println("Stack size increased by one. New Size: " + solutionPathStack.size());
            }
        }

    }

    // private methods
    private gridGraph.cell chooseBestLookAhead(ArrayList<gridGraph.cell> inputMoveList, gridGraph.cell inputCell) {
        if (inputMoveList.size() == 0) {
            System.out.println("The input move list size is zero.");
        }
        gridGraph.cell outputCell = null;
        double shortestDistance = Double.MAX_VALUE;
        for (int i = 0; i < inputMoveList.size(); i++) {
            miniAgent workingAgent = new miniAgent(inputMoveList.get(i), this.goalCell, 3);
            workingAgent.traverseLookAhead();
            double workingDistance = workingAgent.getDistanceToGoal();
            if (workingDistance < shortestDistance) {
                outputCell = inputMoveList.get(i);
            }
        }
        return outputCell;
    }
    private void determinePossibleMoves(gridGraph.cell inputCell) {
        // this method takes a cell as input, and adds the cells that are possible to move to to the possibleMoves array
        // clear the array first, such that there are no other cells present
        this.possibleMoves.clear();
        // examine each wall and add the neighboring cell to the possible moves list if the wall is a passage
        if (inputCell.getTopWall().isPassage()) {
            gridGraph.cell topNeighbor = inputCell.getNeighbors()[0];
            if (topNeighbor.getVisitCount() == 0) {
                this.possibleMoves.add(topNeighbor);
            }
        }
        if (inputCell.getRightWall().isPassage()) {
            gridGraph.cell rightNeighbor = inputCell.getNeighbors()[1];
            if (rightNeighbor.getVisitCount() == 0) {
                this.possibleMoves.add(rightNeighbor);
            }
        }
        if (inputCell.getBottomWall().isPassage()) {
            gridGraph.cell bottomNeighbor = inputCell.getNeighbors()[2];
            if (bottomNeighbor.getVisitCount() == 0) {
                this.possibleMoves.add(bottomNeighbor);
            }
        }
        if (inputCell.getLeftWall().isPassage()) {
            gridGraph.cell leftNeighbor = inputCell.getNeighbors()[3];
            if (leftNeighbor.getVisitCount() == 0) {
                this.possibleMoves.add(leftNeighbor);
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
    private gridGraph.cell randomNextMove(ArrayList<gridGraph.cell> inputCellList) {
        Random rand = new Random();
        int randomChoice = rand.nextInt(inputCellList.size());
        return inputCellList.get(randomChoice);
    }
    private double euclideanDistance(gridGraph.cell cellOne, gridGraph.cell cellTwo) {
        // this method takes two cells as input and computes the straight line distance between them
        int oneXpos = cellOne.getX();
        int oneYpos = cellOne.getY();
        int twoXpos = cellTwo.getX();
        int twoYpos = cellTwo.getY();
        return Math.sqrt(Math.pow((twoXpos - oneXpos),2) + Math.pow((twoYpos - oneYpos),2));
    }
}