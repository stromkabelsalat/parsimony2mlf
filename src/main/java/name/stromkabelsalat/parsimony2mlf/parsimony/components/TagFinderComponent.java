package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyMessage;
import name.stromkabelsalat.parsimony2mlf.parsimony.repositories.ParsimonyMessageRepository;

@Component
@ConditionalOnProperty(name = "name.stromkabelsalat.parsimony2mlf.find-tags")
@Slf4j
public class TagFinderComponent {
	public TagFinderComponent(final ParsimonyMessageRepository parsimonyMessageRepository) throws IOException {
		final List<String> tagNames = new ArrayList<>();
		final StringBuilder stringBuilder = new StringBuilder();

		ParsimonyMessage lastMessageDataRecord = null;

		for (final ParsimonyMessage parsimonyMessage : parsimonyMessageRepository.findAll()) {
			final Short epochNum = parsimonyMessage.getEpochNum();
			final Long boardNum = parsimonyMessage.getBoardNum();
			final Integer num = parsimonyMessage.getNum();

			log.info("Finding tags in {} {} {}", epochNum, boardNum, num);

			final String body = parsimonyMessage.getBody();

			for (final Element element : Jsoup.parse(body).body().getAllElements()) {
				final String tagName = element.tagName();

				if (!tagNames.contains(tagName)) {
					if (!parsimonyMessage.equals(lastMessageDataRecord)) {
						stringBuilder.append(String.format("# %d %d %d", epochNum, boardNum, num))
								.append(System.lineSeparator());

						lastMessageDataRecord = parsimonyMessage;
					}

					stringBuilder.append(String.format("<%s", tagName)).append(System.lineSeparator());

					tagNames.add(tagName);
				}
			}
		}

		final Path path = Paths.get("found-tags.txt");
		try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
			bufferedWriter.write(stringBuilder.toString());
		}
	}
}
