package com.cyberrange.api.bean;

public class PeerRanks {
    
    private Long moduleId;
    private String userName;
    private int points;
    private int totalPoints;

    public PeerRanks(Long moduleId,String userName,int points,int totalPoints){
        this.moduleId = moduleId;
        this.userName = userName;
        this.totalPoints = totalPoints;
        this.points = points;
    }

    public Long getModuleId(){ return this.moduleId;}
    public String getUserName(){ return this.userName;}
    public int getTotalPoints(){ return this.totalPoints;}
    public int getPoints(){ return this.points;}

}
