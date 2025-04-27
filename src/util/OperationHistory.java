package util;

import java.util.Stack;
import model.Matrix;

public class OperationHistory {
    private Stack<Matrix> undoStack = new Stack<>();
    private Stack<Matrix> redoStack = new Stack<>();

    public void pushOperation(Matrix matrix) {
        undoStack.push(matrix);
        redoStack.clear();
    }

    public Matrix undo() {
        if (undoStack.isEmpty()) {
            return null;
        }
        Matrix matrix = undoStack.pop();
        redoStack.push(matrix);
        return matrix;
    }

    public Matrix redo() {
        if (redoStack.isEmpty()) {
            return null;
        }
        Matrix matrix = redoStack.pop();
        undoStack.push(matrix);
        return matrix;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}