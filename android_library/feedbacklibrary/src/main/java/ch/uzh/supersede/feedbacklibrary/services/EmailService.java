package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.content.*;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;

import java.net.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.components.views.*;
import ch.uzh.supersede.feedbacklibrary.feedback.*;
import ch.uzh.supersede.feedbacklibrary.utils.*;

public class EmailService {
    private static final String TAG = "EmailService";
    private static EmailService instance;

    private EmailService() {
    }

    public static EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }
        return instance;
    }

    public void checkAndSendViaEmail(final Activity activity, CheckBox sendViaEmailCheckbox, EditText emailEditText, final Feedback feedback, final List<MechanismView> mechanismViews) {
        if (!sendViaEmailCheckbox.isChecked()) {
            return;
        }

        final String email = emailEditText.getText().toString();
        if (Utils.isEmailValid(email)) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Thread emailThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    execSendMail(activity, email, feedback, mechanismViews);
                }
            });
            emailThread.start();
        } else {
            DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.invalid_email)}, true);
        }
    }

    //TODO: Funktionalitaet geht noch nicht.
    private static void execSendMail(final Activity activity, final String email, final Feedback feedback, final List<MechanismView> mechanismViews) {
        final String username = "supersede.zurich@gmail.com";
        final String password = System.getenv("F2F_SMTP_EMAIL_PASSWORD");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Copy of your feedback");

            BodyPart messageBodyPart = new MimeBodyPart();
            String feedbackText = feedback.getTextFeedback().get(0).getText();

            CategoryMechanismView categoryMechanismView = (CategoryMechanismView) mechanismViews.get(4);
            String category = categoryMechanismView.getCategorySpinner().getSelectedItem().toString();

            if (category.equals("My feedback is about…")) {
                category = "-";
            }

            RatingMechanismView ratingMechanismView = (RatingMechanismView) mechanismViews.get(3);
            String rating = String.valueOf(ratingMechanismView.getRating());

            // Now set the actual message
            messageBodyPart.setText(String.format("Feedback text: %s, Rating: %s, Category: %s", feedbackText, rating, category));

            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            for (ScreenshotFeedback screenshotFeedback : feedback.getScreenshotFeedback()) {
                addAttachment(multipart, screenshotFeedback.getImagePath());
            }
            for (AudioFeedback audioFeedback : feedback.getAudioFeedback()) {
                addAttachment(multipart, audioFeedback.getAudioPath());
            }

            message.setContent(multipart);
            Transport.send(message);

            SharedPreferences sharedPreferences = activity.getSharedPreferences("FeedbackApp", Context.MODE_PRIVATE);
            String savedEmail = sharedPreferences.getString("email", "");
            if (TextUtils.isEmpty(savedEmail)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.apply();
            }

        } catch (MessagingException e) {
            Log.e(e.getMessage(), e.toString());
            DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
        }
    }

    private static void addAttachment(Multipart multipart, String filePath) throws MessagingException {
        URI uri;
        try {
            uri = new URI(filePath);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Failed to create URI", e);
            return;
        }
        String[] segments = uri.getPath().split("/");
        String filename = segments[segments.length - 1];

        DataSource source = new FileDataSource(filePath);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
    }
}
