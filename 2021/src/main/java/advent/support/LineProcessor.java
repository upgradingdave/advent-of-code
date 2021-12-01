package advent.support;

public abstract class LineProcessor<T> {

    T context;

    public LineProcessor(T context) {
        this.context = context;
    }

    protected abstract void processLine(String line);

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

}
