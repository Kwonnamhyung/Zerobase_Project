package db;

public class WIFIInfoHistory {

    private String LAT;
    private String LNT;
    private String WORK_DTTM;

    private String RANK;

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLNT() {
        return LNT;
    }

    public void setLNT(String LNT) {
        this.LNT = LNT;
    }

    public String getWORK_DTTM() {
        return WORK_DTTM;
    }

    public void setWORK_DTTM(String WORK_DTTM) {
        this.WORK_DTTM = WORK_DTTM;
    }
}
