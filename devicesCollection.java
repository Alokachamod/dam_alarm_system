public class devicesCollection {

    private device[] deviceArray = new device[0];


    public void addDevice(device newDevice) {

        device[] tempArray = new device[deviceArray.length + 1];

        for (int i = 0; i < deviceArray.length; i++) {
            tempArray[i] = deviceArray[i];
        }

        tempArray[tempArray.length - 1] = newDevice;

        deviceArray = tempArray;

    }


    public boolean updateDeviceName(String id, String newName) {
        for (int i = 0; i < deviceArray.length; i++) {
            if (deviceArray[i].getId().equalsIgnoreCase(id)) {
                deviceArray[i].setName(newName);
                return true;
            }
        }
        return false;
    }


    public boolean removeDevice(String id) {
        int targetIndex = -1;


        for (int i = 0; i < deviceArray.length; i++) {
            if (deviceArray[i].getId().equalsIgnoreCase(id)) {
                targetIndex = i;
                break;
            }
        }


        if (targetIndex == -1) {
            return false;
        }


        device[] temp = new device[deviceArray.length - 1];

        for (int i = 0, j = 0; i < deviceArray.length; i++) {
            if (i != targetIndex) {
                temp[j++] = deviceArray[i];
            }
        }

        deviceArray = temp;
        return true; 
    }

    public device[] getDeviceArray() {
        return deviceArray;
    }
}

