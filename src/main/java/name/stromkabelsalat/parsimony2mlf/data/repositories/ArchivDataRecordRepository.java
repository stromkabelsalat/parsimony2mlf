package name.stromkabelsalat.parsimony2mlf.data.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import name.stromkabelsalat.parsimony2mlf.data.components.ArchivDataRecordComponent;
import name.stromkabelsalat.parsimony2mlf.data.entities.ArchivDataRecord;

@Repository
public class ArchivDataRecordRepository {
	private final List<ArchivDataRecord> archivDataRecords;

	public ArchivDataRecordRepository(final ArchivDataRecordComponent archivDataRecordComponent,
			final DataRecordRepository dataRecordRepository) {
		this.archivDataRecords = dataRecordRepository
				.findAll()
				.stream()
				.filter(dataRecord -> dataRecord.nameComponents().size() == 6)
				.filter(dataRecord -> "parsimony".equals(dataRecord.nameComponents().get(1)))
				.filter(dataRecord -> dataRecord.nameComponents().get(2).startsWith("parsimony"))
				.filter(dataRecord -> "forum".equals(dataRecord.nameComponents().get(3)))
				.filter(dataRecord -> dataRecord.nameComponents().get(4).startsWith("forum"))
				.map(archivDataRecordComponent::archivDataRecord)
				.toList();
	}

	public List<ArchivDataRecord> findAll() {
		return this.archivDataRecords;
	}
}
