package com.cyberrange.api.bean;
import java.util.List;
import com.cyberrange.api.model.Modules;
public class ModuleStats {
    private Long count;
    private Long moduleId;
    private String moduleTitle;

    public ModuleStats(Long count, Long moduleId, String moduleTitle){
        this.count = count;
        this.moduleId = moduleId;
        this.moduleTitle = moduleTitle;
    }

    public Long getCount(){ return this.count;}
    public Long getModuleId(){ return this.moduleId;}
    public String getModuleTitle(){ return this.moduleTitle;}
}
