package xxx.joker.libs.core.javafx;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import xxx.joker.libs.core.exception.JkRuntimeException;
import xxx.joker.libs.core.files.JkFiles;
import xxx.joker.libs.core.utils.JkStrings;

import java.nio.file.Path;
import java.util.List;

public class JfxUtil2 {

	public static ImageView createImageView(Integer fitWidth, Integer fitHeight) {
		return createImageView1(null, fitWidth, fitHeight, true);
	}
	public static ImageView createImageView(Path imgPath, Integer fitWidth, Integer fitHeight) {
		return createImageView(new Image(JkFiles.toURL(imgPath)), fitWidth, fitHeight);
	}
	public static ImageView createImageView(Image image, Integer fitWidth, Integer fitHeight) {
		return createImageView1(image, fitWidth, fitHeight, true);
	}
	public static ImageView createImageView(Image image, Integer fitWidth, Integer fitHeight, boolean preserveRatio) {
		return createImageView1(image, fitWidth, fitHeight, preserveRatio);
	}
	private static ImageView createImageView1(Image image, Integer fitWidth, Integer fitHeight, boolean preserveRatio) {
		ImageView imageView = new ImageView();
		if(image != null)	imageView.setImage(image);
		imageView.setPreserveRatio(preserveRatio);
		if(fitWidth != null)	imageView.setFitWidth(fitWidth);
		if(fitHeight != null)	imageView.setFitHeight(fitHeight);
		return imageView;
	}

	public static HBox createHBox(String styleClasses, Node... nodes) {
		HBox hbox = new HBox(nodes);
		List<String> scList = JkStrings.splitList(styleClasses, " ");
		hbox.getStyleClass().addAll(scList);
		return hbox;
	}

	public static Window getWindow(Event e) {
		return ((Node)e.getSource()).getScene().getWindow();
	}

	public static Stage getStage(Event e) {
		return (Stage)getWindow(e);
	}

	public static <T extends Node> T getChildren(Node root, int... childrenIndexes) {
		try {
			Node tmp = root;
			for (int idx : childrenIndexes) {
				tmp = ((Pane)tmp).getChildren().get(idx);
			}
			return (T) tmp;

		} catch (Exception ex) {
			throw new JkRuntimeException(ex);
		}
	}
}
