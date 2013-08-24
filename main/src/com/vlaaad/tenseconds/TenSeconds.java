package com.vlaaad.tenseconds;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.vlaaad.common.App;
import com.vlaaad.tenseconds.states.*;

/**
 * Created 24.08.13 by vlaaad
 */
public class TenSeconds extends App {

    private MainMenuState mainMenuState;

    @Override
    protected void onCreate() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
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
                    startNewGame(3);
                }
            });
            setState(mainMenuState);
        }
    };

    private void startNewGame(int sides) {
        setState(new PlayState(onEndGame, sides));
    }

    private final PlayState.Callback onEndGame = new PlayState.Callback() {
        @Override
        public void onWin(final int sides, float totalTime) {
            setState(new WinState(new WinState.Callback() {
                @Override
                public void onGoToMainMenu() {
                    setState(mainMenuState);
                }

                @Override
                public void onContinue() {
                    startNewGame(sides + 2);
                }
            }, totalTime));
        }

        @Override
        public void onLose(int sides, float totalTime) {
            setState(new LoseState(new LoseState.Callback() {
                @Override
                public void onGoToMainMenu() {
                    setState(mainMenuState);
                }

                @Override
                public void onTryAgain() {
                    startNewGame(3);
                }
            }));
        }

        @Override
        public void onCancel() {
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
