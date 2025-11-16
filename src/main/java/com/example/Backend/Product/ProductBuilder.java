package com.example.Backend.Product;

public class ProductBuilder {
    private String name;
    private String category;
    private String imageUrl;
    private String description;
    private Double price;
    private String color;
    private String size;
    private String material;

    public ProductBuilder setName(String name){
        this.name=name;
        return this;
    }
    public ProductBuilder setCategory(String category){
        this.category=category;
        return this;
    }
    public ProductBuilder setImageUrl(String imageUrl){
        this.imageUrl=imageUrl;
        return this;
    }
    public ProductBuilder setDescription(String description){
        this.description=description;
        return this;
    }
    public ProductBuilder setPrice(Double price){
        this.price=price;
        return this;
    }
    public ProductBuilder setColor(String color){
        this.color=color;
        return this;
    }
    public ProductBuilder setSize(String size){
        this.size=size;
        return this;
    }
    public ProductBuilder setMaterial(String material){
        this.material=material;
        return this;
    }
    public Product build(){
        Product product=new Product();
        product.setName(this.name);
        product.setCategory(this.category);
        product.setImageUrl(this.imageUrl!=null?this.imageUrl:"https://placehold.co/400x300/f59e0b/fff?text="+name);
        product.setDescription(this.description!=null?this.description:"Custom "+category);
        product.setPrice(this.price);
        product.setColor(this.color);
        product.setSize(this.size);
        product.setMaterial(this.material);
        return product;
    }
}