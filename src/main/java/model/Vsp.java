package model;

import java.util.List;

public class Vsp {
    private String vspName;
    private List<String> amdins;

    public String getVspName() {
        return vspName;
    }

    public void setVspName(String vspName) {
        this.vspName = vspName;
    }

    public List<String> getAmdins() {
        return amdins;
    }

    public void setAmdins(List<String> amdins) {
        this.amdins = amdins;
    }

    @Override
    public String toString() {
        return "Vsp{" +
                "vspName='" + vspName + '\'' +
                ", amdins=" + amdins +
                '}';
    }
}
