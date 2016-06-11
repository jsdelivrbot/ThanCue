package ThanCue;

/**
 * Created by mike on 17/05/16.
 */
public class UnknownCue extends Cue {

    public UnknownCue() {
        this.setCueType(CueType.UNKNOWN);
    }

    @Override
    public void playCue() {
        System.out.println("Unknown cue has been played.");
    }
}
