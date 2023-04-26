package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.MatchResult;

import org.springframework.stereotype.Component;

import name.stromkabelsalat.parsimony2mlf.parsimony.model.InnerOuter;

@Component
public class InnerOuterComponent {
	// Als Antwort( |\n)( )*auf:( |\n)( )*(.|\u0085)*( |\n).*( )*geschrieben( )?(
	// |\n)( )*von( |\n).*(( |\n).*am( |\n)( )*\d\d\.( |\n).*( |\n)( )*\d\d\d\d\d?(
	// |\n)( )*)?(at( |\n)|um( |\n))?( )*\d\d:\d\d:(\d\d:)?
	public static final String ANTWORT_REGEX = "Als Antwort( |\\n)( )*auf:( |\\n)( )*(.|\\u0085)*( |\\n).*( )*geschrieben( )?( |\\n)( )*von( |\\n).*(( |\\n).*am( |\\n)( )*\\d\\d\\.( |\\n).*( |\\n)( )*\\d\\d\\d\\d\\d?( |\\n)( )*)?(at( |\\n)|um( |\\n))?( )*\\d\\d:\\d\\d:(\\d\\d:)?";

	// Geschrieben( |\n)( )*von( |\n).*(( |\n).*am( |\n)( )*\d\d\.( |\n).*( |\n)(
	// )*\d\d\d\d\d?( |\n)( )*)?(at( |\n)|um( |\n))?( )*\d\d:\d\d:(\d\d:)?
	public static final String GESCHRIEBEN_REGEX = "Geschrieben( |\\n)( )*von( |\\n).*(( |\\n).*am( |\\n)( )*\\d\\d\\.( |\\n).*( |\\n)( )*\\d\\d\\d\\d\\d?( |\\n)( )*)?(at( |\\n)|um( |\\n))?( )*\\d\\d:\\d\\d:(\\d\\d:)?";

	// <center><img src=".*"><\/center>
	public static final String IMG_REGEX = "<center><img src=\".*\"><\\/center>";

	// <ul>(\n( )*)?<li><a href=".*"(
	// target=(_blank|_top))?>.*(\n)?.*<\/a>(<\/li>)?(\n( )*)?<\/ul>
	public static final String URL_REGEX = "<ul>(\\n( )*)?<li><a href=\".*\"( target=(_blank|_top))?>.*(\\n)?.*<\\/a>(<\\/li>)?(\\n( )*)?<\\/ul>";

	public InnerOuter innerOuter(final String complete, final String regex) {
		String inner = null;

		try (Scanner scanner = new Scanner(complete)) {
			inner = scanner.findAll(regex).findFirst().map(MatchResult::group).orElse(null);
		}

		final String outer = Optional
				.ofNullable(inner)
				.map(innerCandidate -> complete.replace(innerCandidate, ""))
				.orElse(complete);

		return new InnerOuter(inner, outer);
	}
}
