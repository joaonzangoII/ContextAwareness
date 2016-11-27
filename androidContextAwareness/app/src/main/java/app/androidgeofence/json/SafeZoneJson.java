package app.androidgeofence.json;

import android.support.annotation.Nullable;

public class SafeZoneJson {
    public long id;
    public String name;
    public double latitude;
    public double longitude;
    public float radius;
    public String description;
    public String created_at;
    public String updated_at;

    public SafeZoneJson(final long id,
                        final String name,
                        final double latitutde,
                        final double longitude,
                        final float radius,
                        final String description,
                        @Nullable final String created_at,
                        @Nullable final String updated_at) {
        this.id = id;
        this.name = name;
        this.latitude = latitutde;
        this.longitude = longitude;
        this.radius = radius;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
