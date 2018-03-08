package com.example.controlmusic;

import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;

/**
 * @author bo.
 * @Date 2017/8/4.
 * @desc
 */

public class MusicComponent extends AbMusicComponent {

    private static MusicComponent mMusicComponent;

    public static MusicComponent getInstance(SmartManager smartManager) {
        if (null == mMusicComponent) {
            synchronized (MusicComponent.class) {
                if (null == mMusicComponent) {
                    mMusicComponent = new MusicComponent(smartManager);
                }
            }
        }
        return mMusicComponent;
    }

    private MusicComponent(SmartManager smartManager) {
        super(smartManager);
        mSmartManager = smartManager;
    }

    @Override
    public void start() {

    }

    @Override
    public void dealAction(SmartAction action) {
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_PAUSE_MUSIC:
                int status = (bytes[8] & 0xff);
                if (0 == status) {
                    dispatchContinueMusic();
                } else if (1 == status) {
                    dispatchPaseMusic();
                }
                mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_NORMAL_ACK,bytes[4] & 0xff,0);
                break;
            case REQUEST_ACTION_ADJUST_VOLUME:
                int volume = (bytes[8] & 0xff);
                if (0 == volume) {
                    dispatchDownVolume();
                } else {
                    dispatchUpVolume();
                }
                break;
            case REQUEST_ACTION_SWITCH_MUSIC:
                int switchMusic = (bytes[8] & 0xff);
                if (0 == switchMusic) {
                    dispatchNextMusic();
                } else {
                    dispatchPreviousMusic();
                }
                break;
            default:
                break;
        }
    }
}
