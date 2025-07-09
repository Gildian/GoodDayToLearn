import javax.swing.UIManager;

public class TestLookAndFeel {
    public static void main(String[] args) {
        try {
            String laf = UIManager.getSystemLookAndFeel();
            System.out.println("System Look and Feel: " + laf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
