package classes;

public class Contact {

    // Attributes
    private String name_;
    private String deviceMAC_;
    private String deviceName_;

    public Contact(String name, String deviceMAC, String deviceName){
        this.name_ = name;
        this.deviceMAC_ = deviceMAC;
        this.deviceName_ = deviceName;
    }

    public String getName() {
        return name_;
    }

    public String getDeviceMAC() {
        return deviceMAC_;
    }

    public String getDeviceName() {
        return deviceName_;
    }

    public String toString(){
        return "Name : " + name_ + "\nMAC : " + deviceMAC_ + "\nDevice : " + deviceName_;
    }
}
