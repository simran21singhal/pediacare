package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by TANVIPC on 27-06-2017.
 */

public class SharedPrefrences {
    private static String PREF_NAME = "login";
    public static  String KEY_IS_LOGGED_IN = "isLoggedIn";
//    private final String REG_ID = "regidkey";
//    private final String TOKEN = "tokenkey";

    private SharedPreferences sharePresfs;
    private Context _context;
    private SharedPreferences.Editor editor;

    public String getREG_ID() {
        return this.sharePresfs.getString("REG_ID","1");
    }

    public String getTOKEN() {
//        return TOKEN;
        return this.sharePresfs.getString("TOKEN","hgvgvn");
    }

    public String getTYPE() {
        return this.sharePresfs.getString("TYPE","vghvjhb");
    }

    public SharedPrefrences(Context context) {
        this.sharePresfs = context.getSharedPreferences("pediacare", Context.MODE_PRIVATE);
        this._context = context;
        this.editor = this.sharePresfs.edit();
    }


    public void putRegId(String regid) {
        Log.d("shared pref reg id", regid);
        editor = sharePresfs.edit();
        editor.putString("REG_ID", regid);
        editor.commit();
    }
    public void putToken(String token) {
        editor = sharePresfs.edit();
        editor.putString("TOKEN", token);
        editor.commit();
    }
    public void putType(String type) {
        editor = sharePresfs.edit();
        editor.putString("TYPE", type);
        editor.commit();
    }


    public void setLogin(Boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "user login session end");
    }

    public boolean isLoggedIn() {
        return sharePresfs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
