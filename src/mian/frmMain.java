package mian;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by mike on 15/05/16.
 */
public class frmMain {

    private JPanel pnlMain;
    private JList lstCues;
    private JButton btnNextCue;
    private JButton btnAddCue;
    private JButton btnMoveUp;
    private JButton btnMoveDown;
    private List<Cue> cueCollection;
    private Random r; // purely for adding test cues todo remove

    frmMain() {
        r = new Random();

        cueCollection = new ArrayList<>(); //this stores the latest cues. We can load this in from somewhere or create
        //new.
        populateCueCollection(); //this is a test.

        lstCues.setListData(cues());

        registerActionListeners();
    }

    private void registerActionListeners() {
        btnAddCue.addActionListener(actionEvent -> {
            Cue c = new SoundCue();
            c.setCueName("Test cue " + r.nextInt());
            cueCollection.add(c);
            lstCues.setListData(cues());
        });

        btnMoveUp.addActionListener(actionEvent -> {
            if (lstCues.isSelectionEmpty())
                return;
            int toMove = lstCues.getSelectedIndex(); // todo support moving multiple indices at once
            if (toMove > 0) {
                Cue toSwapWith = cueCollection.get(toMove - 1);
                cueCollection.set(toMove - 1, cueCollection.get(toMove));
                cueCollection.set(toMove, toSwapWith);
                lstCues.setListData(cues());
                lstCues.setSelectedIndex(toMove - 1);
            }
        });

        btnMoveDown.addActionListener(actionEvent -> {
            if (lstCues.isSelectionEmpty())
                return;
            int toMove = lstCues.getSelectedIndex(); // todo support moving multiple indices at once
            if (toMove < cueCollection.size() - 1) {
                Cue toSwapWith = cueCollection.get(toMove + 1);
                cueCollection.set(toMove + 1, cueCollection.get(toMove));
                cueCollection.set(toMove, toSwapWith);
                lstCues.setListData(cues());
                lstCues.setSelectedIndex(toMove + 1);
            }
        });

        btnNextCue.addActionListener(actionEvent -> playSelectedCue());
    }

    private Cue[] cues() { // todo if we want Object[], THEN we can just use cueCollection.toArray(); // todo look into models
        Object[] a = cueCollection.toArray();
        return Arrays.copyOf(a, a.length, Cue[].class);
    }

    private void playSelectedCue() {
        Cue selectedCue = (Cue) lstCues.getSelectedValue();

        //if no cue selected, simply ignore it.
        if (selectedCue == null) {
            System.out.println("Selected Cue is null");
            return; //add some exceptions maybe. For now this is good enough.
        }
        selectedCue.playCue();
    }

    private void populateCueCollection() {
        Cue c1 = new SoundCue();
        Cue c2 = new SoundCue();
        c1.setCueName("Ryan's cue");
        c2.setCueName("Mike's cue");
        cueCollection.add(c1);
        cueCollection.add(c2);
    }

    JPanel getPanel() {
        return this.pnlMain;
    }
}
