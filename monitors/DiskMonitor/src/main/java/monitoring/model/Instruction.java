package monitoring.model;

public class Instruction {

	private String instruction;
	private String label;
	
	public Instruction(String instruction, String label) {
		this.instruction = instruction;
		this.label = label;
	}
	
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
