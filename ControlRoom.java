public class ControlRoom {
    private WaterLevelObserver[] waterLevelObserverArray = new WaterLevelObserver[0];
    private int waterLevel;

    public void addWaterLevelObserver(WaterLevelObserver waterLevelObserver) {
        extendArray();
        waterLevelObserverArray[waterLevelObserverArray.length - 1] = waterLevelObserver;
        waterLevelObserver.update(waterLevel);
    }

    public void setWaterLevel(int waterLevel) {
        if (this.waterLevel != waterLevel) {
            this.waterLevel = waterLevel;
            notifyDevices();
        }
    }

    private void notifyDevices() {
        for (WaterLevelObserver ob : waterLevelObserverArray) {
            ob.update(waterLevel);
        }
    }

    private void extendArray() {
        WaterLevelObserver[] temp = new WaterLevelObserver[waterLevelObserverArray.length + 1];
        for (int i = 0; i < waterLevelObserverArray.length; i++) {
            temp[i] = waterLevelObserverArray[i];
        }
        waterLevelObserverArray = temp;
    }
}
