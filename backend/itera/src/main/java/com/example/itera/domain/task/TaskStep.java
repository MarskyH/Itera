package com.example.itera.domain.task;

public enum TaskStep {
    R("Requisitos"),
    P("Projeto"),
    B("Desenvolvimento Back-End"),
    F("Desenvolvimento Front-End"),
    T("Testes");

    private String step;



    TaskStep(String step){
        this.step = step;
    }

    public String getTaskStep() {
        return step;
    }

    public static TaskStep fromString(String step) {
        try {
            return TaskStep.valueOf(step.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (TaskStep taskStep : TaskStep.values()) {
                if (taskStep.step.equalsIgnoreCase(step)) {
                    return taskStep;
                }
            }
            throw new IllegalArgumentException("No enum constant TaskStep." + step);
        }
    }
}
/*
[
        {
        "taskStep": "R",
        "deadline": 5,
        "user_id": "123456",
        "task_id": "789012"
        },
        {
        "taskStep": "P",
        "deadline": 10,
        "user_id": "654321",
        "task_id": "210987"
        },
        {
        "taskStep": "B",
        "deadline": 15,
        "user_id": "987654",
        "task_id": "321098"
        }
]
*/