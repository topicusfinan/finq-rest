package nl.finan.finq.websocket;

public enum StatusType {
    INITIAL_STATUS,
    BEFORE_STORY,
    BEFORE_SCENARIO,
    SUCCESSFUL_STEP,
    PENDING_STEP,
    FAILED_STEP,
    AFTER_SCENARIO,
    AFTER_STORY,
    FINAL_STATUS
}
