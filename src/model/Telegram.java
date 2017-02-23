package model;


import model.Location;

/**
 * Created by sbk on 23.02.17.
 */
public class Telegram {
    private double rssi;
    private double txPower;
    private double n;

    public Telegram(double rssi, double txPower, double n) {
        this.rssi = rssi;
        this.txPower = txPower;
        this.n = n;
    }

    public double getRssi() {
        return rssi;
    }

    public double getTxPower() {
        return txPower;
    }

    public double getN() {
        return n;
    }
}
