package org.techtown.evtalk.ui.message;

public class RoomData {
    private String username1;
    private String username2;
    private String roomNumber;
    private String content; //마지막 채팅 내용을 위한 변수

    public RoomData(String username1, String username2, String roomNumber) {
        this.username1 = username1;
        this.username2 = username2;
        this.roomNumber = roomNumber;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username) {
        this.username1 = username;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
