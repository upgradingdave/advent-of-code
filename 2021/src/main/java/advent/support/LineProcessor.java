package advent.support;

public abstract class LineProcessor<T> {

    T context;
    int lineNumber;
    int state;

    public LineProcessor(T context) {
        this.context = context;
        this.state = -1;
        this.lineNumber = 0;
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
