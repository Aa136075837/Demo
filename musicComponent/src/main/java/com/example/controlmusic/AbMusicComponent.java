package com.example.controlmusic;

import com.example.controlmusic.listener.IControlMusic;
import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/8/4.
 * @desc
 */

public abstract class AbMusicComponent extends AbComponent {

    public AbMusicComponent (SmartManager smartManager) {
        super (smartManager);
    }

    public void addControlMusicListener (IControlMusic listener) {
        if (!ListenerInfo.IControlMusics.contains (listener)) {
            ListenerInfo.IControlMusics.add (listener);
        }
    }

    public void removeControlMusicListener (IControlMusic listener) {
        if (!ListenerInfo.IControlMusics.contains (listener)) {
            ListenerInfo.IControlMusics.remove (listener);
        }
    }

    protected void dispatchPaseMusic() {
        for (IControlMusic iControlMusic : ListenerInfo.IControlMusics) {
            iControlMusic.psuseMusic();
        }
    }

    protected void dispatchContinueMusic() {
        for (IControlMusic iControlMusic : ListenerInfo.IControlMusics) {
            iControlMusic.continueMusic();
        }
    }

    protected void dispatchUpVolume() {
        for (IControlMusic iControlMusic : ListenerInfo.IControlMusics) {
            iControlMusic.upVolume();
        }
    }

    protected void dispatchDownVolume() {
        for (IControlMusic iControlMusic : ListenerInfo.IControlMusics) {
            iControlMusic.downVolume();
        }
    }

    protected void dispatchNextMusic() {
        for (IControlMusic iControlMusic : ListenerInfo.IControlMusics) {
            iControlMusic.nextMusic();
        }
    }

    protected void dispatchPreviousMusic() {
        for (IControlMusic iControlMusic : ListenerInfo.IControlMusics) {
            iControlMusic.previousMusic();
        }
    }

    @Override
    public void registerComponent () {
        mSmartManager.getIService ().registerComponent (this);
    }

    @Override
    public void unRegisterComponent () {
        mSmartManager.getIService ().unRegisterComponent (this);
    }

    public static class ListenerInfo {
        public static List<IControlMusic> IControlMusics = new ArrayList<> ();
    }
}
