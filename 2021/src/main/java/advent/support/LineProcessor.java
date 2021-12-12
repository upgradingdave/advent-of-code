package advent.support;

import java.util.HashMap;
import java.util.Map;

public abstract class LineProcessor<T> {

    T context;
    int lineNumber;
    int state;

    public LineProcessor(T context) {
        this.context = context;
        this.state = -1;
        this.lineNumber = 0;
    }

    public Map<Integer, Integer> parseIntegerMap(String line) {
        Map<Integer, Integer> row = new HashMap<>();
        for(int i=0; i<line.length(); i++) {
            Character c = line.charAt(i);
            row.put(i, Integer.parseInt(""+c));
        }
        return row;
    }

    protected abstract void processLine(String line);

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
