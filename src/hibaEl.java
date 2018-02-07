import javax.swing.*;

public class hibaEl extends Thread {

    private JLabel hibaText;

    hibaEl(JLabel h){
        this.hibaText = h;
    }

    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(3000);
            hibaText.setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
