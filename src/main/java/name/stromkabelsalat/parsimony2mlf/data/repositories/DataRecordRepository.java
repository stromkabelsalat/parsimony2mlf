package name.stromkabelsalat.parsimony2mlf.data.repositories;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import name.stromkabelsalat.parsimony2mlf.data.entities.DataRecord;

@Repository
@Slf4j
public class DataRecordRepository {
	private final List<DataRecord> dataRecords = new ArrayList<>();

	public DataRecordRepository(@Value("${name.stromkabelsalat.parsimony2mlf.paths.data}") final Path dataPath)
			throws IOException {
		try (
				final InputStream inputStream = Files.newInputStream(dataPath);
				final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				final XZCompressorInputStream xzCompressorInputStream = new XZCompressorInputStream(
						bufferedInputStream);
				final TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
						xzCompressorInputStream)) {

			TarArchiveEntry tarArchiveEntry;
			while ((tarArchiveEntry = tarArchiveInputStream.getNextEntry()) != null) {
				final String name = tarArchiveEntry.getName();

				log.info("Reading {}", name);

				final InputStreamReader inputStreamReader = new InputStreamReader(tarArchiveInputStream,
						StandardCharsets.ISO_8859_1);
				final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				final String content = bufferedReader
						.lines()
						.collect(Collectors.joining(System.lineSeparator()));

				final DataRecord dataRecord = new DataRecord(name, content);

				this.dataRecords.add(dataRecord);
			}
		}

		Collections.sort(this.dataRecords);
	}

	public List<DataRecord> findAll() {
		return this.dataRecords;
	}
}
