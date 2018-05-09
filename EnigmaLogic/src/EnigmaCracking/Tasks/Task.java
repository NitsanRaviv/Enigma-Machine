package EnigmaCracking.Tasks;

public class Task {

    protected String initialCode;
    protected int taskSize;

    public Task(String initialCode, int taskSize){
        this.initialCode = initialCode;
        this.taskSize = taskSize;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getInitialCode() {
        return initialCode;
    }
}
