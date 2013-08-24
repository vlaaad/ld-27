package com.vlaaad.tenseconds;

import com.badlogic.gdx.assets.AssetManager;
import com.vlaaad.common.App;
import com.vlaaad.tenseconds.states.InitState;
import com.vlaaad.tenseconds.states.MainMenuState;
import com.vlaaad.tenseconds.states.PlayState;

/**
 * Created 24.08.13 by vlaaad
 */
public class TenSeconds extends App {

    private MainMenuState mainMenuState;

    @Override
    protected void onCreate() {
        AppConfig.assetManager = new AssetManager();
        InitState initState = new InitState(onInitialized);
        setState(initState);
    }

    private final InitState.Callback onInitialized = new InitState.Callback() {
        @Override
        public void onInitialized() {
            mainMenuState = new MainMenuState(new MainMenuState.Callback() {
                @Override
                public void onStartNewGame() {
                    startNewGame();
                }
            });
            setState(mainMenuState);
        }
    };

    private void startNewGame() {
        setState(new PlayState(onEndGame));
    }

    private final PlayState.Callback onEndGame = new PlayState.Callback() {
        @Override
        public void onWin(float totalTime) {
            setState(mainMenuState);
        }

        @Override
        public void onLose(float totalTime) {
            setState(mainMenuState);
        }
    };

    @Override
    protected void onDispose() {
        AppConfig.assetManager.dispose();
    }

    @Override
    protected float getScreenScale() { return 2; }
}
