package be.hesest.tfe.entities;

import be.hesest.tfe.units.ProductCategoryUnit;
import be.hesest.tfe.units.ProductTypeUnit;
import jakarta.persistence.*;

@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductTypeUnit type;
    @Enumerated(EnumType.STRING)
    private ProductCategoryUnit category;
    private Float quantity;
    private String barCode;
    private String marketName;
    @Column(columnDefinition = "LONGTEXT")
    private String marketLink;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    private ProductEntity() {}

    public ProductEntity(String name, ProductTypeUnit type, ProductCategoryUnit category, Float quantity, String barCode, String marketName, String marketLink, byte[] image) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.quantity = quantity;
        this.barCode = barCode;
        this.marketName = marketName;
        this.marketLink = marketLink;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductTypeUnit getType() {
        return type;
    }

    public void setType(ProductTypeUnit type) {
        this.type = type;
    }

    public ProductCategoryUnit getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryUnit category) {
        this.category = category;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketLink() {
        return marketLink;
    }

    public void setMarketLink(String marketLink) {
        this.marketLink = marketLink;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}