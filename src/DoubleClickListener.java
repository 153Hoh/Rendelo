class DoubleClickListener {

    private int numOfClicks;
    private long clickTime;

    DoubleClickListener() {
        this.numOfClicks = 0;
        this.clickTime = 0;
    }

    boolean isDoubleClicked() {
        numOfClicks++;
        if (System.currentTimeMillis() - clickTime > 1000) {
            numOfClicks = 0;
        }
        if (numOfClicks >= 1) {
            return true;
        }
        clickTime = System.currentTimeMillis();
        return false;
    }

}
