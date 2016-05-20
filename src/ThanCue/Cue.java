package ThanCue;

import javax.swing.*;

/**
 * Created by ryan on 15/05/16.
 */
public abstract class Cue {

    private String cueType;
    private String cueName;
    private CueBehaviour behaviour;

    public Cue() {
        cueType = "Unset Cue";
        cueName = "Unset Cue";
        behaviour = CueBehaviour.PLAY_ON_GO;
    }

    String getCueName() {
        return cueName;
    }

    String getCueType() {
        return cueType;
    }

    ImageIcon getIcon(){
        return Cue.getIcon(cueType);
    }

    CueBehaviour getBehaviour() {
        return behaviour;
    }

    void setCueType(String cueType) {
        this.cueType = cueType;
    }

    void setCueName(String name) {
        cueName = name;
    }

    void setBehaviour(CueBehaviour behaviour) {
        this.behaviour = behaviour;
    }

    @Override
    public String toString() {
        return getCueType() + " - " + getCueName();
    }

    public Object[] getAttributeArray(){
        return new Object[]{0, Cue.getIcon(cueType), cueType, cueName, behaviour.name().toLowerCase().replace("_"," ")};
    }

    public abstract void playCue(); //this plays the cue, be it lighting, sound, video or table flipping

    private static ImageIcon getIcon(String type){
        /*

        Note: required to do this way, else ALL table entries get the icon of the most recently added cue

         */
        switch(type.toLowerCase()){
            case "sound":
                return new ImageIcon("img/music.png");
            case "light":
                return new ImageIcon("img/light.png");
            case "video":
                return new ImageIcon("img/video.png");
            default:
                return new ImageIcon("img/unknown.png");
        }
    }
}
