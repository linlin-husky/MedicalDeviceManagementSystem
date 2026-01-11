/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.models.room;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rajath
 */
public class RoomDirectory {
    private List<Room> roomList;

    public RoomDirectory() {
        roomList = new ArrayList<>();
    }

    public Room addNewRoom() {
        Room newRoom = new Room();
        roomList.add(newRoom);
        return newRoom;
    }

    public void addRoom(Room room) {
        if (room != null && !roomList.contains(room)) {
            roomList.add(room);
        }
    }

    public boolean removeRoom(Room room) {
        return roomList.remove(room);
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public Room findRoomByNumber(int number) {
        for (Room room : roomList) {
            if (room.getNumber() == number) {
                return room;
            }
        }
        return null;
    }
}