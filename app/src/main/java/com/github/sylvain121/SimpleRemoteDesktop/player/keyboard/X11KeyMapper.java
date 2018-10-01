package com.github.sylvain121.SimpleRemoteDesktop.player.keyboard;

import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by sylvain on 15/02/18.
 */

public class X11KeyMapper {
    private static final String TAG = "X11_KEYCODE_MAPPER";

    public static int getKeysym(int keycode) {

        switch (keycode) {
            case KeyEvent.KEYCODE_0:
                return X11KeyBoardDefinition.XK_0;
            case KeyEvent.KEYCODE_1:
                return X11KeyBoardDefinition.XK_1;
            case KeyEvent.KEYCODE_2:
                return X11KeyBoardDefinition.XK_2;
            case KeyEvent.KEYCODE_3:
                return X11KeyBoardDefinition.XK_3;
            case KeyEvent.KEYCODE_4:
                return X11KeyBoardDefinition.XK_4;
            case KeyEvent.KEYCODE_5:
                return X11KeyBoardDefinition.XK_5;
            case KeyEvent.KEYCODE_6:
                return X11KeyBoardDefinition.XK_6;
            case KeyEvent.KEYCODE_7:
                return X11KeyBoardDefinition.XK_7;
            case KeyEvent.KEYCODE_8:
                return X11KeyBoardDefinition.XK_8;
            case KeyEvent.KEYCODE_9:
                return X11KeyBoardDefinition.XK_9;
            case KeyEvent.KEYCODE_STAR:
                //TODO
                return 0;
            case KeyEvent.KEYCODE_POUND:
                //TODO
                return 0;
            case KeyEvent.KEYCODE_DPAD_UP:
                return X11KeyBoardDefinition.XK_KP_Up;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return X11KeyBoardDefinition.XK_KP_Down;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return X11KeyBoardDefinition.XK_KP_Left;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return X11KeyBoardDefinition.XK_KP_Right;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                //TODO
                return 0;
            case KeyEvent.KEYCODE_CLEAR:
                return X11KeyBoardDefinition.XK_Clear;
            case KeyEvent.KEYCODE_A:
                return X11KeyBoardDefinition.XK_A;
            case KeyEvent.KEYCODE_B:
                return X11KeyBoardDefinition.XK_B;
            case KeyEvent.KEYCODE_C:

            case KeyEvent.KEYCODE_D:
            case KeyEvent.KEYCODE_E:
            case KeyEvent.KEYCODE_F:
            case KeyEvent.KEYCODE_G:
            case KeyEvent.KEYCODE_H:
            case KeyEvent.KEYCODE_I:
            case KeyEvent.KEYCODE_J:
            case KeyEvent.KEYCODE_K:
            case KeyEvent.KEYCODE_L:
            case KeyEvent.KEYCODE_M:
            case KeyEvent.KEYCODE_N:
            case KeyEvent.KEYCODE_O:
            case KeyEvent.KEYCODE_P:
            case KeyEvent.KEYCODE_Q:
            case KeyEvent.KEYCODE_R:
            case KeyEvent.KEYCODE_S:
            case KeyEvent.KEYCODE_T:
            case KeyEvent.KEYCODE_U:
            case KeyEvent.KEYCODE_V:
            case KeyEvent.KEYCODE_W:
            case KeyEvent.KEYCODE_X:
            case KeyEvent.KEYCODE_Y:
            case KeyEvent.KEYCODE_Z:
            case KeyEvent.KEYCODE_COMMA:
            case KeyEvent.KEYCODE_PERIOD:
            case KeyEvent.KEYCODE_ALT_LEFT:
            case KeyEvent.KEYCODE_ALT_RIGHT:
            case KeyEvent.KEYCODE_SHIFT_LEFT:
            case KeyEvent.KEYCODE_SHIFT_RIGHT:
            case KeyEvent.KEYCODE_TAB:
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_SYM:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DEL:
            case KeyEvent.KEYCODE_GRAVE:
            case KeyEvent.KEYCODE_MINUS:
            case KeyEvent.KEYCODE_EQUALS:
            case KeyEvent.KEYCODE_LEFT_BRACKET:
            case KeyEvent.KEYCODE_RIGHT_BRACKET:
            case KeyEvent.KEYCODE_BACKSLASH:
            case KeyEvent.KEYCODE_SEMICOLON:
            case KeyEvent.KEYCODE_APOSTROPHE:
            case KeyEvent.KEYCODE_SLASH:
            case KeyEvent.KEYCODE_AT:
            case KeyEvent.KEYCODE_NUM:
            case KeyEvent.KEYCODE_HEADSETHOOK:
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_PLUS:
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_NOTIFICATION:
            case KeyEvent.KEYCODE_SEARCH:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_STOP:
            case KeyEvent.KEYCODE_MEDIA_NEXT:
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
            case KeyEvent.KEYCODE_MUTE:
            case KeyEvent.KEYCODE_PAGE_UP:
            case KeyEvent.KEYCODE_PAGE_DOWN:
            case KeyEvent.KEYCODE_PICTSYMBOLS:   // switch symbol-sets (Emoji,Kao-moji)
            case KeyEvent.KEYCODE_SWITCH_CHARSET:   // switch char-sets (Kanji,Katakana)
            case KeyEvent.KEYCODE_ESCAPE:
            case KeyEvent.KEYCODE_FORWARD_DEL:
            case KeyEvent.KEYCODE_CTRL_LEFT:
            case KeyEvent.KEYCODE_CTRL_RIGHT:
            case KeyEvent.KEYCODE_CAPS_LOCK:
            case KeyEvent.KEYCODE_SCROLL_LOCK:
            case KeyEvent.KEYCODE_META_LEFT:
            case KeyEvent.KEYCODE_META_RIGHT:
            case KeyEvent.KEYCODE_BREAK:
            case KeyEvent.KEYCODE_MOVE_HOME:
            case KeyEvent.KEYCODE_MOVE_END:
            case KeyEvent.KEYCODE_INSERT:
            case KeyEvent.KEYCODE_FORWARD:
            case KeyEvent.KEYCODE_F1:
            case KeyEvent.KEYCODE_F2:
            case KeyEvent.KEYCODE_F3:
            case KeyEvent.KEYCODE_F4:
            case KeyEvent.KEYCODE_F5:
            case KeyEvent.KEYCODE_F6:
            case KeyEvent.KEYCODE_F7:
            case KeyEvent.KEYCODE_F8:
            case KeyEvent.KEYCODE_F9:
            case KeyEvent.KEYCODE_F10:
            case KeyEvent.KEYCODE_F11:
            case KeyEvent.KEYCODE_F12:
            case KeyEvent.KEYCODE_NUM_LOCK:
            case KeyEvent.KEYCODE_NUMPAD_0:
            case KeyEvent.KEYCODE_NUMPAD_1:
            case KeyEvent.KEYCODE_NUMPAD_2:
            case KeyEvent.KEYCODE_NUMPAD_3:
            case KeyEvent.KEYCODE_NUMPAD_4:
            case KeyEvent.KEYCODE_NUMPAD_5:
            case KeyEvent.KEYCODE_NUMPAD_6:
            case KeyEvent.KEYCODE_NUMPAD_7:
            case KeyEvent.KEYCODE_NUMPAD_8:
            case KeyEvent.KEYCODE_NUMPAD_9:
            case KeyEvent.KEYCODE_NUMPAD_DIVIDE:
            case KeyEvent.KEYCODE_NUMPAD_MULTIPLY:
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
            case KeyEvent.KEYCODE_NUMPAD_ADD:
            case KeyEvent.KEYCODE_NUMPAD_DOT:
            case KeyEvent.KEYCODE_NUMPAD_COMMA:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_EQUALS:
            case KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN:
            case KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN:
            default:
                Log.d(TAG, "keycode : " + keycode + " not implemented");
                break;
        }
        return 0;
    }
}
