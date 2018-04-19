package ch.uzh.supersede.feedbacklibrary.stubs;

import ch.uzh.supersede.feedbacklibrary.utils.NumberUtility;

public class GeneratorStub {

    private GeneratorStub() {
    }

    public static class BagOfFeedback {

        private BagOfFeedback() {
        }

        private static String[] topics = new String[]{
                "GUI confusing.", "Response-Time too long.", "Colors too bright.", "I like this feature.", "We need more possibilities."
        };

        public static String pickRandom() {
            return topics[NumberUtility.randomPosition(topics)];
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
    }
}
