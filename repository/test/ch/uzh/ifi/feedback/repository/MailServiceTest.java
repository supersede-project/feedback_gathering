package ch.uzh.ifi.feedback.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;

import ch.uzh.ifi.feedback.library.mail.MailClient;
import ch.uzh.ifi.feedback.library.mail.MailConfiguration;
import ch.uzh.ifi.feedback.repository.mail.MailService;
import ch.uzh.ifi.feedback.repository.mail.RepositoryMailConfiguration;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;

public class MailServiceTest {

	private static final Log LOGGER = LogFactory.getLog(MailService.class);
	private MailService mailService;
	private Gson gson;
	private final String testConfigurationJson = "[\n" +
			"  {\n" +
			"    \"id\": 39,\n" +
			"    \"type\": \"PUSH\",\n" +
			"    \"mechanisms\": [\n" +
			"      {\n" +
			"        \"id\": 59,\n" +
			"        \"configurationsId\": 39,\n" +
			"        \"type\": \"TEXT_TYPE\",\n" +
			"        \"active\": true,\n" +
			"        \"order\": 1,\n" +
			"        \"canBeActivated\": false,\n" +
			"        \"parameters\": [\n" +
			"          {\n" +
			"            \"id\": 492,\n" +
			"            \"key\": \"maxLengthVisible\",\n" +
			"            \"value\": 1.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 485,\n" +
			"            \"key\": \"borderColor\",\n" +
			"            \"value\": \"#000000\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 479,\n" +
			"            \"key\": \"mandatory\",\n" +
			"            \"value\": 1.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 472,\n" +
			"            \"key\": \"title\",\n" +
			"            \"value\": \"Feedback\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 471,\n" +
			"            \"key\": \"maxLength\",\n" +
			"            \"value\": 1000.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 489,\n" +
			"            \"key\": \"fieldFontType\",\n" +
			"            \"value\": \"normal\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 484,\n" +
			"            \"key\": \"borderWidth\",\n" +
			"            \"value\": 1.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 494,\n" +
			"            \"key\": \"validateOnSkip\",\n" +
			"            \"value\": 1.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 488,\n" +
			"            \"key\": \"fieldFontSize\",\n" +
			"            \"value\": 14.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 475,\n" +
			"            \"key\": \"labelPositioning\",\n" +
			"            \"value\": \"left\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 478,\n" +
			"            \"key\": \"textareaFontColor\",\n" +
			"            \"value\": \"#7A7A7A\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 487,\n" +
			"            \"key\": \"fieldHeight\",\n" +
			"            \"value\": 90.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 486,\n" +
			"            \"key\": \"fieldWidth\",\n" +
			"            \"value\": 200.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 568,\n" +
			"            \"key\": \"hint\",\n" +
			"            \"value\": \"Ihr Feedback\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 491,\n" +
			"            \"key\": \"fieldBackgroundColor\",\n" +
			"            \"value\": \"#ffffff\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 476,\n" +
			"            \"key\": \"labelColor\",\n" +
			"            \"value\": \"#353535\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 570,\n" +
			"            \"key\": \"mandatoryReminder\",\n" +
			"            \"value\": \"Bitte das Feld ausfüllen\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 490,\n" +
			"            \"key\": \"fieldTextAlignment\",\n" +
			"            \"value\": \"left\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 569,\n" +
			"            \"key\": \"label\",\n" +
			"            \"value\": \"Bitte geben Sie Ihr Feedback ein\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 493,\n" +
			"            \"key\": \"validationRegex\",\n" +
			"            \"value\": \".\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 482,\n" +
			"            \"key\": \"undoSteps\",\n" +
			"            \"value\": 1.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 483,\n" +
			"            \"key\": \"clearInput\",\n" +
			"            \"value\": 0.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 477,\n" +
			"            \"key\": \"labelFontSize\",\n" +
			"            \"value\": 15.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 481,\n" +
			"            \"key\": \"undoEnabled\",\n" +
			"            \"value\": 1.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"id\": 60,\n" +
			"        \"configurationsId\": 39,\n" +
			"        \"type\": \"AUDIO_TYPE\",\n" +
			"        \"active\": false,\n" +
			"        \"order\": 2,\n" +
			"        \"canBeActivated\": false,\n" +
			"        \"parameters\": [\n" +
			"          {\n" +
			"            \"id\": 495,\n" +
			"            \"key\": \"maxTime\",\n" +
			"            \"value\": 30.0,\n" +
			"            \"defaultValue\": \"30.0\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"id\": 61,\n" +
			"        \"configurationsId\": 39,\n" +
			"        \"type\": \"SCREENSHOT_TYPE\",\n" +
			"        \"active\": true,\n" +
			"        \"order\": 3,\n" +
			"        \"canBeActivated\": false,\n" +
			"        \"parameters\": [\n" +
			"          {\n" +
			"            \"id\": 496,\n" +
			"            \"key\": \"title\",\n" +
			"            \"value\": \"Title for screenshot feedback\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 579,\n" +
			"            \"key\": \"cropTitle\",\n" +
			"            \"value\": \"Ausschneiden\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2017-02-25 09:47:34.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 580,\n" +
			"            \"key\": \"annotationTitle\",\n" +
			"            \"value\": \"Bereiche markieren\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2017-02-25 09:47:34.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 578,\n" +
			"            \"key\": \"zoomTitle\",\n" +
			"            \"value\": \"Zoom\",\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2017-02-25 09:47:34.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 581,\n" +
			"            \"key\": \"textTitle\",\n" +
			"            \"value\": \"Text hinzufügen\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2017-02-25 09:47:34.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 582,\n" +
			"            \"key\": \"freehandMouseover\",\n" +
			"            \"value\": \"Klicken um zu Zeichnen\",\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2017-03-02 10:25:24.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 497,\n" +
			"            \"key\": \"defaultPicture\",\n" +
			"            \"value\": \"lastScreenshot\",\n" +
			"            \"defaultValue\": \"noImage\",\n" +
			"            \"editableByUser\": true,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"id\": 62,\n" +
			"        \"configurationsId\": 39,\n" +
			"        \"type\": \"RATING_TYPE\",\n" +
			"        \"active\": true,\n" +
			"        \"order\": 4,\n" +
			"        \"canBeActivated\": false,\n" +
			"        \"parameters\": [\n" +
			"          {\n" +
			"            \"id\": 499,\n" +
			"            \"key\": \"ratingIcon\",\n" +
			"            \"value\": \"star\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 501,\n" +
			"            \"key\": \"defaultRating\",\n" +
			"            \"value\": 0.0,\n" +
			"            \"defaultValue\": \"0.0\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 571,\n" +
			"            \"key\": \"title\",\n" +
			"            \"value\": \"Bewerten Sie die aktuelle Ansicht\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 500,\n" +
			"            \"key\": \"maxRating\",\n" +
			"            \"value\": 5.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"id\": 63,\n" +
			"        \"configurationsId\": 39,\n" +
			"        \"type\": \"AUDIO_TYPE\",\n" +
			"        \"active\": false,\n" +
			"        \"order\": 2,\n" +
			"        \"canBeActivated\": false,\n" +
			"        \"parameters\": [\n" +
			"          {\n" +
			"            \"id\": 502,\n" +
			"            \"key\": \"maxTime\",\n" +
			"            \"value\": 30.0,\n" +
			"            \"defaultValue\": \"30.0\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-10-05 12:50:56.0\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"id\": 64,\n" +
			"        \"configurationsId\": 39,\n" +
			"        \"type\": \"CATEGORY_TYPE\",\n" +
			"        \"active\": true,\n" +
			"        \"order\": 5,\n" +
			"        \"canBeActivated\": false,\n" +
			"        \"parameters\": [\n" +
			"          {\n" +
			"            \"id\": 573,\n" +
			"            \"key\": \"mandatoryReminder\",\n" +
			"            \"value\": \"Bitte mindestens eine Kategorie wählen\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2016-10-24 01:45:36.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 508,\n" +
			"            \"key\": \"options\",\n" +
			"            \"value\": [\n" +
			"              {\n" +
			"                \"id\": 574,\n" +
			"                \"key\": \"BUG_CATEGORY\",\n" +
			"                \"value\": \"Fehler\",\n" +
			"                \"defaultValue\": \"en\",\n" +
			"                \"editableByUser\": false,\n" +
			"                \"language\": \"de\",\n" +
			"                \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"              },\n" +
			"              {\n" +
			"                \"id\": 575,\n" +
			"                \"key\": \"FEATURE_REQUEST_CATEGORY\",\n" +
			"                \"value\": \"Neue Funktion gewünscht\",\n" +
			"                \"defaultValue\": \"en\",\n" +
			"                \"editableByUser\": false,\n" +
			"                \"language\": \"de\",\n" +
			"                \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"              },\n" +
			"              {\n" +
			"                \"id\": 576,\n" +
			"                \"key\": \"GENERAL_CATEGORY\",\n" +
			"                \"value\": \"Allgemeines Feedback\",\n" +
			"                \"defaultValue\": \"en\",\n" +
			"                \"editableByUser\": false,\n" +
			"                \"language\": \"de\",\n" +
			"                \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"              }\n" +
			"            ],\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-10-24 01:45:36.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 506,\n" +
			"            \"key\": \"mandatory\",\n" +
			"            \"value\": 1.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-10-24 01:45:36.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 504,\n" +
			"            \"key\": \"ownAllowed\",\n" +
			"            \"value\": 0.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-10-24 01:45:36.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 505,\n" +
			"            \"key\": \"multiple\",\n" +
			"            \"value\": 0.0,\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-10-24 01:45:36.0\"\n" +
			"          },\n" +
			"          {\n" +
			"            \"id\": 572,\n" +
			"            \"key\": \"title\",\n" +
			"            \"value\": \"Bitte wählen Sie eine der folgenden Kategorien\",\n" +
			"            \"defaultValue\": \"\",\n" +
			"            \"editableByUser\": false,\n" +
			"            \"language\": \"de\",\n" +
			"            \"createdAt\": \"2016-10-24 01:45:36.0\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"id\": 65,\n" +
			"        \"configurationsId\": 39,\n" +
			"        \"type\": \"ATTACHMENT_TYPE\",\n" +
			"        \"active\": true,\n" +
			"        \"order\": 7,\n" +
			"        \"canBeActivated\": false,\n" +
			"        \"parameters\": [\n" +
			"          {\n" +
			"            \"id\": 512,\n" +
			"            \"key\": \"title\",\n" +
			"            \"value\": \"You can also attach some files to your feedback\",\n" +
			"            \"language\": \"en\",\n" +
			"            \"createdAt\": \"2016-10-24 01:45:37.0\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"      }\n" +
			"    ],\n" +
			"    \"generalConfiguration\": {\n" +
			"      \"id\": 61,\n" +
			"      \"parameters\": [\n" +
			"        {\n" +
			"          \"id\": 535,\n" +
			"          \"key\": \"feedbackMailKeyName\",\n" +
			"          \"value\": \"energiesparkonto@co2online.de\",\n" +
			"          \"language\": \"en\",\n" +
			"          \"createdAt\": \"2016-12-04 08:41:27.0\"\n" +
			"        },\n" +
			"        {\n" +
			"          \"id\": 577,\n" +
			"          \"key\": \"successDialog\",\n" +
			"          \"value\": 1.0,\n" +
			"          \"language\": \"de\",\n" +
			"          \"createdAt\": \"2017-02-26 09:24:16.0\"\n" +
			"        }\n" +
			"      ],\n" +
			"      \"name\": \"Senercon \",\n" +
			"      \"createdAt\": \"2016-12-04 08:39:54.0\"\n" +
			"    },\n" +
			"    \"createdAt\": \"2016-11-09 09:53:39.0\"\n" +
			"  }\n" +
			"]";

	@Before
	public void initialize() {
		gson = new GsonBuilder()
				.setPrettyPrinting()
				.setDateFormat("yyyy-MM-dd hh:mm:ss.S")
				.create();

		MailConfiguration mailConfiguration = new RepositoryMailConfiguration();
		MailClient mailClient = new MailClient(mailConfiguration);
		mailService = new MailService(mailClient);
	}

	@Ignore
	@Test
	public void testNotifyOfFeedback() {
		List<TextFeedback> textFeedbacks = new ArrayList<TextFeedback>();
		textFeedbacks.add(new TextFeedback(null, null, "Hello, this is my feedback", 1));
		textFeedbacks.add(new TextFeedback(null, null, "ronnieschaniel@gmail.com", 2));

		List<RatingFeedback> ratingFeedbacks = new ArrayList<RatingFeedback>();
		ratingFeedbacks.add(new RatingFeedback(null, "Please rate your experience on this site", 3, null, 9));
		ratingFeedbacks.add(new RatingFeedback(null, "Please rate another thing", 5, null, 9));

		List<CategoryFeedback> categoryFeedbacks = new ArrayList<CategoryFeedback>();
		categoryFeedbacks.add(new CategoryFeedback(null, null, 575, ""));
		categoryFeedbacks.add(new CategoryFeedback(null, null, 576, null));
		categoryFeedbacks.add(new CategoryFeedback(null, null, null, "my own category"));

		String testPath = "test/test_file.pdf";
		List<ScreenshotFeedback> screenshotFeedbacks = new ArrayList<ScreenshotFeedback>();
		screenshotFeedbacks.add(new ScreenshotFeedback(null, null, testPath, 18290, "test_file", 55, null, "pdf"));

		Feedback feedback = new Feedback("Feedback title", 9999, 20, "u192039102",
				new Timestamp(123123123), null, "de", null, textFeedbacks, ratingFeedbacks, screenshotFeedbacks, null, null, null, categoryFeedbacks);

		String orchestratorUrl = "http://supersede.es.atos.net:8081/orchestrator/feedback/de/applications/" + feedback.getApplicationId() + "/configurations";

		mailService.NotifyOfFeedback(feedback.getApplicationId(), feedback, "ronnieschaniel@gmail.com", orchestratorUrl);
	}

	@Ignore
	@Test
	public void testGetConfigurationForApplication() {
		try {
			Map<String, Object> configuration = mailService.getConfigurationForApplication(20, null);
			System.out.println(configuration);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Test
	public void testGetValueForCategoryFeedback() {
		CategoryFeedback categoryFeedback1 = new CategoryFeedback(null, null, 575, "");
		CategoryFeedback categoryFeedback2 = new CategoryFeedback(null, null, 576, null);
		CategoryFeedback categoryFeedback3 = new CategoryFeedback(null, null, null, "my own category");

		JSONArray jsonArray = new JSONArray(testConfigurationJson);
		JSONObject pushConfiguration = jsonArray.getJSONObject(0);
		Map<String, Object> response = gson.fromJson(pushConfiguration.toString(), Map.class);

		Assert.assertEquals("Neue Funktion gewünscht", mailService.getValueForCategoryFeedback(response, categoryFeedback1));
		Assert.assertEquals("Allgemeines Feedback", mailService.getValueForCategoryFeedback(response, categoryFeedback2));
		Assert.assertEquals("my own category", mailService.getValueForCategoryFeedback(response, categoryFeedback3));
	};
}