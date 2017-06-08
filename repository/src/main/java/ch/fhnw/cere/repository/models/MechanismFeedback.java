package ch.fhnw.cere.repository.models;


import ch.fhnw.cere.repository.models.orchestrator.Mechanism;


public interface MechanismFeedback {
    public long getMechanismId();
    public void setMechanismId(long mechanismId);

    public Mechanism getMechanism();
    public void setMechanism(Mechanism mechanism);
}
