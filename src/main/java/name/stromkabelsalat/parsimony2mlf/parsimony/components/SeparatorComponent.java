package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import org.springframework.stereotype.Component;

import lombok.Data;
import name.stromkabelsalat.parsimony2mlf.components.UniqueStringComponent;

@Component
@Data
public class SeparatorComponent {
	private final String separator;

	public SeparatorComponent(final UniqueStringComponent uniqueStringComponent) {
		this.separator = uniqueStringComponent.uniqueString();
	}
}
