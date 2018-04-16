public class MenuItem implements Runnable {

    private String name;
    private int serialNumber;
    private String description;
    private Runnable myFunc;

    public MenuItem(String name, int serialNumber, String description, Runnable func)
    {
        this.name = name;
        this.serialNumber = serialNumber;
        this.description = description;
        this.myFunc = func;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public void run() {
        this.myFunc.run();

    }
}
