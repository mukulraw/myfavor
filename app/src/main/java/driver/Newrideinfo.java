
package driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Newrideinfo {

    @SerializedName("rideid")
    @Expose
    private String rideid;
    @SerializedName("pick_place_name")
    @Expose
    private String pickPlaceName;
    @SerializedName("drop_place_name")
    @Expose
    private String dropPlaceName;
    @SerializedName("route_km")
    @Expose
    private String routeKm;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("start_loc_point_lat")
    @Expose
    private String startLocPointLat;
    @SerializedName("start_loc_point_lng")
    @Expose
    private String startLocPointLng;
    @SerializedName("end_loc_point_lat")
    @Expose
    private String endLocPointLat;
    @SerializedName("end_loc_point_lng")
    @Expose
    private String endLocPointLng;
    @SerializedName("requrest_time")
    @Expose
    private String requrestTime;

    public String getRideid() {
        return rideid;
    }

    public void setRideid(String rideid) {
        this.rideid = rideid;
    }

    public String getPickPlaceName() {
        return pickPlaceName;
    }

    public void setPickPlaceName(String pickPlaceName) {
        this.pickPlaceName = pickPlaceName;
    }

    public String getDropPlaceName() {
        return dropPlaceName;
    }

    public void setDropPlaceName(String dropPlaceName) {
        this.dropPlaceName = dropPlaceName;
    }

    public String getRouteKm() {
        return routeKm;
    }

    public void setRouteKm(String routeKm) {
        this.routeKm = routeKm;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartLocPointLat() {
        return startLocPointLat;
    }

    public void setStartLocPointLat(String startLocPointLat) {
        this.startLocPointLat = startLocPointLat;
    }

    public String getStartLocPointLng() {
        return startLocPointLng;
    }

    public void setStartLocPointLng(String startLocPointLng) {
        this.startLocPointLng = startLocPointLng;
    }

    public String getEndLocPointLat() {
        return endLocPointLat;
    }

    public void setEndLocPointLat(String endLocPointLat) {
        this.endLocPointLat = endLocPointLat;
    }

    public String getEndLocPointLng() {
        return endLocPointLng;
    }

    public void setEndLocPointLng(String endLocPointLng) {
        this.endLocPointLng = endLocPointLng;
    }

    public String getRequrestTime() {
        return requrestTime;
    }

    public void setRequrestTime(String requrestTime) {
        this.requrestTime = requrestTime;
    }

}