package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Courier {

    @JsonProperty("courierId")
    private String courierId = null;

    @JsonProperty("cityRegion")
    private String cityRegion = null;

    @JsonProperty("timeZone")
    private String timeZone = null;

    @JsonProperty("courierInfo")
    private CourierInfo courierInfo = null;

    public Courier courierId(String courierId) {
        this.courierId = courierId;
        return this;
    }

    public Courier cityRegion(String cityRegion) {
        this.cityRegion = cityRegion;
        return this;
    }

    public Courier timeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public Courier courierInfo(CourierInfo courierInfo) {
        this.courierInfo = courierInfo;
        return this;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getCityRegion() {
        return cityRegion;
    }

    public void setCityRegion(String cityRegion) {
        this.cityRegion = cityRegion;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public CourierInfo getCourierInfo() {
        return courierInfo;
    }

    public void setCourierInfo(CourierInfo courierInfo) {
        this.courierInfo = courierInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Courier courier = (Courier) o;
        return Objects.equals(this.courierId, courier.courierId) &&
                Objects.equals(this.cityRegion, courier.cityRegion) &&
                Objects.equals(this.timeZone, courier.timeZone) &&
                Objects.equals(this.courierInfo, courier.courierInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courierId, cityRegion, timeZone, courierInfo);
    }

    @Override
    public String toString() {
        return "Courier{" +
                "courierId='" + courierId + '\'' +
                ", cityRegion='" + cityRegion + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", courierInfo='" + courierInfo + '\'' +
                '}';
    }

}
