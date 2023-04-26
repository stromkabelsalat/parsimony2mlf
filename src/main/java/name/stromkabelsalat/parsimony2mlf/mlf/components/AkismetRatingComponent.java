package name.stromkabelsalat.parsimony2mlf.mlf.components;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import name.stromkabelsalat.parsimony2mlf.mlf.entities.AkismetRating;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyMessage;

@Component
@RequiredArgsConstructor
public class AkismetRatingComponent {
	private final MlfValuesComponent mlfValuesComponent;

	public AkismetRating akismetRating(final ParsimonyMessage parsimonyMessage) {
		final Integer eid = this.mlfValuesComponent.id(parsimonyMessage.getEpochNum(), parsimonyMessage.getBoardNum(),
				parsimonyMessage.getNum());

		final AkismetRating akismetRating = new AkismetRating();
		akismetRating.setEid(eid);

		return akismetRating;
	}
}
