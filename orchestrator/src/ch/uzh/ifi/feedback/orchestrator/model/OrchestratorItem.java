package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;

public abstract class OrchestratorItem<T> extends ItemBase<T> implements IOrchestratorItem<T> {

	@DbAttribute("current_version")
	private transient boolean currentVersion;
	
	@DbAttribute("created_at")
	private Timestamp createdAt;
	
	@Override
	public abstract Integer getId();

	@Override
	public abstract void setId(Integer id);

	public boolean isCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(boolean currentVersion) {
		this.currentVersion = currentVersion;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}
