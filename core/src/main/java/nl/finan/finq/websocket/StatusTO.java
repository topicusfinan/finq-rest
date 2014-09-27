package nl.finan.finq.websocket;

public class StatusTO {

    private Long reportId;
    private Object log;
    private StatusType statusType;

    public StatusTO(Long reportId, Object log, StatusType type) {
        this.reportId = reportId;
        this.log = log;
        this.statusType = type;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Object getLog() {
        return log;
    }

    public void setLog(Object log) {
        this.log = log;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

}
