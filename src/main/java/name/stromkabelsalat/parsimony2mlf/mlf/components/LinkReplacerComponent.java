package name.stromkabelsalat.parsimony2mlf.mlf.components;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import name.stromkabelsalat.parsimony2mlf.components.LeftRightComponent;
import name.stromkabelsalat.parsimony2mlf.model.LeftRight;
import name.stromkabelsalat.parsimony2mlf.parsimony.repositories.ParsimonyMessageRepository;

@Component
@RequiredArgsConstructor
public class LinkReplacerComponent {
	private final MlfValuesComponent mlfValuesComponent;
	private final LeftRightComponent leftRightComponent;
	private final ParsimonyMessageRepository parsimonyMessageRepository;

	public String replaceCompleteMessageLinks(final String text, final short epochNum) {
		// http:\/\/(www\.)?(f[1-9]\\d*\.)?(forum\.)?parsimony.net(\/forum)?\/forum[1-9]\\d*\/messages\/[1-9]\\d{0,4}\.htm
		final String regex = "http:\\/\\/(www\\.)?(f[1-9]\\d*\\.)?(forum\\.)?parsimony.net(\\/forum)?\\/forum[1-9]\\d*\\/messages\\/[1-9]\\d{0,4}\\.htm";

		final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(text);

		String result = text;
		while (matcher.find()) {
			final String group = matcher.group();

			final String boardNumMessagesNum = group
					.toLowerCase()
					.replaceAll("f[1-9]\\d*\\.", "")
					.replace("http://", "")
					.replace("www", "")
					.replace("parsimony.net", "")
					.replace("forum", "")
					.replace(".htm", "")
					.replace("/", "")
					.replace(".", "");

			final LeftRight boardNumNumLeftRight = this.leftRightComponent.leftRight(boardNumMessagesNum, "messages");
			final String boardNumString = boardNumNumLeftRight.left();
			final String numString = boardNumNumLeftRight.right();

			final Long boardNum = Long.parseLong(boardNumString);
			final Integer num = Integer.parseInt(numString);

			if (this.parsimonyMessageRepository.existsByEpochNumAndBoardNum(epochNum, boardNum)) {
				final Integer id = this.mlfValuesComponent.id(epochNum, boardNum, num);
				final String replacement = String.format("index.php?id=%d", id);

				result = result.replace(group, replacement);
			}
		}

		return result;
	}

	public String replaceCompleteBoardLinks(final String text, final short epochNum) {
		// http:\/\/(www\.)?(f[1-9]\\d*\.)?(forum\.)?parsimony.net(\/forum)?\/forum[1-9]\\d*\/?
		final String regex = "http:\\/\\/(www\\.)?(f[1-9]\\d*\\.)?(forum\\.)?parsimony.net(\\/forum)?\\/forum[1-9]\\d*\\/?";

		final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(text);

		String result = text;
		while (matcher.find()) {
			final String group = matcher.group();

			final String boardNumString = group
					.toLowerCase()
					.replaceAll("f[1-9]\\d*\\.", "")
					.replace("http://", "")
					.replace("www", "")
					.replace("parsimony.net", "")
					.replace("forum", "")
					.replace("/", "")
					.replace(".", "");

			final Long boardNum = Long.parseLong(boardNumString);

			if (this.parsimonyMessageRepository.existsByEpochNumAndBoardNum(epochNum, boardNum)) {
				final Integer category = this.mlfValuesComponent.category(epochNum, boardNum);
				final String replacement = String.format("index.php?mode=index&category=%d", category);

				result = result.replace(group, replacement);
			}
		}

		return result;
	}

	public String replaceSimpleMessageLinkTexts(final String text, final short epochNum, final long boardNum) {
		// \[link][1-9]\\d{0,4}\.htm\[\/link]
		final String regex = "\\[link][1-9]\\d{0,4}\\.htm\\[\\/link]";

		final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(text);

		String result = text;
		while (matcher.find()) {
			final String group = matcher.group();

			final String numString = group.toLowerCase().replace("[link]", "").replace(".htm", "").replace("[/link]",
					"");
			final Integer num = Integer.parseInt(numString);

			final Integer id = this.mlfValuesComponent.id(epochNum, boardNum, num);
			final String replacement = String.format("[link]index.php?id=%d[/link]", id);

			result = result.replace(group, replacement);
		}

		return result;
	}

	public String replaceSimpleMessageLinkUrls(final String text, final short epochNum, final long boardNum) {
		// \[link=[1-9]\\d{0,4}\.htm\]
		final String regex = "\\[link=[1-9]\\d{0,4}\\.htm\\]";

		final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(text);

		String result = text;
		while (matcher.find()) {
			final String group = matcher.group();

			final String numString = group.toLowerCase().replace("[link=", "").replace(".htm]", "");
			final Integer num = Integer.parseInt(numString);

			final Integer id = this.mlfValuesComponent.id(epochNum, boardNum, num);
			final String replacement = String.format("[link=index.php?id=%d]", id);

			result = result.replace(group, replacement);
		}

		return result;
	}

	public String replaceLinks(final String text, final short epochNum, final long boardNum) {
		return Optional
				.of(text)
				.map(result -> this.replaceCompleteMessageLinks(result, epochNum))
				.map(result -> this.replaceCompleteBoardLinks(result, epochNum))
				.map(result -> this.replaceSimpleMessageLinkTexts(result, epochNum, boardNum))
				.map(result -> this.replaceSimpleMessageLinkUrls(result, epochNum, epochNum))
				.orElse(null);
	}
}
