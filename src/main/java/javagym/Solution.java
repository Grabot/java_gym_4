package javagym;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;

import noedit.Cell;
import noedit.Maze;
import noedit.Path;
import noedit.PathBuilder;
import noedit.Position;

import static noedit.Cell.*;

public class Solution {


    private int currentDirection;
    int RIGHT = 10;
    int UP = 11;
    int LEFT = 12;
    int DOWN = 13;
    @Nonnull
    @CheckReturnValue
    public Path solve(@Nonnull Maze maze, @Nonnull Position initialPosition) {
        // First the right handed rule!
        Validate.isTrue(Wall != maze.get(initialPosition),
                "Started inside a wall; this should never happen");

        // Ugly solution for now to remember the direction
        currentDirection = 0;
        System.out.println(maze.asStringAll());
        PathBuilder path = new PathBuilder(initialPosition);
        while (true) {

            Position currentPos = path.latest();

            if (checkFinished(path, maze, currentPos)) {
                // We finished!
                break;
            }

            if (currentDirection == UP || currentDirection == 0) {
                directionUpStep(path, maze, currentPos);
            } else if (currentDirection == RIGHT) {
                directionRightStep(path, maze, currentPos);
            } else if (currentDirection == LEFT) {
                directionLeftStep(path, maze, currentPos);
            } else if (currentDirection == DOWN) {
                directionDownStep(path, maze, currentPos);
            }

            if (path.size() > 200) {
                // we give up at this point.
                break;
            }
        }
        return path.build();
    }

    private void directionDownStep(PathBuilder path, Maze maze, Position currentPos) {
        // We went down the last time!
        // +-----+        +-----+
        // |     |        |     |
        // |  *  |   ==>  |     |
        // |     |        |  *  |
        // +-----+        +-----+
        // To keep the right side technique we first check left then down right and up.
        if (maze.getOrElse(currentPos.left(), Wall) == Open) {
            path.left();
            currentDirection = LEFT;
        } else if (maze.getOrElse(currentPos.down(), Wall) == Open) {
            path.down();
            currentDirection = DOWN;
        } else if (maze.getOrElse(currentPos.right(), Wall) == Open) {
            path.right();
            currentDirection = RIGHT;
        } else if (maze.getOrElse(currentPos.up(), Wall) == Open) {
            path.up();
            currentDirection = UP;
        } else {
            // can't go anywhere!?
            path.waitStep();
        }
    }

    private void directionLeftStep(PathBuilder path, Maze maze, Position currentPos) {
        // We went left the last time!
        // +-----+        +-----+
        // |     |        |     |
        // |  *  |   ==>  | *   |
        // |     |        |     |
        // +-----+        +-----+
        // To keep the right side technique we first check up then left down and right.
        if (maze.getOrElse(currentPos.up(), Wall) == Open) {
            path.up();
            currentDirection = UP;
        } else if (maze.getOrElse(currentPos.left(), Wall) == Open) {
            path.left();
            currentDirection = LEFT;
        } else if (maze.getOrElse(currentPos.down(), Wall) == Open) {
            path.down();
            currentDirection = DOWN;
        } else if (maze.getOrElse(currentPos.right(), Wall) == Open) {
            path.right();
            currentDirection = RIGHT;
        } else {
            // can't go anywhere!?
            path.waitStep();
        }
    }

    private void directionUpStep(PathBuilder path, Maze maze, Position currentPos) {
        // We went up the last time!
        // +-----+        +-----+
        // |     |        |  *  |
        // |  *  |   ==>  |     |
        // |     |        |     |
        // +-----+        +-----+
        // To keep the right side technique we first check right, then up, left and down.
        if (maze.getOrElse(currentPos.right(), Wall) == Open) {
            path.right();
            currentDirection = RIGHT;
        } else if (maze.getOrElse(currentPos.up(), Wall) == Open) {
            path.up();
            currentDirection = UP;
        } else if (maze.getOrElse(currentPos.left(), Wall) == Open) {
            path.left();
            currentDirection = LEFT;
        } else if (maze.getOrElse(currentPos.down(), Wall) == Open) {
            path.down();
            currentDirection = DOWN;
        } else {
            // can't go anywhere!?
            path.waitStep();
        }
    }

    private void directionRightStep(PathBuilder path, Maze maze, Position currentPos) {
        // We went to the right last time!
        // +-----+        +-----+
        // |     |        |     |
        // | *   |   ==>  |  *  |
        // |     |        |     |
        // +-----+        +-----+
        // To keep the right side technique we first check down, then right, up and finally left.
        if (maze.getOrElse(currentPos.down(), Wall) == Open) {
            path.down();
            currentDirection = DOWN;
        } else if (maze.getOrElse(currentPos.right(), Wall) == Open) {
            path.right();
            currentDirection = RIGHT;
        } else if (maze.getOrElse(currentPos.up(), Wall) == Open) {
            path.up();
            currentDirection = UP;
        } else if (maze.getOrElse(currentPos.left(), Wall) == Open) {
            path.left();
            currentDirection = LEFT;
        } else {
            // can't go anywhere!?
            path.waitStep();
        }
    }

    private boolean checkFinished(PathBuilder path, Maze maze, Position currentPos) {
        Position leftPos = currentPos.left();
        Position rightPos = currentPos.right();
        Position upPos = currentPos.up();
        Position downPos = currentPos.down();
        // If any of the positions is the exit than we just leave ofcourse
        Cell leftCell = maze.getOrElse(leftPos, Wall);
        if (leftCell == Exit) {
            path.left();
            return true;
        }
        Cell rightCell = maze.getOrElse(rightPos, Wall);
        if (rightCell == Exit) {
            path.right();
            return true;
        }
        Cell upCell = maze.getOrElse(upPos, Wall);
        if (upCell == Exit) {
            path.up();
            return true;
        }
        Cell downCell = maze.getOrElse(downPos, Wall);
        if (downCell == Exit) {
            path.down();
            return true;
        }
        return false;
    }
}
