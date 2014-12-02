/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mc.program;

import java.util.TimerTask;
import javax.swing.JLabel;

/**
 *
 * @author
 * Jeffrey
 */
public class TimerTick extends TimerTask {

    JLabel label;

    public TimerTick(JLabel s) {

        super();
        label = s;
    }

    @Override
    public void run() {

        String[] parts = label.getText().split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int second = Integer.parseInt(parts[2]);

        //if timer reaches 0
        if ((hour == 0) & (minute == 0) & (second == 0)) {
                MCProgram.getFrame().checkAnswer("F");

        } else {

            second -= 1;

            if (second == -1) {
                second = 59;
                minute -= 1;
            }

            if (minute == -1) {
                minute = 59;
                hour -= 1;
            }
            String strHour = Integer.toString(hour);
            String strMin = Integer.toString(minute);
            String strSec = Integer.toString(second);
            if (strMin.length() == 1) {
                strMin = "0" + strMin;
            }
            if (strSec.length() == 1) {
                strSec = "0" + strSec;
            }

            label.setText(strHour + ":" + strMin + ":" + strSec);
        }
    }
}
