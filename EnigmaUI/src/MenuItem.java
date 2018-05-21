public class MenuItem {

    private int serialNumber;
    private String description;

    public MenuItem(int serialNumber, String description)
    {
        this.serialNumber = serialNumber;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getSerialNumber() + " - " + this.getDescription();
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

}
