import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

/**
 * Created 24.08.13 by vlaaad
 */
public class Generator {

    public static void main(String[] args) {
        TexturePacker2.Settings settings = new TexturePacker2.Settings();
        TexturePacker2.process(settings, "config/assets", "android/assets", "ui");
    }
}
