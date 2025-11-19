package com.cyberrange.api.bean;
import java.util.List;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.LabSteps;
import com.cyberrange.api.model.EvaluationQuestions;
import com.cyberrange.api.model.EvaluationAnswers;


public class LabDetails {
    private List<LabSteps> labSteps= null;
	private Modules module =  null;
    private List<EvaluationQuestions> ques = null;
    //private List<EvaluationAnswers> ans = null;

    public List<LabSteps> getLabSteps() {
		return labSteps;
	}
	public void setLabSteps(List<LabSteps> labSteps) {
		this.labSteps = labSteps;
	}
	public Modules getModule() {
		return module;
	}
	public void setModule(Modules module) {
		this.module = module;
	}

    public List<EvaluationQuestions> getEvaluationQuestions(){
        return ques;
    }

    public void setEvaluationQuestions(List<EvaluationQuestions> q){
        this.ques = q;
    }


    
}
