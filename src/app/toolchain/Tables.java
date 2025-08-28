package app.toolchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tables {

	public enum ModoRelocabilidade {
	    ABSOLUTO,
	    RELATIVO
	}
	public enum Sinal {
	    POSITIVO,
	    NEGATIVO
	}
	public record definitionEntry(int endereco, ModoRelocabilidade modo) {}
	public record useEntry(String symbol, int lc, ModoRelocabilidade modo, Sinal signal) {}
	
	private List<Map<String,definitionEntry>> definitionTables;
	private List<List<useEntry>> useTables;
	private List<List<Integer>> relocationTables; 
	
	public record relocationEntry(String symbolName, int offset) {}

	private List<relocationEntry> finalRelocationTable;

	public Tables()
	{
		this.definitionTables = new ArrayList<>();
		this.useTables = new ArrayList<>();
		this.relocationTables = new ArrayList<>();
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

	public List<List<useEntry>> getUseTables() {
		return useTables;
	}

	public void setUseTable(List<List<useEntry>> useTables) {
		this.useTables = useTables;
	}

	public List<List<Integer>> getRelocationTables() {
		return relocationTables;
	}

	public void setRelocationTables(List<List<Integer>> relocationTables) {
		this.relocationTables = relocationTables;
	}

	public List<relocationEntry> getFinalRelocationTable() {
		return finalRelocationTable;
	}

	public void setFinalRelocationTable(List<relocationEntry> finalRelocationTable) {
		this.finalRelocationTable = finalRelocationTable;
	}
	
}
