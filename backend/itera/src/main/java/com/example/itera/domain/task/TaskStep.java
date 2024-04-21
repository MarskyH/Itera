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