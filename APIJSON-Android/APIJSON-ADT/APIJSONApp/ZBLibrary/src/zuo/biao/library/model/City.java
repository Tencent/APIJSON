package zuo.biao.library.model;

import java.io.Serializable;

public class City implements Serializable {

    private static final long serialVersionUID = 1L;
    private String province;
    private String city;
    //    private String number;
    private String firstPY;
    private String allPY;
    private String allFristPY;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    public City() {
        // TODO Auto-generated constructor stub
    }

    public City(String province, String city,Double lat, Double lon) {
        super();
        this.province = province;
        this.city = city;
//        this.number = number;
//        this.firstPY = firstPY;
//        this.allPY = allPY;
//        this.allFristPY = allFristPY;
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    public String getNumber() {
//        return number;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }

    public String getFirstPY() {
        return firstPY;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }

    public String getAllPY() {
        return allPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public String getAllFristPY() {
        return allFristPY;
    }

    public void setAllFristPY(String allFristPY) {
        this.allFristPY = allFristPY;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "City [province=" + province + ", city=" + city
                + ", firstPY=" + firstPY + ", allPY=" + allPY
                + ", allFristPY=" + allFristPY + "]";
    }

}
