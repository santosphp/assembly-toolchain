package app.toolchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tables {

	public enum ModoRelocabilidade {
	    ABSOLUTO,
	    RELATIVO
	}
	public record definitionEntry(int endereco, ModoRelocabilidade modo) {}
	public record useEntry(int lc, ModoRelocabilidade modo, boolean signal) {}
	
	private List<Map<String,definitionEntry>> definitionTables;
	private List<Map<String,useEntry>> useTables;

	public Tables()
	{
		this.definitionTables = new ArrayList<>();
		this.useTables = new ArrayList<>();
	}
	
	public void cleanTables()
	{
		this.definitionTables.clear();
		this.useTables.clear();
	}

	// Getters and Setters
	public List<Map<String, definitionEntry>> getDefinitionTables() {
		return definitionTables;
	}

	public void setDefinitionTable(List<Map<String, definitionEntry>> definitionTables) {
		this.definitionTables = definitionTables;
	}

	public List<Map<String, useEntry>> getUseTables() {
		return useTables;
	}

	public void setUseTable(List<Map<String, useEntry>> useTables) {
		this.useTables = useTables;
	}
}
