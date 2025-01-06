package net.herit.platform.platform_spring.common.logger;

public class Tracker {
    private String txId;
    private String callId;    
    private String jobId;
    private String toString;

    public Tracker() {
        this("", "", "");
    }

    public Tracker(String txId) {
        this(txId, "", "");
    }

    public Tracker(String txId, String callId) {
        this(txId, callId, "");
    }

    public Tracker(String txId, String callId, String jobId) {
        this.txId = txId;
        this.callId = callId;        
        this.jobId = jobId;        
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.toString = null;
        this.txId = txId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.toString = null;
        this.callId = callId;
    }
    
    public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Override
    public Tracker clone() {
        return new Tracker(this.txId, this.callId, this.jobId);
    }

    @Override
    public String toString() {
        if (this.toString == null) {
            this.toString = "[" + String.join("][", this.txId, this.callId, this.jobId) + "]";
        }
        return this.toString;
    }
}
