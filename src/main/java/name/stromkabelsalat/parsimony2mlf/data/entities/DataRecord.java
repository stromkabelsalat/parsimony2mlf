package name.stromkabelsalat.parsimony2mlf.data.entities;

import java.util.Arrays;
import java.util.List;

public record DataRecord(String name, String content) implements Comparable<DataRecord> {
	public List<String> nameComponents() {
		final String[] nameComponentsArray = this.name.split("/");
		return Arrays.asList(nameComponentsArray);
	}

	@Override
	public int compareTo(final DataRecord other) {
		return this.name.compareTo(other.name);
	}
}
