package assign1;
/**
 * Enum that allows for easy of extension of Movie Review categories
 *
 * @author tesic
 */
public enum ReviewScore{
    NEGATIVE{
        @Override
        public String toString() {
            return "negative";
        }
    },POSITIVE{
        @Override
        public String toString() {
            return "positive";
        }
    }
}
