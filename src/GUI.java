import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by harsh on 2017-03-25.
 */
public class GUI {

    public GridPane addGridPane(Pos pos, int HGap, int VGap, Insets padding) {
        GridPane grid = new GridPane();
        grid.setAlignment(pos);
        grid.setHgap(HGap);
        grid.setVgap(VGap);
        grid.setPadding(padding);

        return grid;
    }

    public Text addText(String text, Font font) {
        Text scenetitle = new Text(text);
        scenetitle.setFont(font);

        return scenetitle;
    }

    public Button addButton(String text, Boolean visibility) {
        Button button = new Button(text);
        button.setVisible(visibility);

        return button;
    }
}
