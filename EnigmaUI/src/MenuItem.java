public class MenuItem {

    private String name;
    private int serialNumber;
    private String description;

    public MenuItem(String name, int serialNumber, String description)
    {
        this.name = name;
        this.serialNumber = serialNumber;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getSerialNumber() + " - " + this.getDescription();
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

}
