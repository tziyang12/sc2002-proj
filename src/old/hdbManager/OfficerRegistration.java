package old.hdbManager;

class OfficerRegistration {
    private Officer officer;
    private String status = "Pending";

    public OfficerRegistration(Officer officer) {
        this.officer = officer;
    }

    public void approve() { status = "Approved"; }
    public void reject() { status = "Rejected"; }
    public Officer getOfficer() { return officer; }
    public String getStatus() { return status; }
}