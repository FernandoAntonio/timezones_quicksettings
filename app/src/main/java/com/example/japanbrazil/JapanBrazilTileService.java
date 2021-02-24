package com.example.japanbrazil;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import java.util.TimeZone;

import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;


enum LocalTimeZoneType {
    japan, brazil, other
}

public class JapanBrazilTileService extends TileService {
    private LocalTimeZoneType localTimeZoneType;


    @Override
    public void onStartListening() {
        super.onStartListening();
        checkZoneId();
    }

    @Override
    public void onClick() {
        super.onClick();
        try {
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
            startActivity(new Intent(Settings.ACTION_DATE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_MULTIPLE_TASK));

            checkZoneId();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "An unexpected error occurred", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void checkZoneId() {
        String initialZoneId = TimeZone.getDefault().toZoneId().toString();

        switch (initialZoneId) {
            case "Asia/Tokyo":
                localTimeZoneType = LocalTimeZoneType.japan;
                break;
            case "America/Sao_Paulo":
                localTimeZoneType = LocalTimeZoneType.brazil;
                break;
            default:
                localTimeZoneType = LocalTimeZoneType.other;
                break;
        }

        updateTileData();
    }

    private void updateTileData() {
        String label;
        int icon;

        switch (localTimeZoneType) {
            case japan:
                label = "Japan";
                icon = R.drawable.ic_japan;
                break;
            case brazil:
                label = "Brazil";
                icon = R.drawable.ic_brazil;
                break;
            default:
                label = "Other";
                icon = R.drawable.ic_world;
                break;
        }

        getQsTile().setIcon(Icon.createWithResource(getApplicationContext(), icon));
        getQsTile().setLabel(label);
        getQsTile().setContentDescription("Timezones");
        getQsTile().updateTile();
    }
}
