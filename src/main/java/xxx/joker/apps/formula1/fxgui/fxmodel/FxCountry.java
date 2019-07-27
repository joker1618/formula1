package xxx.joker.apps.formula1.fxgui.fxmodel;

import javafx.scene.image.Image;
import xxx.joker.apps.formula1.model.entities.F1Country;

public class FxCountry {

    private F1Country country;
    private Image flagIcon;
    private Image flagImage;
//    private JkImage flagIcon;
//    private JkImage flagImage;

//    public FxCountry(F1Country country) {
//        this.country = country;
//        this.flagIcon = country.getFlagIcon().getUri().toJkImage();
//        this.flagImage = country.getFlagImage().getUri().toJkImage();
//    }


    public FxCountry(F1Country country, Image flagIcon, Image flagImage) {
        this.country = country;
        this.flagIcon = flagIcon;
        this.flagImage = flagImage;
    }

    public String getName() {
        return country.getName();
    }

    public String getCode() {
        return country.getCode();
    }

//    public JkImage getFlagIcon() {
//        return flagIcon;
//    }
//
//    public JkImage getFlagImage() {
//        return flagImage;
//    }

    public Image getFlagIcon() {
        return flagIcon;
    }

    public Image getFlagImage() {
        return flagImage;
    }
}
