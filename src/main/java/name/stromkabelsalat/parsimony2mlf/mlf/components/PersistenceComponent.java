package name.stromkabelsalat.parsimony2mlf.mlf.components;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import name.stromkabelsalat.parsimony2mlf.mlf.entities.AkismetRating;
import name.stromkabelsalat.parsimony2mlf.mlf.entities.B8Rating;
import name.stromkabelsalat.parsimony2mlf.mlf.entities.Entry;
import name.stromkabelsalat.parsimony2mlf.mlf.repositories.AkismetRatingRepository;
import name.stromkabelsalat.parsimony2mlf.mlf.repositories.B8RatingRepository;
import name.stromkabelsalat.parsimony2mlf.mlf.repositories.EntryRepository;
import name.stromkabelsalat.parsimony2mlf.parsimony.repositories.ParsimonyMessageRepository;

@Component
@Slf4j
public class PersistenceComponent {
	public PersistenceComponent(
			final ParsimonyMessageRepository parsimonyMessageRepository,
			final SqlScriptExecutorComponent sqlScriptExecutorComponent,
			final AkismetRatingComponent akismetRatingComponent,
			final B8RatingComponent b8RatingComponent,
			final EntryComponent entryComponent,
			final AkismetRatingRepository akismetRatingRepository,
			final B8RatingRepository b8RatingRepository,
			final EntryRepository entryRepository) {

		sqlScriptExecutorComponent.executeSqlScript("sql/truncate.sql");

		log.info("Creating entities");

		final List<Entry> entries = parsimonyMessageRepository
				.findAll()
				.stream()
				.map(entryComponent::entry)
				.sorted()
				.toList();

		final List<AkismetRating> akistmetRatings = parsimonyMessageRepository
				.findAll()
				.stream()
				.map(akismetRatingComponent::akismetRating)
				.sorted()
				.toList();

		final List<B8Rating> b8Ratings = parsimonyMessageRepository
				.findAll()
				.stream()
				.map(b8RatingComponent::b8Rating)
				.sorted()
				.toList();

		log.info("Persisting to database");
		entryRepository.saveAll(entries);
		akismetRatingRepository.saveAll(akistmetRatings);
		b8RatingRepository.saveAll(b8Ratings);

		sqlScriptExecutorComponent.executeSqlScript("sql/set-last-reply.sql");
	}
}
