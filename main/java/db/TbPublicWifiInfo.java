package db;

import java.util.ArrayList;
import java.util.List;

public class TbPublicWifiInfo {

    private static TbPublicWifiInfo tbPublicWifiInfo  = new TbPublicWifiInfo();
    List<Result> Result_list = new ArrayList<Result>();
    private static List<WIFIInfo> rows = new ArrayList<>();

    public static List<WIFIInfo> getRows() {
        return rows;
    }

    public static void setRows(List<WIFIInfo> rowsData) {
        rows = rowsData;
    }

    public static TbPublicWifiInfo getTbPublicWifiInfo() {
        return tbPublicWifiInfo;
    }

    public static void setTbPublicWifiInfo(TbPublicWifiInfo tbPublicWifiInfoData) {
        tbPublicWifiInfo = tbPublicWifiInfoData;
    }

    String list_total_count;

    public String  getList_total_count() {
        return list_total_count;
    }

    public void setList_total_count(String list_total_count) {
        this.list_total_count = list_total_count;
    }


    public List<Result> getResult_list() {
        return Result_list;
    }

    public void setResult_list(List<Result> result_list) {
        Result_list = result_list;
    }
}
