package EnigmaCracking.Tasks;

public class ImpossibleTask extends Task {

    public ImpossibleTask(String initialCode, int taskSize){
        super(initialCode, taskSize);
    }

    @Override
    public int getTaskSize() {
        return super.getTaskSize();
    }

    @Override
    public String getInitialCode() {
        return super.getInitialCode();
    }
}
