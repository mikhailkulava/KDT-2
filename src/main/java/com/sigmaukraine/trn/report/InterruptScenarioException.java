package com.sigmaukraine.trn.report;

public class InterruptScenarioException extends RuntimeException {

    private static final long serialVersionUID = 5118522519220527563L;

    public InterruptScenarioException(String description, Throwable cause) {
        super(description, cause);
    }

    public InterruptScenarioException(Throwable cause) {
        super(cause);
    }

    public InterruptScenarioException(String description) {
        super(description);
    }
}
