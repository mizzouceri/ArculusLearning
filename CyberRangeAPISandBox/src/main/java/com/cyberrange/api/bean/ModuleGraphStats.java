package com.cyberrange.api.bean;

public class ModuleGraphStats {
    private Long count;
    private Long moduleId;
    private int Month;
    private int Year;

    public ModuleGraphStats(Long count, Long moduleId, int Month, int Year){
        this.count = count;
        this.moduleId = moduleId;
        this.Month = Month;
        this.Year = Year;
    }

    public Long getCount(){ return this.count;}
    public Long getModuleId(){ return this.moduleId;}
    public int getYear(){ return this.Year;}
    public int getMonth(){ return this.Month;}

    
    public String toString(){
        return "ModuleGraphStats"+
        "Count : " + this.count + "\n"+
        "moduleId : " + this.moduleId + "\n"+
        "Month : " + this.Month + "\n"+
        "Year : " + this.Year + "\n";
    }
}
