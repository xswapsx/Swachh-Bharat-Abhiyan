package com.appynitty.swachbharatabhiyanlibrary.pojos;

/**
 * Created by Ayan Dey on 28/5/19.
 */
public class QrLocationPojo {

    private String appId;
    private String gcType;
    private String referanceId;
    private String Name;
    private String NameMar;
    private String Address;
    private String Lat;
    private String Long;
    private Integer zoneId;
    private Integer wardId;
    private Integer areaId;
    private String houseNumber;
    private String userId;
    private String mobileno;
    private String date;
    private String OfflineID;
    private String QRCodeImage;
    private Integer new_const;
    private String geom;
    private String createTs;

    public String getQRCodeImage() {
        return QRCodeImage;
    }

    public void setQRCodeImage(String QRCodeImage) {
        this.QRCodeImage = QRCodeImage;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getGcType() {
        return gcType;
    }

    public void setGcType(String gcType) {
        this.gcType = gcType;
    }

    public String getReferanceId() {
        return referanceId;
    }

    public void setReferanceId(String referanceId) {
        this.referanceId = referanceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNameMar() {
        return NameMar;
    }

    public void setNameMar(String nameMar) {
        NameMar = nameMar;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOfflineID() {
        return OfflineID;
    }

    public void setOfflineID(String offlineID) {
        OfflineID = offlineID;
    }

    public Integer getNew_const() {
        return new_const;
    }

    public void setNew_const(Integer new_const) {
        this.new_const = new_const;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getCreateTs() {
        return createTs;
    }

    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

    @Override
    public String toString() {
        return "QrLocationPojo{" +
                "appId='" + appId + '\'' +
                ", gcType='" + gcType + '\'' +
                ", referanceId='" + referanceId + '\'' +
                ", Name='" + Name + '\'' +
                ", NameMar='" + NameMar + '\'' +
                ", Address='" + Address + '\'' +
                ", Lat='" + Lat + '\'' +
                ", Long='" + Long + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", wardId='" + wardId + '\'' +
                ", areaId='" + areaId + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", userId='" + userId + '\'' +
                ", mobileno='" + mobileno + '\'' +
                ", date='" + date + '\'' +
                ", OfflineID='" + OfflineID + '\'' +
                '}';
    }
}
