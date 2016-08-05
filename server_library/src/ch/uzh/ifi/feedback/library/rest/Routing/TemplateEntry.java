package ch.uzh.ifi.feedback.library.rest.Routing;

public class TemplateEntry {
	
	private TemplatePart templatePart;
	private String value;
	
	public TemplateEntry(TemplatePart part, String value)
	{
		this.templatePart = part;
		this.value = value;
	}

	public TemplatePart getTemplatePart() {
		return templatePart;
	}

	public void setTemplatePart(TemplatePart templatePart) {
		this.templatePart = templatePart;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
