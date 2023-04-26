package name.stromkabelsalat.parsimony2mlf.mlf.components;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import name.stromkabelsalat.parsimony2mlf.mlf.entities.B8Rating;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyMessage;

@Component
@RequiredArgsConstructor
public class B8RatingComponent {
	private final MlfValuesComponent mlfValuesComponent;

	public B8Rating b8Rating(final ParsimonyMessage parsimonyMessage) {
		final Integer eid = this.mlfValuesComponent.id(parsimonyMessage.getEpochNum(), parsimonyMessage.getBoardNum(),
				parsimonyMessage.getNum());

		final B8Rating b8Rating = new B8Rating();
		b8Rating.setEid(eid);

		return b8Rating;
	}
}
