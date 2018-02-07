import javax.swing.*;
import java.awt.*;

class HibaShow {

    private JLabel hibaText;

    HibaShow(JLabel h){
        this.hibaText = h;
    }

    void show(Color c, String m, boolean el){
        hibaText.setForeground(c);
        hibaText.setText(m);
        hibaText.setVisible(true);
        if(el) {
            new hibaEl(hibaText).start();
        }
    }
}
