package be.hesest.tfe.entities;

import be.hesest.tfe.units.TransportModeUnit;
import jakarta.persistence.*;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String password;
    private String name;
    private Long creationDate;
    @Column(columnDefinition = "LONGTEXT")
    private String availableMarkets;
    @Column(columnDefinition = "LONGTEXT")
    private String shoppingList;
    private String startAddressStreet;
    private String startAddressNumber;
    private String startAddressPostalCode;
    private String startAddressCity;
    private String startAddressCountry;
    private Double startAddressLatitude;
    private Double startAddressLongitude;
    private String endAddressStreet;
    private String endAddressNumber;
    private String endAddressPostalCode;
    private String endAddressCity;
    private String endAddressCountry;
    private Double endAddressLatitude;
    private Double endAddressLongitude;
    @Enumerated(EnumType.STRING)
    private TransportModeUnit transportMode;

    private UserEntity() {}

    public UserEntity(String email, String password, String name, Long creationDate, String availableMarkets, String shoppingList, String startAddressStreet, String startAddressNumber, String startAddressPostalCode, String startAddressCity, String startAddressCountry, Double startAddressLatitude, Double startAddressLongitude, String endAddressStreet, String endAddressNumber, String endAddressPostalCode, String endAddressCity, String endAddressCountry, Double endAddressLatitude, Double endAddressLongitude, TransportModeUnit transportMode) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.creationDate = creationDate;
        this.availableMarkets = availableMarkets;
        this.shoppingList = shoppingList;
        this.startAddressStreet = startAddressStreet;
        this.startAddressNumber = startAddressNumber;
        this.startAddressPostalCode = startAddressPostalCode;
        this.startAddressCity = startAddressCity;
        this.startAddressCountry = startAddressCountry;
        this.startAddressLatitude = startAddressLatitude;
        this.startAddressLongitude = startAddressLongitude;
        this.endAddressStreet = endAddressStreet;
        this.endAddressNumber = endAddressNumber;
        this.endAddressPostalCode = endAddressPostalCode;
        this.endAddressCity = endAddressCity;
        this.endAddressCountry = endAddressCountry;
        this.endAddressLatitude = endAddressLatitude;
        this.endAddressLongitude = endAddressLongitude;
        this.transportMode = transportMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public String getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(String availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public String getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(String shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getStartAddressStreet() {
        return startAddressStreet;
    }

    public void setStartAddressStreet(String startAddressStreet) {
        this.startAddressStreet = startAddressStreet;
    }

    public String getStartAddressNumber() {
        return startAddressNumber;
    }

    public void setStartAddressNumber(String startAddressNumber) {
        this.startAddressNumber = startAddressNumber;
    }

    public String getStartAddressPostalCode() {
        return startAddressPostalCode;
    }

    public void setStartAddressPostalCode(String startAddressPostalCode) {
        this.startAddressPostalCode = startAddressPostalCode;
    }

    public String getStartAddressCity() {
        return startAddressCity;
    }

    public void setStartAddressCity(String startAddressCity) {
        this.startAddressCity = startAddressCity;
    }

    public String getStartAddressCountry() {
        return startAddressCountry;
    }

    public void setStartAddressCountry(String startAddressCountry) {
        this.startAddressCountry = startAddressCountry;
    }

    public Double getStartAddressLatitude() {
        return startAddressLatitude;
    }

    public void setStartAddressLatitude(Double startAddressLatitude) {
        this.startAddressLatitude = startAddressLatitude;
    }

    public Double getStartAddressLongitude() {
        return startAddressLongitude;
    }

    public void setStartAddressLongitude(Double startAddressLongitude) {
        this.startAddressLongitude = startAddressLongitude;
    }

    public String getEndAddressStreet() {
        return endAddressStreet;
    }

    public void setEndAddressStreet(String endAddressStreet) {
        this.endAddressStreet = endAddressStreet;
    }

    public String getEndAddressNumber() {
        return endAddressNumber;
    }

    public void setEndAddressNumber(String endAddressNumber) {
        this.endAddressNumber = endAddressNumber;
    }

    public String getEndAddressPostalCode() {
        return endAddressPostalCode;
    }

    public void setEndAddressPostalCode(String endAddressPostalCode) {
        this.endAddressPostalCode = endAddressPostalCode;
    }

    public String getEndAddressCity() {
        return endAddressCity;
    }

    public void setEndAddressCity(String endAddressCity) {
        this.endAddressCity = endAddressCity;
    }

    public String getEndAddressCountry() {
        return endAddressCountry;
    }

    public void setEndAddressCountry(String endAddressCountry) {
        this.endAddressCountry = endAddressCountry;
    }

    public Double getEndAddressLatitude() {
        return endAddressLatitude;
    }

    public void setEndAddressLatitude(Double endAddressLatitude) {
        this.endAddressLatitude = endAddressLatitude;
    }

    public Double getEndAddressLongitude() {
        return endAddressLongitude;
    }

    public void setEndAddressLongitude(Double endAddressLongitude) {
        this.endAddressLongitude = endAddressLongitude;
    }

    public TransportModeUnit getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(TransportModeUnit transportMode) {
        this.transportMode = transportMode;
    }

}