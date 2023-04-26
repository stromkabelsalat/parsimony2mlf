package name.stromkabelsalat.parsimony2mlf.data.repositories;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.stereotype.Repository;

import name.stromkabelsalat.parsimony2mlf.data.entities.DataRecord;

@Repository
public class DataRecordRepository
{
    private final List<DataRecord> dataRecords = new ArrayList<>();

    public DataRecordRepository() throws IOException
    {
        final ClassLoader classLoader = DataRecordRepository.class.getClassLoader();

        final InputStream inputStream = classLoader.getResourceAsStream("data.tgz");

        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        final GzipCompressorInputStream gzipCompressorInputStream = new GzipCompressorInputStream(bufferedInputStream);

        try (final TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gzipCompressorInputStream))
        {
            TarArchiveEntry tarArchiveEntry;
            while ((tarArchiveEntry = tarArchiveInputStream.getNextEntry()) != null)
            {
				final String name = tarArchiveEntry.getName();

				final StringBuilder stringBuilder = new StringBuilder();

				final InputStreamReader inputStreamReader = new InputStreamReader(tarArchiveInputStream,
				        StandardCharsets.ISO_8859_1);
				final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				String line;
				while ((line = bufferedReader.readLine()) != null)
				{
				    stringBuilder.append(line).append(System.lineSeparator());
				}

				final String content = stringBuilder.toString();

				final DataRecord dataRecord = new DataRecord(name, content);

				this.dataRecords.add(dataRecord);
            }
        }

        Collections.sort(this.dataRecords);
    }

    public List<DataRecord> findAll()
    {
        return this.dataRecords;
    }
}
