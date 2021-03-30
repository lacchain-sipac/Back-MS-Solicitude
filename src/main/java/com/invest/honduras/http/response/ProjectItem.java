package com.invest.honduras.http.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectItem {
	
    private boolean finalized;
    private boolean initialized;
    private String currentStep;
    private String creator;
    
}
