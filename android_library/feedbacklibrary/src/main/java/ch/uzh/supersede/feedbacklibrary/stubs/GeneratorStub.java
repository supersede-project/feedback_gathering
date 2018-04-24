package ch.uzh.supersede.feedbacklibrary.stubs;

import java.util.ArrayList;

import ch.uzh.supersede.feedbacklibrary.utils.NumberUtility;

public class GeneratorStub {

    private GeneratorStub() {
    }

    public static class BagOfFeedback {

        private BagOfFeedback() {
        }

        private static String[] feedbacks = new String[]{
                "The GUI is way too confusing.",
                "Response-Time is way too long, it's loading forever!!",
                "Colors are just too bright, i get a headache staring at this Application!!",
                "If i click on that button, the App crashes.",
                "We need more possibilities in the settings.",
                "This App totally needs a chat!",
                "I can't figure out how to do this ( see Screenshot )",
                "My Geo-Location always shows my position being in China - I'm from Detroit!!",
                "I bought the founders-package but i didn't get any rewards!!",
        };

        public static String pickRandom() {
            return feedbacks[NumberUtility.randomPosition(feedbacks)];
        }
        public static String[] pickRandomWithTitle() {
            int i = NumberUtility.randomPosition(feedbacks);
            return new String[]{feedbacks[i], BagOfFeedbackTitles.pickSpecific(i)};
        }
    }
    public static class BagOfFeedbackTitles {

        private BagOfFeedbackTitles() {
        }

        private static String[] titles = new String[]{
                "GUI",
                "Response-Time",
                "Colors",
                "Crash",
                "Settings",
                "Chat",
                "Sending Images",
                "GPS",
                "Refund",
        };

        public static String pickRandom() {
            return titles[NumberUtility.randomPosition(titles)];
        }

        public static String pickSpecific(int i) {
            return titles[i];
        }

    }

    public static class BagOfResponses {

        private BagOfResponses() {
        }

        private static String[] names = new String[]{
                "Nice idea!",
                "Cool!",
                "No way!",
                "Boring!",
                "We definitely need that one!",
                "This will never happen...",
                "I had about the same idea..",
                "That's totally a copy of another Feedback i saw recently.",
                "Why do you need that?",
                "I don't think it's bad, it's just not necessary.",
                "I discovered the same behaviour.",
                "Well..., dream on.",
                "Bro, seriously not!",
                "I appreciate this feedback.",
                "This wont change anything.",
                "Well, it works for me.",
                "I couldn't replicate what you describe.",
                "That's a really bad idea.",
                "That's just plain useless.",
                "I think they said they were working on something like that.",
                "Isn't this implemented alread?",
        };

        public static String pickRandom() {
            return names[NumberUtility.randomPosition(names)];
        }

    }
    public static class BagOfNames {

        private BagOfNames() {
        }

        private static String[] names = new String[]{
                "Daniel", "Josua", "Marco", "Hye", "Shena", "Shaunta", "Mirian", "Sherrell", "Alec", "Loren", "Hien", "Chas", "Belkis", "Thanh", "Hisako", "Jay", "Obdulia", "Yoshiko", "Tama",
                "Cordia", "Randall", "Tory", "Layne", "Vania", "Lady", "Heather", "Belia", "Venice", "Genevive", "Monroe", "Linnie", "Jessi", "Odessa", "Arleen", "Carmen", "Jeanette", "Ileana",
                "Lia", "Shantay", "Audrie", "Garrett", "Gloria", "Antoine", "Hazel", "Kamala", "Leeanne", "Yang", "Ethyl", "Jerrell", "Herbert", "Marcela", "Erich", "Vesta"
        };

        public static String pickRandom() {
            return names[NumberUtility.randomPosition(names)];
        }
    }

    public static class BagOfLabels {

        private BagOfLabels() {
        }

        private static String[] labels = new String[]{
                "GUI", "Images", "Scaling", "Performance", "Permissions", "Data-Usage", "Improvement", "Development", "Translations",
                "Battery", "Privacy", "Crashes", "Bugs", "Functionality", "Ideas", "Updates", "Region-Lock", "Content", "Features",
                "Design", "Handling", "Usability", "Audio", "Sensors", "Brightness", "GPS", "Accuracy", "Quality"
        };

        public static String pickRandom() {
            return labels[NumberUtility.randomPosition(labels)];
        }

        public static String[] pickRandom(int maxCount){
            ArrayList<String> labels = new ArrayList<>();
            int upperBound = NumberUtility.randomInt(1,maxCount);
            for (int i = 0; i < upperBound; i++){
                labels.add(pickRandom());
            }
            return labels.toArray(new String[labels.size()]);
        }
    }
}
