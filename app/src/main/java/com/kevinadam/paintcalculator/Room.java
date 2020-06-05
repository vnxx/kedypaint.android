package com.kevinadam.paintcalculator;

public class Room {
    private Integer Id;
    private String Name;
    private Double PaintNeeds, Length, Width, Height;

    public Room(Integer id, String name, Double length, Double width, Double height, Double paintNeeds) {
        Id = id;
        Name = name;
        Length = length;
        Width = width;
        Height = height;
        PaintNeeds = paintNeeds;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getLength() {
        return Length;
    }

    public void setLength(Double length) {
        Length = length;
    }

    public Double getWidth() {
        return Width;
    }

    public void setWidth(Double width) {
        Width = width;
    }

    public Double getHeight() {
        return Height;
    }

    public void setHeight(Double height) {
        Height = height;
    }

    public Double getPaintNeeds() {
        return PaintNeeds;
    }

    public void setPaintNeeds(Double paintNeeds) {
        PaintNeeds = paintNeeds;
    }
}
