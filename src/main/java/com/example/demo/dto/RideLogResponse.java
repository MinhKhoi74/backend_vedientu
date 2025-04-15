package com.example.demo.dto;

public class RideLogResponse {
    private Long id;
    private String userName;
    private Long ticketId;
    private String driverName;
    private String busCode;
    private String route;
    private String rideTime;
    private String status;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getBusCode() { return busCode; }
    public void setBusCode(String busCode) { this.busCode = busCode; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public String getRideTime() { return rideTime; }
    public void setRideTime(String rideTime) { this.rideTime = rideTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
