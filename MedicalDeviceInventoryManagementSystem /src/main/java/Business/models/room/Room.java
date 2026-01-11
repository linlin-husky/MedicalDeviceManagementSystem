/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.models.room;

import java.util.Objects;

public class Room {

    private int number;
    private String floor;
    private boolean isAvailable = true;
    private String roomType = "General OR";

    public Room() {
        this.isAvailable = true;
        this.roomType = "General OR";
    }

    public int getNumber() { return number; }
    public String getFloor() { return floor; }
    public boolean isAvailable() { return isAvailable; }
    public String getRoomType() { return roomType; }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setRoomType(String roomType) {
        this.roomType = (roomType != null && !roomType.trim().isEmpty()) ? roomType.trim() : "General OR";
    }

    @Override
    public String toString() {
        return String.format("Room %d - %s (%s)", number, (floor != null ? floor : "N/A"), (roomType != null ? roomType : "N/A"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return number == room.number && Objects.equals(floor, room.floor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, floor);
    }
}